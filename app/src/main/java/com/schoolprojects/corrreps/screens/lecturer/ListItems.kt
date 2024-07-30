package com.schoolprojects.corrreps.screens.lecturer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.schoolprojects.corrreps.models.CourseScore
import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.ui.theme.Typography

@Composable
fun StudentItem(
    student: Student,
    navigate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navigate()
                // Navigate to the StudentDetailScreen with the student's registration number
                // navController.navigate("${Routes.StudentDetailScreen}/${student.studentId}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val studentName = "${student.studentFirstName} ${student.studentLastName}"
            Text(text = "Name: $studentName", style = Typography.bodyLarge)
            Text(text = "Reg No: ${student.studentRegNo}", style = Typography.bodyMedium)
        }
    }
}

@Composable
fun CourseTile(course: CourseScore, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Course Code: ${course.courseId}", style = Typography.bodyLarge)
            Text(text = "CA Score: ${course.caScore}", style = Typography.bodyMedium)
            Text(text = "Exam Score: ${course.examScore}", style = Typography.bodyMedium)
            Text(text = "Total Score: ${course.totalScore}", style = Typography.bodyMedium)
        }
    }
}