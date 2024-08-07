package com.schoolprojects.corrreps.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolprojects.corrreps.models.Course
import com.schoolprojects.corrreps.models.CourseScore
import com.schoolprojects.corrreps.models.PaidFee
import com.schoolprojects.corrreps.models.RegisteredCourse
import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.utils.Common
import com.schoolprojects.corrreps.utils.Common.coursesCollectionRef
import com.schoolprojects.corrreps.utils.Common.fireStoreDB
import com.schoolprojects.corrreps.utils.Common.mAuth
import com.schoolprojects.corrreps.utils.Common.registeredCoursesCollectionRef
import com.schoolprojects.corrreps.utils.Common.studentsCollectionRef
import com.schoolprojects.corrreps.utils.HelpMe.calculateGPA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class StudentHomeViewModel @Inject constructor() : ViewModel() {

    private val TAG = "StudentHomeViewModel"

    val studentInfo = mutableStateOf<Student>(Student())
    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)

    private val _cgpa = MutableStateFlow<Double?>(0.0)
    val cgpa: StateFlow<Double?> get() = _cgpa

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    fun updateStudentInfo(value: Student) {
        this.studentInfo.value = value
    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    init {
        calculateCGPA()
    }

    fun getStudentInfo(
        onLoading: (isLoading: Boolean) -> Unit,
        onStudentDataFetched: (studentData: Student) -> Unit,
        onStudentNotFetched: (error: String) -> Unit
    ) {
        //val studentQuery = studentsCollectionRef.whereEqualTo("studentId", studentId)
        val studentQuery = studentsCollectionRef.whereEqualTo("studentId", mAuth.uid!!)

        onLoading(true)

        studentQuery.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val student = document.toObject(Student::class.java)
                student?.let { data ->
                    onStudentDataFetched(data)
                    onLoading(false)
                }
            }

        }.addOnFailureListener { exception ->
            onLoading(false)
            onStudentNotFetched(exception.message ?: "Some error occurred")
        }
    }

    // Function to search for a payment by reference
    fun searchPaymentByReference(
        paymentRef: String,
        onPaymentFound: (PaidFee) -> Unit,
        onPaymentNotFound: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        onLoading(true)
        Common.feePaymentCollection
            .whereEqualTo("paymentRef", paymentRef)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val paidFee = document.toObject(PaidFee::class.java)
                    onLoading(false)
                    onPaymentFound(paidFee!!)
                } else {
                    onLoading(false)
                    onPaymentNotFound("No payment found with the provided reference.")
                }
            }
            .addOnFailureListener { exception ->
                onLoading(false)
                // Handle failure
                onPaymentNotFound(exception.localizedMessage?.toString() ?: "Some error occurred")
            }
    }

    fun getCoursesByLevelAndSemester(
        level: String,
        semester: String,
        onCoursesFetched: (List<Course>) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {

        getCarryOverCourses(semester, onCoursesFetched = { carryoverCourses ->
            val coursesToRegister = mutableListOf<Course>()
            coursesToRegister.addAll(carryoverCourses)
            coursesCollectionRef
                .whereEqualTo("courseLevel", level)
                .whereEqualTo("courseSemester", semester)
                .get()
                .addOnSuccessListener { result ->
                    val courses = result.documents.mapNotNull { it.toObject(Course::class.java) }
                    coursesToRegister.addAll(courses)
                    onCoursesFetched(coursesToRegister)
                    onLoading(false)
                }
                .addOnFailureListener { exception ->
                    onLoading(false)
                    // Handle the error accordingly
                }
        }, onLoading)

        onLoading(true)

    }

    fun getCarryOverCourses(
        semester: String,
        onCoursesFetched: (List<Course>) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {
        registeredCoursesCollectionRef.whereEqualTo("studentId", mAuth.uid!!)
            .whereEqualTo("scoreSemester", semester)
            .whereLessThan("totalScore", 40).get()
            .addOnSuccessListener { result ->
                // Extract course IDs where totalScore is less than 40
                val courseIds = result.documents.mapNotNull { it.getString("courseId") }
                if (courseIds.isEmpty()) {
                    // If no courses are found, indicate loading is complete and return an empty list
                    onCoursesFetched(emptyList())
                    onLoading(false)
                    return@addOnSuccessListener
                }

                // Query the courses collection using the extracted course IDs
                Common.coursesCollectionRef
                    .whereIn("courseCode", courseIds)
                    .get()
                    .addOnSuccessListener { courseResult ->
                        // Map the documents to Course objects
                        val courses = courseResult.documents.mapNotNull { document ->
                            val course = document.toObject(Course::class.java)
                            // Append "(C/O)" to the course title if course is not null
                            course?.let {
                                it.copy(courseTitle = "${it.courseTitle} (C/O)")
                            }
                        }

                        // Return the fetched courses
                        onCoursesFetched(courses)
                        // Indicate loading is complete
                        onLoading(false)
                    }
                    .addOnFailureListener { exception ->
                        // Handle error for fetching courses
                        onLoading(false)
                        Log.d(TAG, "getCarryOverCourses: ${exception.message!!}")
                        // Optionally, handle the error by invoking a callback or logging the error
                    }
            }
            .addOnFailureListener { exception ->
                // Handle error for fetching registered courses
                onLoading(false)
                Log.d(TAG, "getCarryOverCourses: ${exception.message!!}")

                // Optionally, handle the error by invoking a callback or logging the error
            }
    }

    fun registerCourses(
        courses: List<CourseScore>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        courses.forEach { course ->
            val courseRef = registeredCoursesCollectionRef.document(course.scoreId)
            fireStoreDB.set(courseRef, course.toMap())
        }

        // Commit the batch
        fireStoreDB.commit()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Function to fetch registered courses for a student
    fun getRegisteredCourses(
        studentId: String,
        level: String,
        semester: String,
        onCoursesFetched: (List<RegisteredCourse>, Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        registeredCoursesCollectionRef
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("scoreLevel", level)
            .whereEqualTo("scoreSemester", semester)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val registeredCourses = mutableListOf<RegisteredCourse>()

                for (document in querySnapshot.documents) {
                    val courseScore = document.toObject(CourseScore::class.java)
                    if (courseScore != null) {
                        // Fetch course details using courseId
                        coursesCollectionRef
                            .whereEqualTo("courseCode", courseScore.courseId)
                            .get()
                            .addOnSuccessListener { courseSnapshot ->
                                for (courseDocument in courseSnapshot.documents) {
                                    val course = courseDocument.toObject(Course::class.java)
                                    if (course != null) {
                                        // Combine course score and details
                                        registeredCourses.add(
                                            RegisteredCourse(
                                                courseTitle = course.courseTitle,
                                                courseCode = course.courseCode,
                                                creditUnits = course.creditUnits,
                                                caScore = courseScore.caScore,
                                                examScore = courseScore.examScore,
                                                totalScore = courseScore.totalScore
                                            )
                                        )
                                    }
                                }
                                // Calculate GPA
                                val gpa = calculateGPA(registeredCourses)
                                // Callback with the list of registered courses and GPA
                                onCoursesFetched(registeredCourses, gpa)
                            }
                            .addOnFailureListener { e ->
                                Log.d(TAG, "getRegisteredCourses: $e")
                                onFailure(e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Function to fetch courses and calculate CGPA
    fun calculateCGPA() {
        _loading.value = true

        registeredCoursesCollectionRef
            .whereEqualTo("studentId", mAuth.uid!!)
            .get()
            .addOnSuccessListener { result ->
                val courses = result.documents.mapNotNull { it.toObject(CourseScore::class.java) }
                _cgpa.value = computeCGPA(courses)
                _loading.value = false
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching courses: ${exception.message}")
                _loading.value = false
            }
    }

    // Helper function to compute CGPA from a list of courses
    private fun computeCGPA(courses: List<CourseScore>): Double {
        if (courses.isEmpty()) return 0.0

        var totalWeightedGradePoints = 0.0
        var totalCreditUnits = 0

        courses.forEach { course ->
            Log.d(TAG, "computeCGPA: $course")
            val gradePoint = getGradePoint(course.totalScore)
            totalWeightedGradePoints += gradePoint * course.courseCreditUnit.toInt()
            totalCreditUnits += course.courseCreditUnit.toInt()
        }

        return if (totalCreditUnits == 0) {
            0.0
        } else {
            totalWeightedGradePoints / totalCreditUnits
        }
    }

    // Function to determine grade points from total score
    private fun getGradePoint(totalScore: Int): Double {
        return when {
            totalScore >= 70 -> 5.0 // A
            totalScore >= 60 -> 4.0 // B
            totalScore >= 50 -> 3.0 // C
            totalScore >= 45 -> 2.0 // D
            totalScore >= 40 -> 1.0 // E
            else -> 0.0  // F
        }
    }
    private fun CourseScore.toMap(): Map<String, Any> {
        return mapOf(
            "scoreId" to scoreId,
            "studentId" to mAuth.uid.toString(),
            "courseId" to courseId,
            "caScore" to caScore,
            "examScore" to examScore,
            "totalScore" to totalScore,
            "scoreSemester" to scoreSemester,
            "scoreLevel" to scoreLevel,
            "courseCreditUnit" to courseCreditUnit
        )
    }

}