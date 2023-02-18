package com.radancy.demo.controller

import com.radancy.demo.service.DatabaseService
import com.radancy.demo.service.UserService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/user")
class UserController(val userView: UserService, val dbService: DatabaseService) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/")
    fun createUser(@RequestBody username: String): ResponseEntity<Any> {
        if (dbService.doesUserExist(username)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "This user already exists")
        }
        try {
            return ResponseEntity<Any>(userView.createUser(username), HttpStatus.CREATED)
        } catch (ex: IllegalArgumentException) {
            logger.error(ex.message, ex.stackTrace)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message)
        }
    }
}