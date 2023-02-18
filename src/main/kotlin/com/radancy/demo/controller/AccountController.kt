package com.radancy.demo.controller

import com.radancy.demo.service.AccountService
import com.radancy.demo.service.DatabaseService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/account")
class AccountController(val accountView: AccountService, val dbService: DatabaseService) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/")
    fun getAccounts(@RequestHeader username: String): ResponseEntity<Any> {
        if (!dbService.doesUserExist(username)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        }
        try {
            return ResponseEntity<Any>(accountView.getAccounts(username), HttpStatus.OK)
        } catch (ex: IllegalArgumentException) {
            logger.error(username, ex.stackTrace)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage)
        }

    }

    @PostMapping("/")
    fun createAccount(@RequestHeader username: String): ResponseEntity<Any> {
        if (!dbService.doesUserExist(username)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        }
        try {
            return ResponseEntity<Any>(accountView.createAccount(username), HttpStatus.CREATED)
        } catch (ex: IllegalArgumentException) {
            logger.error(username, ex.stackTrace)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage)
        }
    }

    @DeleteMapping("/{accountID}")
    fun deleteAccount(
        @RequestHeader("username") username: String,
        @PathVariable("accountID") accountID: String,
    ): ResponseEntity<Any> {
        if (!dbService.doesUserExist(username)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist")
        }
        if (!dbService.doesAccountExist(username, accountID)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This account does not exist")
        }
        try {
            return ResponseEntity<Any>(accountView.deleteAccount(username, accountID), HttpStatus.ACCEPTED)
        } catch (ex: IllegalArgumentException) {
            logger.error(username, accountID, ex.stackTrace)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage)
        }
    }
}