package com.kvsinyuk.xls.starter.model

import com.kvsinyuk.xls.starter.model.context.Context
import com.kvsinyuk.xls.starter.model.context.ContextImpl
import com.kvsinyuk.xls.starter.model.TableType.ROW_BASED
import com.kvsinyuk.xls.starter.model.TableType.COLUMN_BASED
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.Closeable
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class XlsBuilder<T>(
    private val file: File,
    private var processors: List<CellProcessor<T>>,
    private var tableType: TableType,
    sheetName: String? = null
) : Closeable {

    private val context: Context
    private val workbook: SXSSFWorkbook
    private var sheet: SXSSFSheet

    private var rowNumber: AtomicInteger = AtomicInteger(1)
    private var columnNumber: AtomicInteger = AtomicInteger(1)

    init {
        workbook = createWorkbook()
        sheet = createNewSheet(sheetName)
        context = ContextImpl(workbook)
        dumpSheetHead()
    }

    fun dump(entity: T) {
        when (tableType) {
            ROW_BASED -> dumpRow(entity)
            COLUMN_BASED -> dumpColumn(entity)
        }
    }

    fun dumpAll(entities: Iterable<T>): XlsBuilder<T> {
        entities.forEach { dump(it) }
        return this
    }

    fun skipRows(num: Int): XlsBuilder<T> {
        rowNumber.addAndGet(num)
        return this
    }

    fun skipColumns(num: Int): XlsBuilder<T> {
        columnNumber.addAndGet(num)
        return this
    }

    fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilder<T> {
        return this
            .apply { this.processors = processors }
            .also { context.clearContext() }
    }

    fun createNewSheet(processors: List<CellProcessor<T>>, sheetName: String? = null): XlsBuilder<T> {
        rowNumber = AtomicInteger(1)
        columnNumber = AtomicInteger(1)
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

        when (tableType) {
            ROW_BASED -> dumpRowSheetHead(style)
            COLUMN_BASED -> dumpColumnSheetHead(style)
        }
    }

    private fun dumpRowSheetHead(style: CellStyle) {
        val row = sheet.createRow(0)
        for (processorNum in processors.indices) {
            row.createCell(processorNum)
                .also { it.setCellValue(processors[processorNum].name()) }
                .apply { cellStyle = style }
        }
    }

    private fun dumpColumnSheetHead(style: CellStyle) {
        for (processorNum in processors.indices) {
            val row = sheet.createRow(processorNum)
            row.createCell(0)
                .also { it.setCellValue(processors[processorNum].name()) }
                .apply { cellStyle = style }
        }
    }

    private fun dumpRow(entity: T) {
        val row = sheet.createRow(rowNumber.getAndIncrement())

        for (processorNum in processors.indices) {
            val cell = row.createCell(processorNum)
            processors[processorNum].accept(entity, cell, context)
        }
    }

    private fun dumpColumn(entity: T) {
        for (processorNum in processors.indices) {
            val cell = sheet.getRow(processorNum).createCell(columnNumber.get())
            processors[processorNum].accept(entity, cell, context)
        }
        columnNumber.incrementAndGet()
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