package com.kvsinyuk.xls.starter.model.processors

import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import java.time.LocalDate

class LocalDateCellProcessor<ENTITY>(
    name: String,
    format: String? = "dd/mm/yyyy",
    extractor: (ENTITY) -> LocalDate
) : AbstractCellProcessor<LocalDate, ENTITY>(name, format, extractor) {

    private val CONTEXT_KEY = "date_cell_processor"

    override fun acceptExtracted(value: LocalDate, cell: Cell, context: Context) {
        cell.cellStyle = context.get(CONTEXT_KEY, CellStyle::class.java, cellStyleFactory())
        cell.setCellValue(value)
    }

    private fun cellStyleFactory(): (Workbook) -> CellStyle =
        { workbook ->
            workbook.createCellStyle()
                .apply { dataFormat = workbook.createDataFormat().getFormat(format) }
        }
}