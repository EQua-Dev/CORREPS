/*
 * Copyright (c) 2024.
 * Luomy EQua
 * Under Awesomeness Studios
 */

package com.schoolprojects.corrreps.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
//import org.devstrike.persacg.models.Course
//import org.devstrike.persacg.models.Lecturer
//import org.devstrike.persacg.models.RegisteredCourse
//import org.devstrike.persacg.models.Student
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round

object Common {

    val mAuth = FirebaseAuth.getInstance()
    val fireStoreDB = Firebase.firestore.batch()

    enum class UserTypes(val userType: String) {
        STUDENT("Student"),
        LECTURER("Lecturer"),
    }

    enum class Levels(val level: String){
        ONE("100 Level"),
        TWO("200 Level"),
        THREE("300 Level"),
        FOUR("400 Level"),
    }



    private const val STUDENTS_REF = "Correps Students"
    private const val LECTURERS_REF = "Correps Lecturers"
    private const val COURSES_REF = "Courses"
    //const val HEALTH_RECORD_REF = "Patrealysis Health Record"

    val studentsCollectionRef = Firebase.firestore.collection(STUDENTS_REF)
    val lecturersCollectionRef = Firebase.firestore.collection(LECTURERS_REF)
    val coursesCollectionRef = Firebase.firestore.collection(COURSES_REF)
    //val healthRecordCollectionRef = Firebase.firestore.collection(HEALTH_RECORD_REF)

    fun logout() {
        mAuth.signOut()
    }

    /*fun getStudentInfo(
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

    fun getCourseInfo(
        courseCode: String,
        onLoading: (isLoading: Boolean) -> Unit,
        onCourseDataFetched: (courseData: Course) -> Unit,
        onCourseNotFetched: (error: String) -> Unit
    ) {

        *//*the course code is what I have, so I have to either:
        * a. change it from storing course code to storing course id and then always fetch it from here
        * b. in this function, go to the courses collection and fetch only the course that has that course code (sounds better)
        * *//*
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

    fun getLecturerInfo(
        lecturerId: String,
        onLoading: (isLoading: Boolean) -> Unit,
        onLecturerDataFetched: (lecturerData: Lecturer) -> Unit,
        onLecturerNotFetched: (error: String) -> Unit
    ) {

        val lecturersQuery = lecturersCollectionRef.whereEqualTo("lecturerId", lecturerId)

        onLoading(true)

        lecturersQuery.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val lecturer = document.toObject(Lecturer::class.java)
                lecturer?.let { data ->
                    onLecturerDataFetched(data)
                    onLoading(false)
                }
            }

        }.addOnFailureListener { exception ->
            onLoading(false)
            onLecturerNotFetched(exception.message ?: "Some error occurred")
        }
    }
*/
    fun roundToSignificantFigures(number: Double, significantFigures: Int): Double {
        if (number == 0.0) return 0.0

        val d = ceil(log10(abs(number)))
        val power = significantFigures - d.toInt()

        val magnitude = 10.0.pow(power)
        val shifted = round(number * magnitude)
        return shifted / magnitude
    }

    fun whatHonours(cgpa: Double): String {
        return when {
            cgpa in 4.5..5.0 -> "First Class"
            cgpa in 3.5..4.49 -> "Second Class Upper"
            cgpa in 2.5..3.49 -> "Second Class Lower"
            cgpa in 1.5..2.49 -> "Third Class"
            cgpa in 1.0..1.49 -> "Pass"
            else -> "Fail"
        }
    }

    fun gradeCreditScore(grade: String): Int {
        return when (grade) {
            "A" -> 5
            "B" -> 4
            "C" -> 3
            "D" -> 2
            "E" -> 1
            else -> 0
        }
    }

    fun whatGrade(grade: Int): String {
        return when (grade) {
            in 70..100 -> "A"
            in 60..69 -> "B"
            in 50..59 -> "C"
            in 45..49 -> "D"
            in 40..44 -> "E"
            else -> "F"
        }
    }

    fun getFloorScore(grade: String): Int {
        return when (grade) {
            "A" -> 70
            "B" -> 60
            "C" -> 50
            "D" -> 45
            "E" -> 40
            else -> 39
        }
    }

    fun adviceOnGoalTrack(desiredGoal: String, currentTotal: Int): String {
        /*
                * The advice is to be this way:
                * if the student desires A, then subtract their current score from the floor score of their desired grade
                * then tell the student what they need to score to reach their goal
                *
                * if the difference is between 0 and 10, then add that they are on track
                * if the difference is between 11 and 20, then add that they just need to put more effort
                * if the difference is between 21 and 30, then add that they need to put much hard effort
                * if the difference is above 31, then add that they need to try very hard
                *
                *
                * Step 1: get the floor score for the desired goal
                * */

        return when (val gradeScoreDifference = getFloorScore(desiredGoal).minus(currentTotal)) {
            in 0..10 -> "You need to score at least $gradeScoreDifference to reach your desired grade. \nWell-done, you are on track"
            in 11..20 -> "You need to score at least $gradeScoreDifference to reach your desired grade. \nGood job, you just need little more effort"
            in 21..30 -> "You need to score at least $gradeScoreDifference to reach your desired grade. \nYou need to work harder, and smarter"
            in 31..40 -> "You need to score at least $gradeScoreDifference to reach your desired grade. \nThat's a bit much, you need to put much more effort"
            else -> "You need to score at least $gradeScoreDifference to reach your desired grade. \nOuch!, that's much. You need to work really hard"
        }
    }

    fun adviceOnResult(desiredGoal: String, currentTotal: Int): String {
        /*
                * The advice is to be this way:
                * if the student desires A, then subtract their current score from the floor score of their desired grade
                * then tell the student what they need to score to reach their goal
                *
                * if the difference is between 0 and 10, then add that they are on track
                * if the difference is between 11 and 20, then add that they just need to put more effort
                * if the difference is between 21 and 30, then add that they need to put much hard effort
                * if the difference is above 31, then add that they need to try very hard
                *
                *
                * Step 1: get the floor score for the desired goal
                * */
        val gradeScoreDifference = getFloorScore(desiredGoal).minus(currentTotal)
        Log.d("TAG", "adviceOnResult: $gradeScoreDifference")
        return when (gradeScoreDifference) {
            in 0..10 -> "You missed your desired grade by $gradeScoreDifference. \nTough luck, you were almost there"
            in 11..20 -> "You missed your desired grade by $gradeScoreDifference. \nOuch! That's sad. Do better next time"
            in 21..30 -> "You missed your desired grade by $gradeScoreDifference. \nThat's a bit much, you need to work harder, and smarter next time"
            in 31..40 -> "You missed your desired grade by $gradeScoreDifference. \nThat's quite much, you need to put much more effort next time"

            in -10..0 -> "You surpassed your desired grade by ${gradeScoreDifference.times(-1)}. \nWow!, that's cool, Well done."
            in -20..-11 -> "You surpassed your desired grade by ${gradeScoreDifference.times(-1)}. \nGood job, Good job!"
            in -30..-21 -> "You surpassed your desired grade by ${gradeScoreDifference.times(-1)}. \nWow!, that's nice, Keep it upGood job!"
            in -40..-31 -> "You surpassed your desired grade by ${gradeScoreDifference.times(-1)}. \nWow!, that's great, Way to go!"
            in -50..-41 -> "You surpassed your desired grade by ${gradeScoreDifference.times(-1)}. \nWow!, that's superb!. You're the bomb!"
            else -> "You missed your desired grade by $gradeScoreDifference. \nThat's an awful lot. You need to do really harder next time"
        }
    }


    /*fun calculateGoalsGPA(courseCredit: Course, studentCourses: List<RegisteredCourse>): Double {
        val desiredGoals = studentCourses
        val valuesToCalculate: MutableList<Pair<Double, Int>> = mutableListOf()

        for (course in desiredGoals) {
            if (courseCredit.courseCode == course.courseCode) {
                val valueToAdd =
                    gradeCreditScore(course.desiredGrade).times(courseCredit.creditUnits.toInt())
                        .toDouble()
                valuesToCalculate.add(Pair(valueToAdd, courseCredit.creditUnits.toInt()))
                Log.d("TAG", "course: $course")
                Log.d("TAG", "desiredGoals: $desiredGoals")
                Log.d("TAG", "valueToAdd: $valueToAdd")
                Log.d("TAG", "valuesToCalculate: $valuesToCalculate")
            }
        }
        val scoreGrades = mutableListOf<Double>()
        val scoreCreditUnits = mutableListOf<Double>()
        valuesToCalculate.forEach {
            scoreGrades.add(it.first)
            scoreCreditUnits.add(it.second.toDouble())
        }

        return roundToSignificantFigures(scoreGrades.sum().div(scoreCreditUnits.sum()), 3)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCourseInfoSuspend(courseCode: String): Course? =
        suspendCancellableCoroutine { cont ->
            getCourseInfo(
                courseCode,
                onLoading = { *//* Optionally handle loading *//* },
                onCourseDataFetched = { courseDetail ->
                    cont.resume(courseDetail) { throwable -> *//* Optionally handle cancellation *//* }
                },
                onCourseNotFetched = {
                    cont.resume(null) { throwable -> *//* Optionally handle cancellation *//* }
                }
            )
        }
*/
    /*
    suspend fun calculateActualGPA(
        studentCourses: List<RegisteredCourse>,
    ): Double {
        val desiredGoals = studentCourses
        val valuesToCalculate: MutableList<Pair<Double, Int>> = mutableListOf()
       */
    /* val scoreGrades = mutableListOf<Double>()
        val scoreCreditUnits = mutableListOf<Double>()*//*



        coroutineScope {
            val deferreds = desiredGoals.map { course ->
                async(Dispatchers.IO) {
                    val courseDetail = getCourseInfoSuspend(course.courseCode)
                    courseDetail?.let {
                        val valueToAdd = gradeCreditScore(whatGrade(course.totalScore.toInt())).times(it.creditUnits.toInt()).toDouble()
                        valuesToCalculate.add(Pair(valueToAdd, it.creditUnits.toInt()))
                    }
                }
            }

            deferreds.awaitAll()
        }
      */
    /*  val deferred = CoroutineScope(Dispatchers.IO).async {
                var gpa = 0.0
                for (course in desiredGoals) {
                    getCourseInfo(
                        course.courseCode,
                        onLoading = {},
                        onCourseDataFetched = {
                            if (it.courseCode == course.courseCode) {
                                val valueToAdd =
                                    gradeCreditScore(whatGrade(course.totalScore.toInt())).times(it.creditUnits.toInt())
                                        .toDouble()
//                onAddValue(Pair(valueToAdd, courseDetail.creditUnits.toInt()))
                                valuesToCalculate.add(Pair(valueToAdd, it.creditUnits.toInt()))
                                Log.d("TAG", "valuesToCalculate: $valuesToCalculate")
                                valuesToCalculate.forEach {
                                    scoreGrades.add(it.first)
                                    scoreCreditUnits.add(it.second.toDouble())
                                }

                            }

                        },
                        onCourseNotFetched = {})

                }

                gpa = roundToSignificantFigures(
                    scoreGrades.sum().div(scoreCreditUnits.sum()), 3
                )
                Log.d("TAG", "ActualGPA: $gpa")
                return@async gpa
        }


        Log.d("TAG", "valuesToCalculate final: ${runBlocking { deferred.await() }}")
        return runBlocking { deferred.await() }


    }*//*


    val scoreGrades = valuesToCalculate.map { it.first }
    val scoreCreditUnits = valuesToCalculate.map { it.second.toDouble() }

    val gpa = if (scoreCreditUnits.sum() != 0.0) {
        roundToSignificantFigures(scoreGrades.sum() / scoreCreditUnits.sum(), 3)
    } else {
        0.0
    }

    Log.d("TAG", "ActualGPA: $gpa")
    Log.d("TAG", "valuesToCalculate final: $valuesToCalculate")
    return gpa

}*/

    /*suspend fun calculateActualGPA(studentCourses: List<RegisteredCourse>): Double {
        val valuesToCalculate = mutableListOf<Pair<Double, Int>>()

        coroutineScope {
            val deferreds = studentCourses.map { course ->
                async(Dispatchers.IO) {
                    val courseDetail = getCourseInfoSuspend(course.courseCode)
                    courseDetail?.let {
                        val valueToAdd =
                            gradeCreditScore(whatGrade(course.totalScore.toInt())).times(it.creditUnits.toInt())
                                .toDouble()
                        valuesToCalculate.add(Pair(valueToAdd, it.creditUnits.toInt()))
                    }
                }
            }
            deferreds.awaitAll()
        }

        val scoreGrades = valuesToCalculate.map { it.first }
        val scoreCreditUnits = valuesToCalculate.map { it.second.toDouble() }

        val gpa = if (scoreCreditUnits.sum() != 0.0) {
            roundToSignificantFigures(scoreGrades.sum() / scoreCreditUnits.sum(), 3)
        } else {
            0.0
        }
        return gpa
    }*/
}
