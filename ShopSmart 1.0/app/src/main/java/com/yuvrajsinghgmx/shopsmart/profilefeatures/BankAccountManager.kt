package com.yuvrajsinghgmx.shopsmart.profilefeatures

import androidx.compose.runtime.mutableStateListOf

data class BankAccountInfo(
    val accountNumber: String,
    val accountType: String,
    val bankName: String,
    val holderName: String,
    val ifscCode: String,
    val branchName: String,
    val isDefault: Boolean = false
)

object BankAccountManager {
    private val _savedAccounts = mutableStateListOf<BankAccountInfo>(
        // Add default account
        BankAccountInfo(
            accountNumber = "4321",
            accountType = "Savings",
            bankName = "State Bank",
            holderName = "John Doe",
            ifscCode = "SBIN0001234",
            branchName = "Main Branch",
            isDefault = true
        )
    )

    val savedAccounts: List<BankAccountInfo> get() = _savedAccounts.toList()

    fun addAccount(account: BankAccountInfo) {
        if (account.isDefault) {
            // Remove default status from other accounts
            _savedAccounts.replaceAll { it.copy(isDefault = false) }
        }
        _savedAccounts.add(account)
    }

    fun removeAccount(accountNumber: String) {
        _savedAccounts.removeAll { it.accountNumber == accountNumber }
    }

    fun getAccount(accountNumber: String): BankAccountInfo? {
        return _savedAccounts.find { it.accountNumber == accountNumber }
    }

    fun setDefaultAccount(accountNumber: String) {
        _savedAccounts.replaceAll { account ->
            account.copy(isDefault = account.accountNumber == accountNumber)
        }
    }
}