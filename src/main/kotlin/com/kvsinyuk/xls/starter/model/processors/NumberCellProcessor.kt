package com.kvsinyuk.xls.starter.model.processors

import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import java.math.BigInteger

open class NumberCellProcessor<ENTITY>(
    name: String,
    format: String? = "####",
    extractor: (ENTITY) -> BigInteger,
) : AbstractCellProcessor<BigInteger, ENTITY>(name, format, extractor) {

    private val CONTEXT_KEY = "number_cell_processor"

    override fun acceptExtracted(value: BigInteger, cell: Cell, context: Context) {
        cell.cellStyle = context.get(CONTEXT_KEY, cellStyle())
        cell.setCellValue(value.toDouble())
    }

    override fun cellStyle(): (Workbook) -> CellStyle = { workbook ->
        workbook.createCellStyle()
            .apply { dataFormat = workbook.createDataFormat().getFormat(format) }
    }
}
