package com.schoolprojects.corrreps.utils

import com.schoolprojects.corrreps.models.Course
import com.schoolprojects.corrreps.utils.Common.coursesCollectionRef

object HelpMe {


    fun getCourseInfo(
        courseCode: String,
        onLoading: (isLoading: Boolean) -> Unit,
        onCourseDataFetched: (courseData: Course) -> Unit,
        onCourseNotFetched: (error: String) -> Unit
    ) {

        /*the course code is what I have, so I have to either:
        * a. change it from storing course code to storing course id and then always fetch it from here
        * b. in this function, go to the courses collection and fetch only the course that has that course code (sounds better)
        * */
        val courseQuery = coursesCollectionRef.whereEqualTo("courseCode", courseCode)

        onLoading(true)

        courseQuery.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val course = document.toObject(Course::class.java)
                course?.let { data ->
                    onLoading(false)
                    onCourseDataFetched(data)
                }
            }

        }.addOnFailureListener { exception ->
            onLoading(false)
            onCourseNotFetched(exception.message ?: "Some error occurred")
        }
    }

}