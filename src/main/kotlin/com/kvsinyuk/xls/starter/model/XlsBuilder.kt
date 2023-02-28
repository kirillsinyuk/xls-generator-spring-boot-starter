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
    private val processors: List<CellProcessor<T>>
) : Closeable {

    private val workbook: SXSSFWorkbook
    private val sheet: SXSSFSheet
    private val context: Context


    private var rowNumber: AtomicInteger = AtomicInteger(1)

    init {
        this.workbook = createWorkbook()
        this.sheet = workbook.createSheet()
        context = ContextImpl(this.workbook)
        sheet.trackAllColumnsForAutoSizing()
        dumpSheetHead()
    }

    fun dumpRow(entity: T) {
        val row = sheet.createRow(rowNumber.getAndIncrement())

        for (processorNum in processors.indices) {
            val cell = row.createCell(processorNum)
            processors[processorNum].accept(entity, cell, context)
        }
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

    override fun close() {
        workbook.dispose()
        workbook.close()
    }
}