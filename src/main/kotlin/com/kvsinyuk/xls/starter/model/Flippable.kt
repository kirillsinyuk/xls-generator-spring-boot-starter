package com.kvsinyuk.xls.starter.model

import java.io.Closeable

sealed interface Flippable<T> : Closeable {

    fun createNewSheet(processors: List<CellProcessor<T>>, sheetName: String? = null): XlsBuilder<T>
}
