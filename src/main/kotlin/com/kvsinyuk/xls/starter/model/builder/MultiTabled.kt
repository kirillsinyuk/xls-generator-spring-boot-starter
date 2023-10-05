package com.kvsinyuk.xls.starter.model.builder

import com.kvsinyuk.xls.starter.model.CellProcessor
import java.io.Closeable

sealed interface MultiTabled<T> : Closeable {

    fun secondaryTable(processors: List<CellProcessor<T>>): XlsBuilder<T>
}
