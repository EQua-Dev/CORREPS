package com.schoolprojects.corrreps.screens.student

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.models.Course
import com.schoolprojects.corrreps.models.CourseScore
import com.schoolprojects.corrreps.models.PaidFee
import com.schoolprojects.corrreps.models.RegisteredCourse
import com.schoolprojects.corrreps.utils.Common.mAuth
import com.schoolprojects.corrreps.viewmodels.StudentHomeViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterScreen(
    level: String,
    semester: String,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var isDialogOpen by remember { mutableStateOf(false) }
    var paymentReference by remember { mutableStateOf("") }

    val studentName: String = "Kalvin"
    val studentRegNumber: String = "CST/2016419"
    val courseList = remember { mutableStateListOf<Course>() }
    val selectedCourses = remember { mutableStateListOf<Course>() }
    var totalCreditUnits by remember { mutableStateOf(0) }
    val courses = mutableListOf<CourseScore>() // Assume Course is a data class for course info
    /*onRegisterClick: (String) -> Unit, // Callback for registration reference submission
    checkPaymentReference: (String) -> Boolean*/

    val foundPayment = remember { mutableStateOf(PaidFee()) }

    val TAG = "SemesterScreen"
    // Store registered courses
    val registeredCourses = remember { mutableStateListOf<RegisteredCourse>() }
    // Fetch registered courses when the screen is first displayed
    LaunchedEffect(Unit) {

        studentHomeViewModel.getRegisteredCourses(
            studentId = mAuth.uid!!,
            level = level,
            semester = semester,
            onCoursesFetched = { courses ->
                registeredCourses.clear()
                registeredCourses.addAll(courses)
                Log.d(TAG, "SemesterScreen: ${registeredCourses.toList()}")
            },
            onFailure = { e ->
                Toast.makeText(context, "Failed to fetch courses: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Name: $studentName", fontSize = 14.sp)
                            Text(text = "Reg No: $studentRegNumber", fontSize = 14.sp)
                        }
                        if (registeredCourses.isEmpty() && foundPayment.value.paymentRef.isBlank()) {
                            Button(onClick = { isDialogOpen = true }) {
                                Text(text = "Register")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Level: $level Semester: $semester",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )


                if (courseList.isEmpty() && foundPayment.value.paymentRef.isBlank() && registeredCourses.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No registered courses",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else if (registeredCourses.isEmpty()) {
                    Text(
                        text = "Total Credit Units: $totalCreditUnits",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                    // Display registered courses if available
                    courseList.forEach { course ->
                        CourseItem(
                            course = course,
                            isSelected = selectedCourses.contains(course),
                            onCourseSelected = { isSelected ->
                                if (isSelected) {
                                    selectedCourses.add(course)
                                    totalCreditUnits += course.creditUnits.toInt()
                                } else {
                                    selectedCourses.remove(course)
                                    totalCreditUnits -= course.creditUnits.toInt()
                                }
                            }
                        )
                    }

                    if (selectedCourses.size > 0) {
                        Button(
                            onClick = {
                                val registeredCourses = selectedCourses.map {
                                    CourseScore(
                                        scoreId = UUID.randomUUID()
                                            .toString(), // Unique ID for each course
                                        studentId = mAuth.uid!!,
                                        courseId = it.courseCode, // Using course code as ID
                                        caScore = "0", // Default score, can be updated later
                                        examScore = "0",
                                        totalScore = "0",
                                        scoreSemester = semester,
                                        scoreLevel = level
                                    )
                                }
                                studentHomeViewModel.registerCourses(
                                    courses = registeredCourses,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Courses registered successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        selectedCourses.clear() // Clear selection after registration
                                        courseList.clear() // Optionally clear the course list
                                        studentHomeViewModel.getRegisteredCourses(
                                            studentId = mAuth.uid!!,
                                            level = level,
                                            semester = semester,
                                            onCoursesFetched = { fetchedCourses ->
                                                registeredCourses.toMutableList().clear()
                                                registeredCourses.toMutableList().addAll(courses)
                                            },
                                            onFailure = {

                                            })
                                    },
                                    onFailure = { e ->
                                        Toast.makeText(
                                            context,
                                            "Registration failed: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                                // onRegisterClick(paymentReference)
                                // checkPaymentReference(paymentReference)
                                isDialogOpen = false
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Register")
                        }
                    }
                } else {
                    LazyColumn {
                        items(registeredCourses) { course ->
                            RegisteredCourseItem(course)
                        }
                    }
                }
            }
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text(text = "Enter Payment Reference") },
            text = {
                Column {
                    TextField(
                        value = paymentReference,
                        onValueChange = { paymentReference = it },
                        label = { Text("Reference Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {

                        studentHomeViewModel.searchPaymentByReference(
                            paymentReference,
                            onPaymentFound = {

                                if (it.semesterPaid != semester && it.levelPaid != level) {
                                    Toast.makeText(
                                        context,
                                        "Payment not found for the selected semester or level",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    foundPayment.value = it

                                    studentHomeViewModel.getCoursesByLevelAndSemester(
                                        level = level,
                                        semester = semester,
                                        onCoursesFetched = { courses ->
                                            courseList.clear()
                                            courseList.addAll(courses)
                                            isDialogOpen = false
                                        },
                                        onLoading = {}
                                    )
                                }


                            },
                            onPaymentNotFound = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            },
                            onLoading = {})

                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                IconButton(onClick = { isDialogOpen = false }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }
        )
    }
}
