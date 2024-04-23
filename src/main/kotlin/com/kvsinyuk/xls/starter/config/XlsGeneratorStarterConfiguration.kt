package com.kvsinyuk.xls.starter.config

import com.kvsinyuk.xls.starter.service.XlsGeneratorService
import com.kvsinyuk.xls.starter.service.XlsGeneratorServiceImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class XlsGeneratorStarterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun xlsGeneratorService(): XlsGeneratorService {
        return XlsGeneratorServiceImpl()
    }
}
