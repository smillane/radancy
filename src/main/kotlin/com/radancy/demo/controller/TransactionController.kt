package com.radancy.demo.controller

import com.radancy.demo.service.DatabaseService
import com.radancy.demo.service.TransactionService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/transactions/{accountID}/{transactionValue}")
class TransactionController(val transactionView: TransactionService, val dbService: DatabaseService) {
    private val logger = KotlinLogging.logger {}

    @PutMapping("/deposit")
    fun depositTransaction(
        @RequestHeader("username") username: String,
        @PathVariable("accountID") accountID: String,
        @PathVariable("transactionValue") transactionValue: String,
    ): ResponseEntity<Any> {
        if (!dbService.doesUserExist(username)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        }
        if (!dbService.doesAccountExist(username, accountID)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This account does not exist")
        }
        try {
            return ResponseEntity<Any>(transactionView.depositTransaction(username, accountID, transactionValue.toBigDecimal()), HttpStatus.ACCEPTED)
        } catch (ex: IllegalArgumentException) {
            logger.error(username, accountID, ex.stackTrace)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message)
        }
    }

    @PutMapping("/withdraw")
    fun withdrawTransaction(
        @RequestHeader("username") username: String,
        @PathVariable("accountID") accountID: String,
        @PathVariable("transactionValue") transactionValue: String,
    ): ResponseEntity<Any> {
        if (!dbService.doesUserExist(username)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        }
        if (!dbService.doesAccountExist(username, accountID)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This account does not exist")
        }
        try {
            return ResponseEntity<Any>(transactionView.withdrawTransaction(username, accountID, transactionValue.toBigDecimal()), HttpStatus.ACCEPTED)
        } catch (ex: IllegalArgumentException) {
            logger.error(username, accountID, ex.stackTrace)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message)
        }
    }
}