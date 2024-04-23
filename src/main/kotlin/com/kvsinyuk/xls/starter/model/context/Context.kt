package com.kvsinyuk.xls.starter.model.context

import org.apache.poi.ss.usermodel.Workbook

interface Context {

    fun clearContext()

    fun <T : Any> get(key: String, createFunction: (Workbook) -> T): T
}
