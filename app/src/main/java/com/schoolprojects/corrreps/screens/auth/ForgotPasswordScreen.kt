package com.schoolprojects.corrreps.screens.auth

import CustomTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.R
import com.schoolprojects.corrreps.components.FlatButton
import com.schoolprojects.corrreps.ui.Dimension
import com.schoolprojects.corrreps.ui.theme.Typography
import com.schoolprojects.corrreps.viewmodels.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    /*val email by remember { mutableStateOf(authViewModel.email) }
    val password = remember { "" }*/// authViewModel.password }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val showLoading by remember {
        mutableStateOf(authViewModel.showLoading)
    }

    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {

        Column {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

            }

            Text(
                text = stringResource(id = R.string.forgot_password),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.displayMedium,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "Email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))


            FlatButton(
                text = stringResource(id = R.string.login),
                onClick = { /* Handle login */ },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimension.md))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimension.pagePadding),
                contentAlignment = Alignment.Center,
            ) {
                Divider()
                TextButton(onClick = {
                    onNavigationRequested("login", true)
                }) {
                    Text(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = Dimension.pagePadding.div(2)),
                        text = stringResource(id = R.string.remembered_password),
                        style = MaterialTheme.typography.bodyMedium
                            .copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            ),
                    )
                }

            }
        }

        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    }
}
