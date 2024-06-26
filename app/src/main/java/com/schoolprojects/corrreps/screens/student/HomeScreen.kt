package com.schoolprojects.corrreps.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.network.getStudentInfo
import com.schoolprojects.corrreps.utils.Common.mAuth
import com.schoolprojects.corrreps.viewmodels.StudentHomeViewModel

@Composable
fun StudentHomeScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {

    val studentData by remember {
        studentHomeViewModel.studentInfo
    }
    val errorMessage = remember { mutableStateOf("") }
    val showLoading by remember { mutableStateOf(studentHomeViewModel.showLoading) }


    LaunchedEffect(key1 = null) {
        getStudentInfo(
            mAuth.uid!!,
            onLoading = {
                studentHomeViewModel.updateLoadingStatus(it)
            },
            onStudentDataFetched = { student ->
                studentHomeViewModel.updateStudentInfo(student)
            },
            onStudentNotFetched = { error ->
                errorMessage.value = error
            })
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                val studentName = "${studentData.studentFirstName} ${studentData.studentLastName}"
                Text(text = studentName)
                Text(text = studentData.studentRegNo)
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Column {
                Text(text = "Level: ${studentData.studentCurrentLevel}")
                Text(text = "Semester: ${studentData.studentCurrentSemester}")
                Text(text = studentData.studentCGPA, fontWeight = FontWeight.Bold)
            }
        }
    }
    if (showLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))

        }
    }

}