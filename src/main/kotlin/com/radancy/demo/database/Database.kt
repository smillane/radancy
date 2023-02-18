package com.radancy.demo.database

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class Database {
    val users: MutableMap<String, MutableMap<String, BigDecimal>> = mutableMapOf()
}