package com.schoolprojects.corrreps.network

import android.util.Log
import com.schoolprojects.corrreps.models.CourseScore
import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.utils.Common.registeredCoursesCollectionRef
import com.schoolprojects.corrreps.utils.Common.studentsCollectionRef
import com.schoolprojects.corrreps.utils.HelpMe.calculateCGPA
import com.schoolprojects.corrreps.utils.HelpMe.getLatestLevelAndSemester
import kotlin.math.log

val TAG= "services"
fun getStudentInfo(
    studentId: String,
    onLoading: (isLoading: Boolean) -> Unit,
    onStudentDataFetched: (studentData: Student) -> Unit,
    onStudentNotFetched: (error: String) -> Unit
) {
    //val studentQuery = studentsCollectionRef.whereEqualTo("studentId", studentId)
    val studentQuery = studentsCollectionRef.whereEqualTo("studentId", studentId)

    onLoading(true)

    studentQuery.get().addOnSuccessListener { querySnapshot ->
        for (document in querySnapshot.documents) {
            val student = document.toObject(Student::class.java)
            Log.d(TAG, "getStudentInfo: $student")
            student?.let { data ->
                fetchRegisteredCourses(studentId) { courses, error ->
                    if (error != null) {
                        onLoading(false)
                        onStudentNotFetched(error)
                        return@fetchRegisteredCourses
                    }

                    // Calculate CGPA
                    val cgpa = calculateCGPA(courses)

                    Log.d("TAG", "getStudentInfo: $courses")

                    // Find the latest level and semester
                    val (latestLevel, latestSemester) = getLatestLevelAndSemester(courses)

                    // Update student information
                    val updatedStudent = data.copy(
                        studentCGPA = cgpa.toString(),
                        studentCurrentLevel = latestLevel,
                        studentCurrentSemester = latestSemester
                    )

                    // Update Firestore
                    studentsCollectionRef.document(studentId).set(updatedStudent)
                        .addOnSuccessListener {
                            onStudentDataFetched(updatedStudent)
                            Log.d("TAG", "getStudentInfo: $updatedStudent")
                            onLoading(false)
                        }
                        .addOnFailureListener { exception ->
                            onLoading(false)
                            onStudentNotFetched(exception.message ?: "Some error occurred")
                        }
                }
            } ?: run {
                onLoading(false)
                onStudentNotFetched("Student data not found")
            }
        }
    }.addOnFailureListener { exception ->
        onLoading(false)
        onStudentNotFetched(exception.message ?: "Some error occurred")
    }
}


private fun fetchRegisteredCourses(
    studentId: String,
    onCoursesFetched: (List<CourseScore>, String?) -> Unit
) {
    registeredCoursesCollectionRef
        .whereEqualTo("studentId", studentId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val courses = querySnapshot.documents.mapNotNull { document ->
                document.toObject(CourseScore::class.java)
            }
            onCoursesFetched(courses, null)
        }
        .addOnFailureListener { exception ->
            onCoursesFetched(emptyList(), exception.message)
        }
}