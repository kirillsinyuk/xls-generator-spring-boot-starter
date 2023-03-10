package com.kvsinyuk.xls.starter.model

import com.kvsinyuk.xls.starter.model.context.Context
import com.kvsinyuk.xls.starter.model.context.ContextImpl
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.Closeable
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class XlsBuilder<T>(
    private val file: File,
    private var processors: List<CellProcessor<T>>,
    sheetName: String? = null
) : Closeable {

    private val context: Context
    private val workbook: SXSSFWorkbook
    private var sheet: SXSSFSheet

    private var rowNumber: AtomicInteger = AtomicInteger(1)

    init {
        workbook = createWorkbook()
        sheet = createNewSheet(sheetName)
        context = ContextImpl(workbook)
        dumpSheetHead()
    }

    fun dumpRow(entity: T) {
        val row = sheet.createRow(rowNumber.getAndIncrement())

        for (processorNum in processors.indices) {
            val cell = row.createCell(processorNum)
            processors[processorNum].accept(entity, cell, context)
        }
    }

    fun dumpAllRows(entities: Iterable<T>): XlsBuilder<T> {
        entities.forEach { dumpRow(it) }
        return this
    }

    fun skipRows(num: Int): XlsBuilder<T> {
        rowNumber.addAndGet(num)
        return this
    }

    fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilder<T> {
        return this
            .apply { this.processors = processors }
            .also { context.clearContext() }
    }

    fun createNewSheet(processors: List<CellProcessor<T>>, sheetName: String? = null): XlsBuilder<T> {
        rowNumber = AtomicInteger(1)
        this.processors = processors
        context.clearContext()
        sheet = createNewSheet(sheetName)
        dumpSheetHead()
        return this
    }

    fun build(): File {
        file.outputStream()
            .use { workbook.write(it) }
        return file
    }

    private fun createWorkbook(rowAccessWindowSize: Int = 50) =
        SXSSFWorkbook(rowAccessWindowSize)

    private fun dumpSheetHead() {
        val font = workbook.createFont()
            .apply { bold = true }
        val style = workbook.createCellStyle()
            .also { it.setFont(font) }

        val row = sheet.createRow(0)
        for (processorNum in processors.indices) {
            row.createCell(processorNum)
                .also { it.setCellValue(processors[processorNum].name()) }
                .apply { cellStyle = style }
        }
    }

    private fun createNewSheet(sheetName: String?): SXSSFSheet {
        val sheet = sheetName
            ?.let { workbook.createSheet(sheetName) }
            ?: workbook.createSheet()
        sheet.trackAllColumnsForAutoSizing()
        return sheet
    }

    override fun close() {
        workbook.dispose()
        workbook.close()
    }
}