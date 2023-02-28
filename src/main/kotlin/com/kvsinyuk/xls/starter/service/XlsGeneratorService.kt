package com.kvsinyuk.xls.starter.service

import com.kvsinyuk.xls.starter.model.CellProcessor
import com.kvsinyuk.xls.starter.model.XlsBuilder
import java.io.File

interface XlsGeneratorService {
    fun <T> newXlsBuilder(file: File, cellProcessors: List<CellProcessor<T>>): XlsBuilder<T>
}