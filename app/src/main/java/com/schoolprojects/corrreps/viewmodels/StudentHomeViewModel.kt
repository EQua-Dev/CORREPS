package com.schoolprojects.corrreps.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StudentHomeViewModel @Inject constructor() : ViewModel() {
    
    private val TAG = "StudentHomeViewModel"

    val studentInfo = mutableStateOf<Student>(Student())
    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)


    fun updateStudentInfo(value: Student) {
        this.studentInfo.value = value
    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
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

        onLoading(true)
        Common.coursesCollectionRef
            .whereEqualTo("courseLevel", level)
            .whereEqualTo("courseSemester", semester)
            .get()
            .addOnSuccessListener { result ->
                val courses = result.documents.mapNotNull { it.toObject(Course::class.java) }
                onCoursesFetched(courses)
                onLoading(false)
            }
            .addOnFailureListener { exception ->
                onLoading(false)
                // Handle the error accordingly
            }
    }

    fun registerCourses(courses: List<CourseScore>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

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
        onCoursesFetched: (List<RegisteredCourse>) -> Unit,
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
                                Log.d(TAG, "getRegisteredCourses: $registeredCourses")
                                // Callback with the list of registered courses
                                onCoursesFetched(registeredCourses)
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


    private fun CourseScore.toMap(): Map<String, Any> {
        return mapOf(
            "scoreId" to scoreId,
            "studentId" to mAuth.uid.toString(),
            "courseId" to courseId,
            "caScore" to caScore,
            "examScore" to examScore,
            "totalScore" to totalScore,
            "scoreSemester" to scoreSemester,
            "scoreLevel" to scoreLevel
        )
    }

}