package com.yuvrajsinghgmx.shopsmart.profilefeatures

import java.time.LocalDateTime

data class Transaction(
    val id: String,
    val amount: Double,
    val date: LocalDateTime,
    val description: String,
    val type: String = "PURCHASE",     // "PURCHASE", "REFUND"
    val status: String = "COMPLETED",  // "COMPLETED", "PENDING", "FAILED"
    val paymentMethod: String
)