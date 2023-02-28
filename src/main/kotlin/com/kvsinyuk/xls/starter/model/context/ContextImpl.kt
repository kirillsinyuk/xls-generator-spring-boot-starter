package com.kvsinyuk.xls.starter.model.context

import org.apache.poi.ss.usermodel.Workbook
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

class ContextImpl(
    private val workbook: Workbook
): Context {

    private val context: ConcurrentMap<String, Any> = ConcurrentHashMap()

    override fun <T : Any> get(key: String, target: KClass<out T>, createFunction: (Workbook) -> T): T {
        val value = createFunction.invoke(workbook)

        if(!target.isInstance(value)) {
            throw IllegalArgumentException("Value of key $key is not an instance of type ${target.simpleName}")
        }
        return context.getOrDefault(key, value) as T
    }

    override fun <T: Any> get(key: String, target: KClass<out T>): T {
        val value = context[key]

        if(!target.isInstance(value)) {
            throw IllegalArgumentException("Value of key $key is not an instance of type ${target.simpleName}")
        }
        return value as T
    }
}