package com.kvsinyuk.xls.starter.model.context

import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ContextImpl(
    private val workbook: SXSSFWorkbook
): Context {

    private var sheet: SXSSFSheet = workbook.createSheet()
    private val context: ConcurrentMap<String, Any> = ConcurrentHashMap()

    init {
        sheet.trackAllColumnsForAutoSizing()
    }

    override fun getWorkbook(): SXSSFWorkbook = workbook

    override fun getCurrentSheet(): Sheet = sheet

    override fun clearContext() {
        context.clear()
    }

    override fun <T : Any> get(key: String, createFunction: (Workbook) -> T) =
        context.computeIfAbsent(key) {
            createFunction.invoke(workbook)
        } as T

    override fun <T: Any> get(key: String) =
        context[key] as T
}