package com.kvsinyuk.xls.model

import java.math.BigInteger
import java.util.UUID

data class TestEntity(
    val dummyNumber: BigInteger = BigInteger.valueOf((0..10L).random()),
    val dummyString: String = UUID.randomUUID().toString(),
)