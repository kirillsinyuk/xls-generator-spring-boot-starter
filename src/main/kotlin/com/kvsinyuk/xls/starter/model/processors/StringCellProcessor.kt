package com.kvsinyuk.xls.starter.model.processors

import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell

class StringCellProcessor<ENTITY>(
    name: String,
    format: String? = null,
    extractor: (ENTITY) -> String
) : AbstractCellProcessor<String, ENTITY>(name, format, extractor) {

    override fun acceptExtracted(value: String, cell: Cell, context: Context) {
        cell.setCellValue(value.trim())
    }
}