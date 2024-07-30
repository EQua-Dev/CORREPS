package com.schoolprojects.corrreps.models

data class CourseScore(
    val scoreId: String = "",
    val studentId: String = "",
    val courseId: String = "",
    val caScore: String = "0",
    val examScore: String = "0",
    val totalScore: String = "0",
    val scoreSemester: String = "",
    val scoreLevel: String = "",
)
