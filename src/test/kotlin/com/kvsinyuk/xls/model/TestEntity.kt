package com.kvsinyuk.xls.model

import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class TestEntity(
    val dummyNumber: BigInteger = BigInteger.valueOf((0..10L).random()),
    val dummyString: String = UUID.randomUUID().toString(),
    val dummyInstant: Instant = Instant.now(),
    val dummyLocalDate: LocalDate = LocalDate.now(),
)
