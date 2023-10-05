package com.kvsinyuk.xls.starter.model.builder

import java.io.Closeable
import java.io.File

sealed interface XlsBuilder<T> : Skippable<T>, Flippable<T>, MultiTabled<T>, Closeable {

    fun dump(entity: T): XlsBuilder<T>

    fun dumpAll(entities: Iterable<T>): XlsBuilder<T>

    fun build(): File
}