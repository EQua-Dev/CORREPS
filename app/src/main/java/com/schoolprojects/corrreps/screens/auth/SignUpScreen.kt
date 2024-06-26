package com.schoolprojects.corrreps.screens.auth

import CustomTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.components.DropdownField
import com.schoolprojects.corrreps.components.FlatButton
import com.schoolprojects.corrreps.navigation.Screen
import com.schoolprojects.corrreps.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    onAccountCreated: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    val departments = listOf(
        "Computer Science",
        "Mechanical Engineering",
        "Electrical Engineering",
        "Business Administration"
    )
    val genders = listOf("Male", "Female", "Other")


    val showLoading = remember { mutableStateOf(false) }


    val selectedGenderType = remember {
        mutableStateOf("")
    }

    val currentSelectedAuthIndex by remember { authViewModel.currentSelectedGenderIndex }


    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                placeholder = "First Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                placeholder = "Last Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = regNumber,
                onValueChange = { regNumber = it },
                label = "Reg Number",
                placeholder = "Reg Number",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "Email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                selectedValue = selectedDepartment,
                onValueChange = { selectedDepartment = it },
                label = "School Department",
                options = departments,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "School Department"
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                selectedValue = selectedGender,
                onValueChange = { selectedGender = it },
                label = "Gender",
                options = genders,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Gender"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FlatButton(
                text = "Sign Up",
                onClick = {
                    onNavigationRequested(Screen.Login.route, false)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Have Account? ")
                TextButton(onClick = { /* Handle create account */ }) {
                    Text("Login Instead")
                }


            }
        }
        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }
    }
}
