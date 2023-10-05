package com.kvsinyuk.xls.starter.model.context

import org.apache.poi.ss.usermodel.Workbook
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ContextImpl(
    private val workbook: Workbook
): Context {

    private val context: ConcurrentMap<String, Any> = ConcurrentHashMap()

    override fun clearContext() {
        context.clear()
    }

    override fun <T : Any> get(key: String, createFunction: (Workbook) -> T) =
        context.computeIfAbsent(key) {
            createFunction.invoke(workbook)
        } as T
}