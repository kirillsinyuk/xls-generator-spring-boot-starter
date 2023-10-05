package com.kvsinyuk.xls.starter.service

import com.kvsinyuk.xls.starter.model.CellProcessor
import com.kvsinyuk.xls.starter.model.TableType
import com.kvsinyuk.xls.starter.model.builder.XlsBuilder
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.File

interface XlsGeneratorService {
    fun <T> newXlsBuilder(
        file: File,
        cellProcessors: List<CellProcessor<T>>,
        type: TableType = TableType.ROW_BASED,
        workbook: Workbook = SXSSFWorkbook()
    ): XlsBuilder<T>
}
