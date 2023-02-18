package com.radancy.demo.service

import com.radancy.demo.database.Database
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DatabaseService(val db: Database) {
    fun createUser(username: String) {
        db.users += username to mutableMapOf()
    }

    fun getUserAccounts(username: String): MutableMap<String, BigDecimal>? {
        return db.users[username]
    }

    fun createAccount(username: String, accountID: String) {
        db.users[username]!! += mutableMapOf(accountID to BigDecimal(0))
    }

    fun deleteAccount(username: String, accountID: String) {
        db.users[username]?.remove(accountID)
    }

    fun getAccountBalance(username: String, accountID: String): BigDecimal? {
        return db.users[username]?.get(accountID)
    }

    fun accountDeposit(username: String, accountID: String, userFinancialAccount: BigDecimal, formattedTransactionValue: BigDecimal) {
        db.users[username]?.set(accountID, userFinancialAccount + formattedTransactionValue)
    }

    fun accountWithdraw(username: String, accountID: String, userFinancialAccount: BigDecimal, formattedTransactionValue: BigDecimal) {
        db.users[username]?.set(accountID, userFinancialAccount - formattedTransactionValue)
    }

    fun doesUserExist(username: String): Boolean {
        if (!db.users.contains(username)) {
            return false
        }
        return true
    }

    fun doesAccountExist(username: String, accountID: String): Boolean {
        if (db.users[username]?.contains(accountID) == false) {
            return false
        }
        return true
    }
}