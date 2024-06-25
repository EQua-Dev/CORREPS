package com.schoolprojects.corrreps.auth

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.schoolprojects.corrreps.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    onSignUpClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onAuthenticated: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

   /* val context = LocalContext.current
    val email by remember { authViewModel.email }
    val password by remember { authViewModel.password }

    val showLoading by remember { mutableStateOf(authViewModel.showLoading) }
*/

    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {

        Text(text = "Ndewonu!")
        /*Column {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier
                        .padding(Dimension.pagePadding.div(2))
                        .size(Dimension.mdIcon.times(1f)),
                    painter = painterResource(id = R.drawable.student),
                    contentDescription = "Student Icon"
                )
            }

            Text(
                text = stringResource(id = R.string.login),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.displayMedium,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(Dimension.pagePadding))
            CustomInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large,
                    )
                    .fillMaxWidth(),
                value = email ?: "",
                onValueChange = {
                    authViewModel.updateEmail(value = it.ifBlank { "" })
                },
                enabled = !showLoading.value,
                placeholder = stringResource(id = R.string.email),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                padding = PaddingValues(
                    horizontal = Dimension.pagePadding,
                    vertical = Dimension.pagePadding.times(0.7f),
                ),
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onBackground,
                imeAction = ImeAction.Next,
                shape = MaterialTheme.shapes.large,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = Dimension.pagePadding.div(2))
                            .size(Dimension.mdIcon.times(0.7f)),
                        painter = painterResource(id = R.drawable.ic_profile_empty),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    )
                },
                onFocusChange = { },
                onKeyboardActionClicked = { },
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(Dimension.pagePadding))
            CustomInputField(
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevation,
                        shape = MaterialTheme.shapes.large,
                    )
                    .fillMaxWidth(),
                value = password ?: "",
                onValueChange = {
                    authViewModel.updatePassword(value = it.ifBlank { "" })
                },
                enabled = !showLoading.value,
                placeholder = "Password",
                visualTransformation = PasswordVisualTransformation(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                padding = PaddingValues(
                    horizontal = Dimension.pagePadding,
                    vertical = Dimension.pagePadding.times(0.7f),
                ),
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onBackground,
                imeAction = ImeAction.Done,
                shape = MaterialTheme.shapes.large,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = Dimension.pagePadding.div(2))
                            .size(Dimension.mdIcon.times(0.7f)),
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    )
                },
                onFocusChange = { },
                onKeyboardActionClicked = { },
            )
            *//** The login button *//*
            *//** The login button *//*
            Spacer(modifier = Modifier.height(Dimension.pagePadding))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onForgotPasswordClicked() }) {
                    Text(text = "Forgot Password", style = Typography.bodyLarge)
                }
            }
            Spacer(modifier = Modifier.height(Dimension.pagePadding))

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                *//*.shadow(
                    //elevation = if (uiState !is UiState.Loading) Dimension.elevation else Dimension.zero,
                    shape = MaterialTheme.shapes.large,
                )*//*,
                shape = MaterialTheme.shapes.large,
                padding = PaddingValues(Dimension.pagePadding.div(2)),
                buttonColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(id = R.string.login),
                enabled = !showLoading.value,
                textStyle = MaterialTheme.typography.bodyMedium,
                onButtonClicked = {
                    authViewModel.login(
                        email = email ?: "",
                        password = password ?: "",
                        onLoading = { status ->
                            authViewModel.updateLoadingStatus(status)
                        },
                        onAuthenticated = { userType ->
                            // When user is authenticated, go home or back
                            onAuthenticated(userType)
                        },
                        onAuthenticationFailed = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    )
                }
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
                    onSignUpClicked()
                }) {
                    Text(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = Dimension.pagePadding.div(2)),
                        text = stringResource(id = R.string.sign_up_instead),
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingDialog()
            }
        }*/
    }
}