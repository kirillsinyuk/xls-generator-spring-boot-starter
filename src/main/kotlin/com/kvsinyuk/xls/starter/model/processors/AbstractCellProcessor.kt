package com.kvsinyuk.xls.starter.model.processors

import com.kvsinyuk.xls.starter.model.CellProcessor
import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook

abstract class AbstractCellProcessor<T, ENTITY>(
    val name: String,
    val format: String? = null,
    val extractor: (ENTITY) -> T
) : CellProcessor<ENTITY> {

    abstract fun acceptExtracted(value: T, cell: Cell, context: Context)

    open fun cellStyle(): (Workbook) -> CellStyle = { workbook ->
        workbook.createCellStyle()
    }

    override fun accept(entity: ENTITY, cell: Cell, context: Context) {
        acceptExtracted(extractor.invoke(entity), cell, context)
    }

    override fun name() = name
}