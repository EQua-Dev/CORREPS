package com.schoolprojects.corrreps.viewmodels

import androidx.lifecycle.ViewModel
import com.schoolprojects.corrreps.models.PaidFee
import com.schoolprojects.corrreps.utils.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PayFeesViewModel @Inject constructor(): ViewModel() {

    // Function to collect fee payment information and save it in Firestore
    fun savePaidFeeInfo(
        semester: String,
        level: String,
        studentName: String,
        studentRegNo: String,
        paymentRef: String,
        onFeesSaved: () -> Unit,
        onFeesNotSaved: (String) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        // Generating a unique ID for the paid fee
        val paidFeeId = Common.feePaymentCollection.document().id

        // Getting the current date and formatting it
        val datePaid = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Creating a PaidFee object
        val paidFee = PaidFee(
            paidFeeId = paidFeeId,
            semesterPaid = semester,
            levelPaid = level,
            studentPaid = studentName,
            studentRegNoPaid = studentRegNo,
            datePaid = datePaid,
            paymentRef = paymentRef
        )

        // Using Coroutine to handle Firestore operation

        // Saving the paid fee data to Firestore
        Common.feePaymentCollection
            .document(paidFeeId)
            .set(paidFee)
            .addOnSuccessListener {
                // Handle success
                println("Fee payment saved successfully.")
                onFeesSaved()
            }
            .addOnFailureListener { e ->
                // Handle failure
                println("Error saving fee payment: ${e.message}")
                onFeesNotSaved(e.message.toString())
            }

    }


}