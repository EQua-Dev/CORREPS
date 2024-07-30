package com.schoolprojects.corrreps.screens.auth

import CustomTextField
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.R
import com.schoolprojects.corrreps.components.CustomSnackbar
import com.schoolprojects.corrreps.components.FlatButton
import com.schoolprojects.corrreps.navigation.Screen
import com.schoolprojects.corrreps.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    onAuthenticated: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val email by remember { authViewModel.email }
    val password by remember { authViewModel.password }

    val errorMessage = remember { mutableStateOf("") }
    val showLoading by remember { mutableStateOf(authViewModel.showLoading) }
    var showSnackbar by remember { mutableStateOf(false) }


    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            FlatButton(
                text = stringResource(id = R.string.login),
                onClick = {
                    authViewModel.login(
                        onLoading = {
                            showLoading.value = it
                        },
                        onAuthenticated = { route ->
                            onNavigationRequested(route, false)
                        },
                        onAuthenticationFailed = { error ->
                            errorMessage.value = error
                        })
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1.0f))
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = {
                        onNavigationRequested(Screen.ForgotPassword.route, false)
                    }) {
                    Text(
                        "Forgot Password",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("New Account? ")
                TextButton(onClick = {
                    onNavigationRequested(Screen.Signup.route, false)
                }) {
                    Text("Create Account")
                }


            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = {
                onNavigationRequested(Screen.FeesPayment.route, false)
                /* Handle pay fees */
            }) {
                Text("Pay Fees")
            }

        }
        if (showLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))

            }
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