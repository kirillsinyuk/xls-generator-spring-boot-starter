package com.kvsinyuk.xls.starter.model.context

import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFWorkbook

interface Context {

    fun getWorkbook(): SXSSFWorkbook
    fun getCurrentSheet(): Sheet

    fun <T : Any> get(key: String, createFunction: (Workbook) -> T): T

    fun <T: Any> get(key: String): T
}