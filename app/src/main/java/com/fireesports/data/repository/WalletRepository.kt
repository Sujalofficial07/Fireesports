package com.fireesports.data.repository

import com.fireesports.data.model.Transaction
import com.fireesports.data.model.TransactionCategory
import com.fireesports.data.model.TransactionStatus
import com.fireesports.data.model.TransactionType
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepository @Inject constructor(
    private val supabaseProvider: SupabaseClientProvider
) {
    private val supabase = supabaseProvider.client

    suspend fun getBalance(userId: String): Result<Double> {
        return try {
            val result = supabase.from("users")
                .select(columns = Columns.list("walletBalance")) {
                    filter { eq("id", userId) }
                }.decodeList<Map<String, Double>>()
            val balance = result.firstOrNull()?.get("walletBalance") ?: 0.0
            Result.success(balance)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFunds(userId: String, amount: Double, description: String): Result<Transaction> {
        return try {
            val transaction = Transaction(
                userId = userId,
                amount = amount,
                type = TransactionType.CREDIT,
                category = TransactionCategory.BONUS,
                description = description,
                status = TransactionStatus.COMPLETED
            )
            
            supabase.from("wallet_transactions").insert(transaction)
            
            // Update user balance manually
            val currentBalance = getBalance(userId).getOrNull() ?: 0.0
            supabase.from("users").update({
                set("walletBalance", currentBalance + amount)
            }) {
                filter { eq("id", userId) }
            }
            
            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deductFunds(userId: String, amount: Double, category: TransactionCategory, description: String, referenceId: String? = null): Result<Transaction> {
        return try {
            val transaction = Transaction(
                userId = userId,
                amount = amount,
                type = TransactionType.DEBIT,
                category = category,
                description = description,
                referenceId = referenceId,
                status = TransactionStatus.COMPLETED
            )
            
            supabase.from("wallet_transactions").insert(transaction)
            
            // Update user balance manually
            val currentBalance = getBalance(userId).getOrNull() ?: 0.0
            supabase.from("users").update({
                set("walletBalance", currentBalance - amount)
            }) {
                filter { eq("id", userId) }
            }
            
            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTransactionHistory(userId: String): Result<List<Transaction>> {
        return try {
            val transactions = supabase.from("wallet_transactions")
                .select() {
                    filter { eq("userId", userId) }
                    order(column = "timestamp", ascending = false)
                }.decodeList<Transaction>()
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
