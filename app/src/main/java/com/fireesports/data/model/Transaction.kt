package com.fireesports.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    val amount: Double = 0.0,
    val type: TransactionType = TransactionType.CREDIT,
    val category: TransactionCategory = TransactionCategory.OTHER,
    val description: String = "",
    @SerialName("reference_id")
    val referenceId: String? = null,
    val status: TransactionStatus = TransactionStatus.COMPLETED,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
enum class TransactionType {
    CREDIT, DEBIT
}

@Serializable
enum class TransactionCategory {
    TOURNAMENT_ENTRY, PRIZE_WON, REFUND, BONUS, WITHDRAWAL, OTHER
}

@Serializable
enum class TransactionStatus {
    PENDING, COMPLETED, FAILED, CANCELLED
}
