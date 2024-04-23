package com.kvsinyuk.xls

import com.kvsinyuk.xls.config.TestEnvConfig
import com.kvsinyuk.xls.model.TestEntity
import com.kvsinyuk.xls.starter.config.XlsGeneratorStarterConfiguration
import com.kvsinyuk.xls.starter.model.CellProcessor
import com.kvsinyuk.xls.starter.model.TableType
import com.kvsinyuk.xls.starter.model.processors.InstantCellProcessor
import com.kvsinyuk.xls.starter.model.processors.LocalDateCellProcessor
import com.kvsinyuk.xls.starter.model.processors.NumberCellProcessor
import com.kvsinyuk.xls.starter.model.processors.StringCellProcessor
import com.kvsinyuk.xls.starter.service.XlsGeneratorService
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest(classes = [TestEnvConfig::class, XlsGeneratorStarterConfiguration::class])
class XlsGeneratorSpringBootStarterTests {

    @Autowired
    lateinit var xlsGeneratorService: XlsGeneratorService

    @Test
    fun should_generate_ROW_BASED_xsl_file() {
        // given
        val countOfEntities = 10
        val processors = listOf<CellProcessor<TestEntity>>(
            NumberCellProcessor("NumberName", extractor = TestEntity::dummyNumber),
            StringCellProcessor("StringName", extractor = TestEntity::dummyString),
            InstantCellProcessor("InstantName", extractor = TestEntity::dummyInstant),
        )
        val xlsBuilder = xlsGeneratorService.newXlsBuilder(File.createTempFile("File", ".xls"), processors)
        val testData = (1..countOfEntities)
            .map { TestEntity() }

        // when
        val result = xlsBuilder.use {
            it.dumpAll(testData)
                .build()
        }

        // then
        XSSFWorkbook(result.inputStream())
            .use { assertEquals(countOfEntities, it.getSheetAt(0).lastRowNum) }
    }

    @Test
    fun should_generate_COLUMN_BASED_xsl_file() {
        // given
        val countOfEntities = 10
        val processors = listOf<CellProcessor<TestEntity>>(
            NumberCellProcessor("NumberName", extractor = TestEntity::dummyNumber),
            StringCellProcessor("StringName", extractor = TestEntity::dummyString),
            InstantCellProcessor("InstantName", extractor = TestEntity::dummyInstant),
            LocalDateCellProcessor("LocalDateName", extractor = TestEntity::dummyLocalDate),
        )
        val xlsBuilder = xlsGeneratorService.newXlsBuilder(
            File.createTempFile("File", ".xls"),
            processors,
            type = TableType.COLUMN_BASED,
        )
        val testData = (1..countOfEntities)
            .map { TestEntity() }

        // when
        val result = xlsBuilder.use {
            it.dumpAll(testData)
                .build()
        }

        // then
        XSSFWorkbook(result.inputStream())
            .use {
                assertEquals(processors.size, it.getSheetAt(0).lastRowNum + 1)
                assertEquals(countOfEntities + 1, it.getSheetAt(0).getRow(0).lastCellNum.toInt())
            }
    }
}
