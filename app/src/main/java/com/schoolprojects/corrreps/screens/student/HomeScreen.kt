package com.schoolprojects.corrreps.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavHostController
import com.schoolprojects.corrreps.components.ExpandableCard
import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.navigation.Screen
import com.schoolprojects.corrreps.network.getStudentInfo
import com.schoolprojects.corrreps.ui.theme.Typography
import com.schoolprojects.corrreps.utils.Common
import com.schoolprojects.corrreps.utils.Common.mAuth
import com.schoolprojects.corrreps.viewmodels.StudentHomeViewModel

@Composable
fun StudentHomeScreen(
    baseNavHostController: NavHostController,
    onNavigationRequested: (String, Boolean) -> Unit,
    onSemesterSelected: (level: String, semester: String) -> Unit,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {

    val studentData by remember {
        studentHomeViewModel.studentInfo
    }
    val errorMessage = remember { mutableStateOf("") }
    val showLoading by remember { mutableStateOf(studentHomeViewModel.showLoading) }
    val openDialog by remember { mutableStateOf(studentHomeViewModel.openDialog) }



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


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hello, ${studentData.studentFirstName}",
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(4.dp),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            studentHomeViewModel.updateDialogStatus()
                        })
                }
            }

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 8.dp,
                end = 8.dp
            )
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        val studentName =
                            "${studentData.studentFirstName} ${studentData.studentLastName}"
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
            items(Common.Levels.entries) { level ->
                ExpandableCard(level, onSemesterClicked = { semester ->
                    onSemesterSelected(level.level, semester)
                })
            }

        }
        if (showLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))

            }
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            title = {
                Text(text = "Logout", style = Typography.titleLarge)
            },
            text = {
                Text(text = "Do you want to logout?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Common.mAuth.signOut()
                        baseNavHostController.navigate(Screen.Login.route)
                        studentHomeViewModel.updateDialogStatus()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        studentHomeViewModel.updateDialogStatus()
                    }
                ) {
                    Text("No")
                }
            },

            )
    }


}