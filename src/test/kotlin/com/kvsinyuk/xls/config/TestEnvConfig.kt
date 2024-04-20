package com.kvsinyuk.xls.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan

@TestConfiguration
@ComponentScan(lazyInit = true)
class TestEnvConfig
