package com.schoolprojects.corrreps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.schoolprojects.corrreps.ui.theme.Typography
import com.schoolprojects.corrreps.viewmodels.AuthViewModel


@Composable
fun PasswordStrengthIndicator(passwordStrength: AuthViewModel.PasswordStrength) {
    val color = when (passwordStrength) {
        AuthViewModel.PasswordStrength.TOO_SHORT -> Color.Red
        AuthViewModel.PasswordStrength.WEAK -> Color.Red
        AuthViewModel.PasswordStrength.MEDIUM -> Color.Yellow
        AuthViewModel.PasswordStrength.STRONG -> Color.Green
    }

    val strength = when (passwordStrength) {
        AuthViewModel.PasswordStrength.TOO_SHORT -> 0
        AuthViewModel.PasswordStrength.WEAK -> 1
        AuthViewModel.PasswordStrength.MEDIUM -> 2
        AuthViewModel.PasswordStrength.STRONG -> 3
    }
    val message = when (passwordStrength) {
        AuthViewModel.PasswordStrength.TOO_SHORT -> "Password should be at least 8 characters 🤦🏾"
        AuthViewModel.PasswordStrength.WEAK -> "Password should have at least 1 Uppercase and 1 Lowercase character 🥴"
        AuthViewModel.PasswordStrength.MEDIUM -> "Password should have at least 1 Digit and 1 Special Character character 🥲"
        AuthViewModel.PasswordStrength.STRONG -> "Password is strong! 😁"
    }


    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .weight(1f)
                        .padding(2.dp)
                        .background(
                            color = if (index < strength) color else Color.Gray,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
        Text(
            text = message,
            style = Typography.labelLarge,
            modifier = Modifier.padding(4.dp)
        )
    }
}


@Composable
fun CustomSnackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarHostState) {
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration
        )
        when (result) {
            SnackbarResult.ActionPerformed -> onActionClick()
            SnackbarResult.Dismissed -> onDismiss()
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                snackbarData = data,
                actionColor = if (actionLabel != null) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        }
    )
}
