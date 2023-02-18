package com.radancy.demo.service

import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class AccountService(val dbService: DatabaseService) {
    private val logger = KotlinLogging.logger {}

    fun getAccounts(username: String): MutableMap<String, BigDecimal>? {
        return dbService.getUserAccounts(username)
    }

    //using UUID to generate financial account id for simplicity
    fun createAccount(username: String): String {
        //in a more realistic scenario, would also have an account name that you could change, with the UUID being static
        val accountID: String = UUID.randomUUID().toString()
        dbService.createAccount(username, accountID)
        return accountID
    }

    fun deleteAccount(username: String, accountID: String): String {
        dbService.deleteAccount(username, accountID)
        logger.info(username, accountID, "Account Deleted")
        return "$accountID has been deleted"
    }
}
