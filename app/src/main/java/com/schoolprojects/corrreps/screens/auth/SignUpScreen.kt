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
import com.schoolprojects.corrreps.components.CustomSnackbar
import com.schoolprojects.corrreps.components.DropdownField
import com.schoolprojects.corrreps.components.FlatButton
import com.schoolprojects.corrreps.components.PasswordStrengthIndicator
import com.schoolprojects.corrreps.navigation.Screen
import com.schoolprojects.corrreps.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    onAccountCreated: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var showSnackbar by remember { mutableStateOf(false) }

    var errorMessage = remember {
        mutableStateOf("")
    }
    var firstName by remember { authViewModel.studentFirstName }
    var lastName by remember { authViewModel.studentLastName }
    var regNumber by remember { authViewModel.matricNo }
    var email by remember { authViewModel.email }
    var password by remember { authViewModel.password }
    val passwordStrength by remember { authViewModel.passwordStrength }
    var selectedDepartment by remember { authViewModel.studentDepartment }
    var selectedGender by remember { authViewModel.gender }

    val departments = listOf(
        "Computer Science",
    )
    val genders = listOf("Male", "Female")


    val showLoading = remember { authViewModel.showLoading }


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
                onValueChange = { authViewModel.updateFirstName(it) },
                label = "First Name",
                placeholder = "First Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = lastName,
                onValueChange = { authViewModel.updateLastName(it) },
                label = "Last Name",
                placeholder = "Last Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = regNumber,
                onValueChange = { authViewModel.updateMatricNo(it) },
                label = "Reg Number",
                placeholder = "Reg Number",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = email,
                onValueChange = { authViewModel.updateEmail(it) },
                label = "Email",
                placeholder = "Email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = password,
                onValueChange = { authViewModel.updatePassword(it) },
                label = "Password",
                placeholder = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordStrengthIndicator(passwordStrength)


            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                selectedValue = selectedDepartment,
                onValueChange = { authViewModel.updateStudentDepartment(it) },
                label = "School Department",
                options = departments,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "School Department"
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                selectedValue = selectedGender,
                onValueChange = { authViewModel.updateGender(it) },
                label = "Gender",
                options = genders,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Gender"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FlatButton(
                text = "Sign Up",
                onClick = {
                    authViewModel.createStudent(onLoading = {
                        authViewModel.updateLoadingStatus(it)
                    },
                        onStudentCreated = {
                            onNavigationRequested(Screen.Login.route, false)
                        }, onStudentNotCreated = { error ->
                            errorMessage.value = error
                            showSnackbar = true
                        })
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onNavigationRequested(Screen.Login.route, false) }) {
                    Text(" Have Account? Login Instead")
                }


            }
        }
        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }

        if (showSnackbar) {
            CustomSnackbar(
                message = errorMessage.value,
                actionLabel = "",
                onActionClick = {
                    // Handle action click
                    showSnackbar = false
                },
                onDismiss = {
                    // Handle dismiss
                    showSnackbar = false
                }
            )
        }
    }
}
