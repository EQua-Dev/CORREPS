package com.schoolprojects.corrreps.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolprojects.corrreps.models.Course
import com.schoolprojects.corrreps.models.RegisteredCourse
import com.schoolprojects.corrreps.ui.theme.Typography


@Composable
fun CourseItem(
    course: Course,
    isSelected: Boolean,
    onCourseSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onCourseSelected(!isSelected) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onCourseSelected(it) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "${course.courseCode} - ${course.courseTitle}", fontSize = 16.sp)
            Text(text = "Credit Units: ${course.creditUnits}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}


@Composable
fun RegisteredCourseItem(course: RegisteredCourse) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = course.courseTitle, style = Typography.bodyLarge)
                Text(text = "Course Code: ${course.courseCode}", style = Typography.bodyMedium)
                Text(text = "Credit Units: ${course.creditUnits}", style = Typography.bodyMedium)
                Text(text = "CA Score: ${course.caScore}", style = Typography.bodyMedium)
                Text(text = "Exam Score: ${course.examScore}", style = Typography.bodyMedium)
                Text(text = "Total Score: ${course.totalScore}", style = Typography.bodyMedium)
            }
        }
    }
}