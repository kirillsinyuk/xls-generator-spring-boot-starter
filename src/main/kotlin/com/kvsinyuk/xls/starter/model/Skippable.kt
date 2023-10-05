package com.kvsinyuk.xls.starter.model

import java.io.Closeable

sealed interface Skippable<T> : Closeable {

    fun skipRows(num: Int): XlsBuilder<T>

    fun skipColumns(num: Int): XlsBuilder<T>
}
