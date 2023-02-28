package com.kvsinyuk.xls.starter.model.processors

import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import java.math.BigDecimal

class DecimalCellProcessor<ENTITY>(
    name: String,
    format: String? = "#,###.00",
    extractor: (ENTITY) -> BigDecimal
) : AbstractCellProcessor<BigDecimal, ENTITY>(name, format, extractor) {

    private val CONTEXT_KEY = "decimal_cell_processor"

    override fun acceptExtracted(value: BigDecimal, cell: Cell, context: Context) {
        cell.cellStyle = context.get(CONTEXT_KEY, CellStyle::class, cellStyleFactory())
        cell.setCellValue(value.toDouble())
    }

    private fun cellStyleFactory(): (Workbook) -> CellStyle = { workbook ->
        workbook.createCellStyle()
            .apply { dataFormat = workbook.createDataFormat().getFormat(format) }
    }
}