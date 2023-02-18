package com.radancy.demo.service

import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Component
class TransactionService(val dbService: DatabaseService) {
    private val zero = BigDecimal(0)
    private val maxDepositLimit = BigDecimal(10000)
    private val maxWithdrawalPercentage = BigDecimal(90)
    private val minAccountValue = BigDecimal(100)

    private val logger = KotlinLogging.logger {}

    fun depositTransaction(username: String, accountID: String, transactionValue: BigDecimal): String {
        val userFinancialAccount = dbService.getAccountBalance(username, accountID)
        if (transactionValue < zero) {
            throw Exception("Negative numbers are not allowed")
        }
        val formattedTransactionValue = transactionValue.stripTrailingZeros()
        if (formattedTransactionValue.scale() > 2) {
            throw Exception("Maximum of two decimal places allowed")
        }
        if (formattedTransactionValue > maxDepositLimit) {
            throw Exception("Deposit exceeds $10,000 maximum")
        }
        if (userFinancialAccount != null) {
            logger.info(username, accountID, userFinancialAccount, "Deposit of $formattedTransactionValue")
            dbService.accountDeposit(username, accountID, userFinancialAccount, formattedTransactionValue)
        }
        return "Deposit of ${formattedTransactionValue.toDouble()} completed"
    }

    fun withdrawTransaction(username: String, accountID: String, transactionValue: BigDecimal): String {
        val userFinancialAccount = dbService.getAccountBalance(username, accountID)
        if (transactionValue < zero) {
            throw Exception("Negative numbers are not allowed")
        }
        val formattedTransactionValue = transactionValue.stripTrailingZeros()
        if (formattedTransactionValue.scale() > 2) {
            throw Exception("Maximum of two decimal places allowed")
        }
        if (formattedTransactionValue > userFinancialAccount) {
            throw Exception("Not sufficient funds for withdrawal")
        }
        if (userFinancialAccount!!.minus(formattedTransactionValue) < minAccountValue) {
            throw Exception("Withdrawal puts account value under $100")
        }
        if (formattedTransactionValue.multiply(minAccountValue).divide(userFinancialAccount) > maxWithdrawalPercentage) {
            throw Exception("Withdrawal exceeds 90% of total balance")
        }
        dbService.accountWithdraw(username, accountID, userFinancialAccount, formattedTransactionValue)
        logger.info(username, accountID, userFinancialAccount, "Withdraw of $formattedTransactionValue at ${
            DateTimeFormatter.ISO_INSTANT.format(
            Instant.now())}")
        return "Withdrawal of ${formattedTransactionValue.toDouble().absoluteValue} completed"
    }
}