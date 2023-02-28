package com.kvsinyuk.xls.starter.model.processors

import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class InstantCellProcessor<ENTITY>(
    name: String,
    format: String? = "dd/mm/yyyy hh:mm",
    extractor: (ENTITY) -> Instant
) : AbstractCellProcessor<Instant, ENTITY>(name, format, extractor) {

    private val CONTEXT_KEY = "instant_cell_processor"

    override fun acceptExtracted(value: Instant, cell: Cell, context: Context) {
        cell.cellStyle = context.get(CONTEXT_KEY, CellStyle::class.java, cellStyleFactory())
        cell.setCellValue(LocalDateTime.ofInstant(value, ZoneId.systemDefault()))
    }

    private fun cellStyleFactory(): (Workbook) -> CellStyle =
        { workbook ->
            workbook.createCellStyle()
                .apply { dataFormat = workbook.createDataFormat().getFormat(format) }
        }
}