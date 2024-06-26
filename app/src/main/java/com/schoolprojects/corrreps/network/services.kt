package com.schoolprojects.corrreps.network

import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.utils.Common.studentsCollectionRef

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
