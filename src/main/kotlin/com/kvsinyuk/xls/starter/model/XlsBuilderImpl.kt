package com.kvsinyuk.xls.starter.model

import com.kvsinyuk.xls.starter.model.context.Context
import com.kvsinyuk.xls.starter.model.context.ContextImpl
import com.kvsinyuk.xls.starter.model.TableType.ROW_BASED
import com.kvsinyuk.xls.starter.model.TableType.COLUMN_BASED
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class XlsBuilderImpl<T>(
    private val file: File,
    private var processors: List<CellProcessor<T>>,
    private var tableType: TableType,
    private val workbook: Workbook = SXSSFWorkbook(),
    sheetName: String? = null
) : XlsBuilder<T> {

    private val context: Context

    private var sheet: Sheet

    private var rowNumber: AtomicInteger = AtomicInteger(1)
    private var columnNumber: AtomicInteger = AtomicInteger(1)

    init {
        sheet = createNewSheet(sheetName)
        context = ContextImpl(workbook)
        dumpSheetHead()
    }

    override fun dump(entity: T): XlsBuilderImpl<T> {
        when (tableType) {
            ROW_BASED -> dumpRow(entity)
            COLUMN_BASED -> dumpColumn(entity)
        }
        return this
    }

    override fun dumpAll(entities: Iterable<T>): XlsBuilderImpl<T> {
        entities.forEach { dump(it) }
        return this
    }

    override fun skipRows(num: Int): XlsBuilderImpl<T> {
        rowNumber.addAndGet(num)
        return this
    }

    override fun skipColumns(num: Int): XlsBuilderImpl<T> {
        columnNumber.addAndGet(num)
        return this
    }

    override fun createNewSheet(processors: List<CellProcessor<T>>, sheetName: String?): XlsBuilderImpl<T> {
        rowNumber = AtomicInteger(1)
        columnNumber = AtomicInteger(1)
        this.processors = processors
        context.clearContext()
        sheet = createNewSheet(sheetName)
        dumpSheetHead()
        return this
    }

    override fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilderImpl<T> {
        return this
            .apply { this.processors = processors }
            .also { context.clearContext() }
    }

    override fun build(): File {
        file.outputStream()
            .use { workbook.write(it) }
        return file
    }

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

    private fun createNewSheet(sheetName: String?): Sheet {
        return sheetName
            ?.let { workbook.createSheet(sheetName) }
            ?: workbook.createSheet()
    }

    override fun close() {
        if (workbook is SXSSFWorkbook) {
            workbook.dispose()
        }
        workbook.close()
    }
}
