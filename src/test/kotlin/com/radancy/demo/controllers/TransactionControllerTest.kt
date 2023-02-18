package com.radancy.demo.controllers


import com.radancy.demo.controller.TransactionController
import com.radancy.demo.service.DatabaseService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class TransactionControllerTest {
    private val username = "Sean"
    private val accountID = UUID.randomUUID().toString()
    private val deposit150 = "150"
    private val withdraw100 = "100"
    private val deposit10000 = "10000"
    private val deposit20000 = "120000"
    private val withdraw9500 = "9500"
    private val negativeTransaction = "-100"
    private val transactionWith2Decimals = "211.29"
    private val transactionWith3Decimals = "100.111"
    private val accountValue0 = BigDecimal(0)

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var transactionController: TransactionController

    @Test
    fun shouldDeposit10000() {
        createAccountWithBalance(deposit10000)
        val response = transactionController.depositTransaction(username, accountID, deposit150)
        assertEquals(responseAcceptedWithMessage("Deposit of ${deposit150.toDouble()} completed"), response)
    }

    @Test
    fun shouldDepositWithDecimals() {
        createAccountWithBalance(deposit10000)
        val response = transactionController.depositTransaction(username, accountID, transactionWith2Decimals)
        assertEquals(responseAcceptedWithMessage("Deposit of ${transactionWith2Decimals.toDouble()} completed"), response)
    }

    @Test
    fun shouldWithdraw() {
        createAccountWithBalance(deposit10000)
        val response = transactionController.withdrawTransaction(username, accountID, withdraw100)
        assertEquals(responseAcceptedWithMessage("Withdrawal of ${withdraw100.toDouble()} completed"), response)
    }

    @Test
    fun shouldWithdrawWithDecimals() {
        createAccountWithBalance(deposit10000)
        val response = transactionController.withdrawTransaction(username, accountID, transactionWith2Decimals)
        assertEquals(responseAcceptedWithMessage("Withdrawal of ${transactionWith2Decimals.toDouble()} completed"), response)
    }

    @Test
    fun shouldFailDepositNegativeNumber() {
        createAccountWithBalance(deposit10000)
        assertThrows<Exception>("Negative numbers are not allowed") {
            transactionController.depositTransaction(
                username,
                accountID,
                negativeTransaction
            )
        }
    }

    @Test
    fun shouldFailDepositTooLarge() {
        createAccountWithBalance(deposit10000)
        assertThrows<java.lang.Exception>("Deposit exceeds $10,000 maximum") {
            transactionController.depositTransaction(
                username,
                accountID,
                deposit20000
            )
        }
    }

    @Test
    fun shouldFailDepositThreeDecimalPlaces() {
        createAccountWithBalance(deposit10000)
        assertThrows<java.lang.Exception>("Maximum of two decimal places allowed") {
            transactionController.depositTransaction(
                username,
                accountID,
                transactionWith3Decimals
            )
        }
    }

    @Test
    fun shouldFailWithdrawOver90Percent() {
        createAccountWithBalance(deposit10000)
        assertThrows<java.lang.Exception>("Withdrawal exceeds 90% of total balance") {
            transactionController.withdrawTransaction(
                username,
                accountID,
                withdraw9500
            )
        }
    }

    @Test
    fun shouldFailWithdrawUnder100Balance() {
        createAccountWithBalance(deposit150)
        assertThrows<java.lang.Exception>("Withdrawal puts account value under $100") {
            transactionController.withdrawTransaction(
                username,
                accountID,
                withdraw100
            )
        }
    }

    @Test
    fun shouldFailWithdrawNegativeNumber() {
        createAccountWithBalance(deposit10000)
        assertThrows<java.lang.Exception>("Negative numbers are not allowed") {
            transactionController.withdrawTransaction(
                username,
                accountID,
                negativeTransaction
            )
        }
    }

    @Test
    fun shouldFailWithdrawThreeDecimalPlaces() {
        createAccountWithBalance(deposit10000)
        assertThrows<java.lang.Exception>("Maximum of two decimal places allowed") {
            transactionController.withdrawTransaction(
                username,
                accountID,
                transactionWith3Decimals
            )
        }
    }

    @Test
    fun shouldFailWithdrawInsufficientFunds() {
        createAccountWithBalance(deposit150)
        assertThrows<java.lang.Exception>("Not sufficient funds for withdrawal") {
            transactionController.withdrawTransaction(
                username,
                accountID,
                withdraw9500
            )
        }
    }

    private fun createAccountWithBalance(balance: String) {
        databaseService.createUser(username)
        databaseService.createAccount(username, accountID)
        databaseService.accountDeposit(username, accountID, accountValue0, BigDecimal(balance))
    }

    private fun responseAcceptedWithMessage(message: String) = ResponseEntity<Any>(message, HttpStatus.ACCEPTED)
}