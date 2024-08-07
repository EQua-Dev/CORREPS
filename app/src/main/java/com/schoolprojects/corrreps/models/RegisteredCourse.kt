package com.schoolprojects.corrreps.models

data class RegisteredCourse(

    val courseTitle: String = "",
    val courseCode: String = "",
    val creditUnits: String = "",
    val caScore: String = "",
    val examScore: String = "",
    val totalScore: Int = 0
)
