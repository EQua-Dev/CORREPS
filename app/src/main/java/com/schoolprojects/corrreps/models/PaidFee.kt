package com.schoolprojects.corrreps.models

data class PaidFee(
    val paidFeeId: String = "",
    val semesterPaid: String = "",
    val levelPaid: String = "",
    val studentPaid: String = "",
    val studentRegNoPaid: String = "",
    val datePaid: String = "",
    val paymentRef: String = "",
)
