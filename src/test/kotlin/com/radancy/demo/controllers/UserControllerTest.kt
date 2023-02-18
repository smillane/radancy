package com.radancy.demo.controllers

import com.radancy.demo.controller.UserController
import com.radancy.demo.service.DatabaseService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.lang.Exception

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    private val username1 = "Sean"
    private val username2 = "John"
    private val username3 = "Bill"
    private val username4 = "Bob"

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var databaseService: DatabaseService

    @Test
    fun shouldReturnError() {
        databaseService.createUser(username1)
        assertThrows<Exception>("This user already exists") { userController.createUser(username1) }
    }

    @Test
    fun shouldCreateUsers() {
        val response1 = userController.createUser(username3)
        val response2 = userController.createUser(username4)
        assertEquals(ResponseEntity<Any>(username3, HttpStatus.CREATED), response1)
        assertEquals(ResponseEntity<Any>(username4, HttpStatus.CREATED), response2)
    }

    @Test
    fun shouldCreateUser() {
        val response1 = userController.createUser(username2)
        assertEquals(ResponseEntity<Any>(username2, HttpStatus.CREATED), response1)
    }
}