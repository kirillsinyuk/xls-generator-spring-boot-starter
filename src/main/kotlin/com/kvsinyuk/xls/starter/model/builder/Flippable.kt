package com.kvsinyuk.xls.starter.model.builder

import com.kvsinyuk.xls.starter.model.CellProcessor
import java.io.Closeable

sealed interface Flippable<T> : Closeable {

    fun createNewSheet(processors: List<CellProcessor<T>>, sheetName: String? = null): XlsBuilder<T>
}
