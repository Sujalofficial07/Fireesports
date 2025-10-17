package com.fireesports.data.repository

import com.fireesports.data.model.Transaction
import com.fireesports.data.model.TransactionCategory
import com.fireesports.data.model.TransactionStatus
import com.fireesports.data.model.TransactionType
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
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
            .select(columns = Columns.list("wallet_balance")) {  // Changed from walletBalance
                filter { eq("id", userId) }
            }
            .decodeList<Map<String, Any>>()
        
        val balance = result.firstOrNull()?.get("wallet_balance")?.toString()?.toDoubleOrNull() ?: 0.0
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
        
        // Update user balance
        val currentBalance = getBalance(userId).getOrNull() ?: 0.0
        val updates = mapOf("wallet_balance" to (currentBalance + amount))  // Changed
        
        supabase.from("users").update(updates) {
            filter { eq("id", userId) }
        }
        
        Result.success(transaction)
      } catch (e: Exception) {
          Result.failure(e)
    }
}

    suspend fun deductFunds(
       userId: String, 
       amount: Double, 
       category: TransactionCategory, 
       description: String, 
       referenceId: String? = null
    ): Result<Transaction> {
      return try {
        val currentBalance = getBalance(userId).getOrNull() ?: 0.0
        if (currentBalance < amount) {
            return Result.failure(Exception("Insufficient balance"))
        }
        
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
        
        // Update user balance
        val updates = mapOf("wallet_balance" to (currentBalance - amount))  // Changed
        
        supabase.from("users").update(updates) {
            filter { eq("id", userId) }
        }
        
        Result.success(transaction)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
