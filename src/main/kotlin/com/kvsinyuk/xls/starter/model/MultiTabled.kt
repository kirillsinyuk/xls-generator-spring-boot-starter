package com.kvsinyuk.xls.starter.model

import java.io.Closeable

sealed interface MultiTabled<T> : Closeable {

    fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilder<T>
}
