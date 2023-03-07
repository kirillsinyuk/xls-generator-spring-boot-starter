package com.kvsinyuk.xls.starter.model

import com.kvsinyuk.xls.starter.model.context.Context
import com.kvsinyuk.xls.starter.model.context.ContextImpl
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.Closeable
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class XlsBuilder<T>(
    private val file: File,
    private var processors: List<CellProcessor<T>>
) : Closeable {

    private val context: Context


    private var rowNumber: AtomicInteger = AtomicInteger(1)

    init {
        context = ContextImpl(createWorkbook())
        dumpSheetHead()
    }

    fun dumpRow(entity: T) {
        val row = context.getCurrentSheet().createRow(rowNumber.getAndIncrement())

        for (processorNum in processors.indices) {
            val cell = row.createCell(processorNum)
            processors[processorNum].accept(entity, cell, context)
        }
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

    fun build(): File {
        file.outputStream()
            .use { context.getWorkbook().write(it) }
        return file
    }

    private fun createWorkbook(rowAccessWindowSize: Int = 50) =
        SXSSFWorkbook(rowAccessWindowSize)

    private fun dumpSheetHead() {
        val font = context.getWorkbook().createFont()
            .apply { bold = true }
        val style = context.getWorkbook().createCellStyle()
            .also { it.setFont(font) }

        val row = context.getCurrentSheet().createRow(0)
        for (processorNum in processors.indices) {
            row.createCell(processorNum)
                .also { it.setCellValue(processors[processorNum].name()) }
                .apply { cellStyle = style }
        }
    }

    override fun close() {
        context.getWorkbook().dispose()
        context.getWorkbook().close()
    }
}