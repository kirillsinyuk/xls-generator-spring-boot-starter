package com.kvsinyuk.xls.starter.model.context

import org.apache.poi.ss.usermodel.Workbook
import kotlin.reflect.KClass

interface Context {

    fun <T : Any> get(key: String, target: KClass<out T>, createFunction: (Workbook) -> T): T

    fun <T: Any> get(key: String, target: KClass<out T>): T
}