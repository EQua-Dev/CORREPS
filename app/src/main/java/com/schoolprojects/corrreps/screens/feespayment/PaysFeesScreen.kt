package com.schoolprojects.corrreps.screens.feespayment

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.corrreps.components.DropdownMenuBox
import com.schoolprojects.corrreps.viewmodels.PayFeesViewModel
import org.devstrike.persacg.utils.generatePaymentRef

@Composable
fun PaysFees(modifier: Modifier = Modifier, payFeesViewModel: PayFeesViewModel = hiltViewModel()) {

    val context = LocalContext.current

    var studentName by remember { mutableStateOf("") }
    var studentRegNumber by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("Select Level") }
    var selectedSemester by remember { mutableStateOf("Select Semester") }
    var paymentAmount by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCVC by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }
    var paymentRef by remember { mutableStateOf("") }

    // Sample fee structure, map each semester to its fee
    val feesMap = mapOf(
        "100 Level - 1st Semester" to "208500",
        "100 Level - 2nd Semester" to "113500",
        "200 Level - 1st Semester" to "203500",
        "200 Level - 2nd Semester" to "116500",
        "300 Level - 1st Semester" to "203500",
        "300 Level - 2nd Semester" to "116500",
        "400 Level - 1st Semester" to "203500",
        "400 Level - 2nd Semester" to "116500"
    )

    // Levels and semesters
    val levels = listOf("100", "200", "300", "400")
    val semesters = listOf("First", "Second")

    // Logic to handle level and semester selection and update payment amount
    fun updatePaymentAmount() {
        val key = "$selectedLevel - $selectedSemester"
        paymentAmount = feesMap[key] ?: "0"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Fees Payment", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        // Student Name Input
        OutlinedTextField(
            value = studentName,
            onValueChange = { studentName = it },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Registration Number Input
        OutlinedTextField(
            value = studentRegNumber,
            onValueChange = { studentRegNumber = it },
            label = { Text("Registration Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Level Dropdown
        DropdownMenuBox(selectedLevel, levels, onSelect = {
            selectedLevel = it
            updatePaymentAmount() // Update amount based on selection
        })

        Spacer(modifier = Modifier.height(8.dp))

        // Semester Dropdown
        DropdownMenuBox(selectedSemester, semesters, onSelect = {
            selectedSemester = it
            updatePaymentAmount() // Update amount based on selection
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Amount
        Text(text = "Amount: ₦$paymentAmount", fontSize = 18.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        // Card Number Input
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card Expiry Input
        OutlinedTextField(
            value = cardExpiry,
            onValueChange = { cardExpiry = it },
            label = { Text("Expiry Date (MM/YY)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card CVC Input
        OutlinedTextField(
            value = cardCVC,
            onValueChange = { cardCVC = it },
            label = { Text("CVC") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pay Button
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Pay")
        }

        // Payment PIN Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Enter PIN") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = pin,
                            onValueChange = { pin = it },
                            label = { Text("PIN") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Validate PIN and Process Payment
                            if (pin.length == 4) { // Assume valid if PIN is 4 digits
                                // Generate payment reference
                                paymentRef = generatePaymentRef()

                                // Close dialog

                                payFeesViewModel.savePaidFeeInfo(
                                    semester = selectedSemester,
                                    level = selectedLevel,
                                    studentName = studentName,
                                    studentRegNo = studentRegNumber,
                                    paymentRef = paymentRef,
                                    onFeesSaved = {
                                        showDialog = false
                                    },
                                    onFeesNotSaved = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    })
                                // Display payment confirmation and reference
                                // Here, you could call a function or trigger a UI update
                            } else {
                                // Show error message for invalid PIN
                                // e.g., Snackbar, Toast, etc.
                            }
                        }
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Payment Reference with Copy Option
        if (paymentRef.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Payment Reference: $paymentRef",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Copy",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable {
                        // Copy the reference to clipboard
                        // Example: Copy to clipboard function
//                        copyToClipboard(paymentRef)
                    }
                )
            }
        }
    }
}