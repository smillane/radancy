package com.radancy.demo.controllers

import com.radancy.demo.controller.AccountController
import com.radancy.demo.service.DatabaseService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest() {
    private val username = "Sean"
    private val accountID = UUID.randomUUID().toString()
    private val accountValue0 = BigDecimal(0)
    private val accountValue513point23 = BigDecimal(513.23)
    private val accountValue10000 = BigDecimal(10000)
    private val emptyMap = mutableMapOf<String, BigDecimal>()

    @Autowired
    private lateinit var accountController: AccountController

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Test
    fun shouldReturnEmptyMap() {
        databaseService.createUser(username)
        val response = accountController.getAccounts(username)
        assertEquals(ResponseEntity<Any>(emptyMap, HttpStatus.OK), response)
    }

    @Test
    fun shouldReturnEmptyMapAfterDeletingAccount() {
        createAccountWithBalance()
        databaseService.deleteAccount(username, accountID)
        val response = accountController.getAccounts(username)
        assertEquals(ResponseEntity<Any>(emptyMap, HttpStatus.OK), response)
    }

    @Test
    fun shouldReturnOneAccountWithNoBalance() {
        databaseService.createUser(username)
        databaseService.createAccount(username, accountID)
        val response = accountController.getAccounts(username)
        assertEquals(responseWithAccountBalance(accountID, accountValue0), response)
    }

    @Test
    fun shouldReturnOneAccountWith10000Balance() {
        databaseService.createUser(username)
        databaseService.createAccount(username, accountID)
        databaseService.accountDeposit(username, accountID, accountValue0, accountValue10000)
        val response = accountController.getAccounts(username)
        assertEquals(responseWithAccountBalance(accountID, accountValue10000), response)
    }

    @Test
    fun shouldReturnOneAccountWithDecimalsInBalance() {
        databaseService.createUser(username)
        databaseService.createAccount(username, accountID)
        databaseService.accountDeposit(username, accountID, accountValue0, accountValue513point23)
        val response = accountController.getAccounts(username)
        assertEquals(responseWithAccountBalance(accountID, accountValue513point23), response)
    }

    private fun createAccountWithBalance() {
        databaseService.createUser(username)
        databaseService.createAccount(username, accountID)
        databaseService.accountDeposit(username, accountID, accountValue0, accountValue10000)
    }

    private fun responseWithAccountBalance(accountID: String, balance: BigDecimal) = ResponseEntity<Any>(mapOf(accountID to balance), HttpStatus.OK)

}