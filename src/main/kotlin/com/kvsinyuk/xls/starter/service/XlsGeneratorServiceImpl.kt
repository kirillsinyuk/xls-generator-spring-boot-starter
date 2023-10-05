package com.kvsinyuk.xls.starter.service

import com.kvsinyuk.xls.starter.model.CellProcessor
import com.kvsinyuk.xls.starter.model.TableType
import com.kvsinyuk.xls.starter.model.XlsBuilderImpl
import java.io.File

class XlsGeneratorServiceImpl: XlsGeneratorService {
    override fun <T> newXlsBuilder(
        file: File,
        cellProcessors: List<CellProcessor<T>>,
        type: TableType
    ) = XlsBuilderImpl(file, cellProcessors, type)
}
