package com.jsloane.littleone.domain.model

import java.time.LocalDate

data class Child(
    val id: String = "invalid",
    val name: String = "Child",
    val birthday: LocalDate = LocalDate.MIN
)
