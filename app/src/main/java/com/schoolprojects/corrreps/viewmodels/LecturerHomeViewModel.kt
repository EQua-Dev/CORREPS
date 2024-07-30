package com.schoolprojects.corrreps.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class LecturerHomeViewModel @Inject constructor() : ViewModel() {

    private val TAG = "LecturerHomeViewModel"

    val studentInfo = mutableStateOf<Student>(Student())
    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)


    private val _courses = MutableStateFlow<List<CourseScore>>(emptyList())
    val courses: StateFlow<List<CourseScore>> get() = _courses


    fun updateStudentInfo(value: Student) {
        this.studentInfo.value = value
    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    // MutableStateList to hold the fetched students
    var studentsList = mutableStateListOf<Student>()
        private set

    // State for holding selected level and semester
    var selectedLevel by mutableStateOf("")
    var selectedSemester by mutableStateOf("")

    // Function to fetch students by level and semester
    fun fetchStudents(level: String, semester: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = studentsCollectionRef
                    .whereEqualTo("studentCurrentLevel", level)
                    .whereEqualTo("studentCurrentSemester", semester)
                    .get()
                    .await()

                val students =
                    querySnapshot.documents.mapNotNull { it.toObject(Student::class.java) }

                // Update the list with fetched students
                studentsList.clear()
                studentsList.addAll(students)

            } catch (e: Exception) {
                // Handle errors appropriately
                studentsList.clear()
                e.printStackTrace()
            }
        }
    }



    // Fetch courses for a specific student, level, and semester
    fun fetchCourses(studentId: String, level: String, semester: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coursesQuery = registeredCoursesCollectionRef
                    .whereEqualTo("studentId", studentId)
                    .whereEqualTo("scoreLevel", level)
                    .whereEqualTo("scoreSemester", semester)
                    .get()
                    .await()

                val courseScores = coursesQuery.documents.map { document ->
                    document.toObject(CourseScore::class.java) ?: CourseScore()
                }
                _courses.value = courseScores
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Update course score in Firestore
    fun updateCourseScore(courseScore: CourseScore, caScore: String, examScore: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalScore = (caScore.toInt() + examScore.toInt()).toString()
                val updatedCourseScore = courseScore.copy(
                    caScore = caScore,
                    examScore = examScore,
                    totalScore = totalScore
                )

                registeredCoursesCollectionRef.document(courseScore.scoreId)
                    .set(updatedCourseScore)
                    .await()

                // Refresh courses list after update
                fetchCourses(
                    courseScore.studentId,
                    courseScore.scoreLevel,
                    courseScore.scoreSemester
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}