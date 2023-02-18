package com.radancy.demo.service

import org.springframework.stereotype.Component

@Component
class UserService(val dbService: DatabaseService) {
    fun createUser(username: String): String {
        dbService.createUser(username)
        return username
    }
}