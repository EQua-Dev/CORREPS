package com.schoolprojects.corrreps.screens.lecturer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.schoolprojects.corrreps.ui.theme.Typography
import com.schoolprojects.corrreps.viewmodels.LecturerHomeViewModel
import kotlinx.coroutines.delay

@Composable
fun LecturerSemesterScreen(
    modifier: Modifier = Modifier, level: String,
    semester: String,
    onViewStudent: (studentId: String, level: String, semester: String) -> Unit,
    baseNavHostController: NavController,
    lecturerHomeViewModel: LecturerHomeViewModel = hiltViewModel()
) {

    // Trigger the fetch operation on initialization
    LaunchedEffect(Unit) {
        // Adding a delay to ensure the view model is ready (optional)
        delay(1000)
        lecturerHomeViewModel.fetchStudents(level, semester)
    }

    val students = lecturerHomeViewModel.studentsList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the selected level and semester
        Text(
            text = "Level: $level, Semester: $semester",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // List of students
        if (students.isEmpty()) {
            Text("No students found for this level and semester.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(students) { student ->
                    StudentItem(student = student, navigate = {
                        onViewStudent(student.studentId, level, semester)
                    })
                }
            }
        }
    }


}