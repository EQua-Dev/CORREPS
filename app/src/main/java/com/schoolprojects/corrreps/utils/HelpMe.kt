package com.schoolprojects.corrreps.utils

import com.schoolprojects.corrreps.models.Course
import com.schoolprojects.corrreps.models.CourseScore
import com.schoolprojects.corrreps.models.RegisteredCourse
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

    fun calculateGPA(courses: List<RegisteredCourse>): Double {
        var totalPoints = 0.0
        var totalCredits = 0.0

        for (course in courses) {
            val creditUnits = course.creditUnits.toDoubleOrNull() ?: 0.0
            val totalScore = course.totalScore.toDouble() ?: 0.0

            val gradePoints = when {
                totalScore >= 70 -> 5.0
                totalScore >= 60 -> 4.0
                totalScore >= 50 -> 3.0
                totalScore >= 45 -> 2.0
                totalScore >= 40 -> 1.0
                else -> 0.0
            }

            totalPoints += gradePoints * creditUnits
            totalCredits += creditUnits
        }

        return if (totalCredits > 0) totalPoints / totalCredits else 0.0
    }


    fun calculateCGPA(courses: List<CourseScore>): Double {
        var totalCredits = 0.0
        var weightedScoreSum = 0.0

        courses.forEach { course ->

            val courseCredits = course.courseCreditUnit.toDoubleOrNull() ?: 0.0
            val totalScore = course.totalScore.toDouble() ?: 0.0

            weightedScoreSum += totalScore * courseCredits
            totalCredits += courseCredits
        }

        return if (totalCredits > 0) weightedScoreSum / totalCredits else 0.0
    }

    fun getLatestLevelAndSemester(courses: List<CourseScore>): Pair<String, String> {
        var latestLevel = ""
        var latestSemester = ""

        courses.forEach { course ->
            if (course.scoreLevel > latestLevel) {
                latestLevel = course.scoreLevel
                latestSemester = course.scoreSemester
            } else if (course.scoreLevel == latestLevel && course.scoreSemester > latestSemester) {
                latestSemester = course.scoreSemester
            }
        }

        return Pair(latestLevel, latestSemester)
    }

}