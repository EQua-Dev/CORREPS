package com.schoolprojects.corrreps.screens.lecturer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.models.CourseScore
import com.schoolprojects.corrreps.ui.theme.Typography
import com.schoolprojects.corrreps.viewmodels.LecturerHomeViewModel
import kotlinx.coroutines.launch

@Composable
fun StudentCoursesScreen(
    modifier: Modifier = Modifier,
    studentId: String,
    level: String,
    semester: String,
    lecturerHomeViewModel: LecturerHomeViewModel = hiltViewModel()
) {

    // State to manage dialog visibility and scores
    var showDialog by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<CourseScore?>(null) }
    val courses by lecturerHomeViewModel.courses.collectAsState()

    // Trigger the fetch operation
    LaunchedEffect(Unit) {
        lecturerHomeViewModel.fetchCourses(studentId, level, semester)
    }

    // Dialog State
    val caScoreState = remember { mutableStateOf(TextFieldValue("0")) }
    val examScoreState = remember { mutableStateOf(TextFieldValue("0")) }

    // Composable Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display Level and Semester
        Text(
            text = "Level: $level, Semester: $semester",
            style = Typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display list of courses
        if (courses.isEmpty()) {
            Text("No registered courses found.")
        } else {
            LazyColumn {
                items(courses) { course ->
                    CourseTile(
                        course = course,
                        onClick = {
                            selectedCourse = course
                            caScoreState.value = TextFieldValue(course.caScore)
                            examScoreState.value = TextFieldValue(course.examScore)
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    // Show dialog if a course is selected
    if (showDialog && selectedCourse != null) {
        ScoreEntryDialog(
            course = selectedCourse!!,
            caScoreState = caScoreState,
            examScoreState = examScoreState,
            onDismiss = { showDialog = false },
            onSubmit = { caScore, examScore ->
                lecturerHomeViewModel.updateCourseScore(selectedCourse!!, caScore, examScore)
                showDialog = false
            }
        )
    }

}


@Composable
fun ScoreEntryDialog(
    course: CourseScore,
    caScoreState: MutableState<TextFieldValue>,
    examScoreState: MutableState<TextFieldValue>,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val errorState = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Enter Scores for ${course.courseId}") },
        text = {
            Column {
                OutlinedTextField(
                    value = caScoreState.value,
                    onValueChange = {
                        if (it.text.toIntOrNull() ?: 0 <= 30) {
                            caScoreState.value = it
                            errorState.value = ""
                        } else {
                            errorState.value = "CA score must not exceed 30."
                        }
                    },
                    label = { Text("CA Score") },
                    isError = errorState.value.isNotEmpty()
                )
                OutlinedTextField(
                    value = examScoreState.value,
                    onValueChange = {
                        if (it.text.toIntOrNull() ?: 0 <= 70) {
                            examScoreState.value = it
                            errorState.value = ""
                        } else {
                            errorState.value = "Exam score must not exceed 70."
                        }
                    },
                    label = { Text("Exam Score") },
                    isError = errorState.value.isNotEmpty()
                )
                if (errorState.value.isNotEmpty()) {
                    Text(
                        text = errorState.value,
                        color = MaterialTheme.colorScheme.error,
                        style = Typography.labelLarge
                    )
                }
                Text(
                    text = "Total Score: ${(caScoreState.value.text.toIntOrNull() ?: 0) + (examScoreState.value.text.toIntOrNull() ?: 0)}",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        onSubmit(caScoreState.value.text, examScoreState.value.text)
                    }
                }
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}