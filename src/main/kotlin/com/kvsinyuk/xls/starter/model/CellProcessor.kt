package com.kvsinyuk.xls.starter.model

import com.kvsinyuk.xls.starter.model.context.Context
import org.apache.poi.ss.usermodel.Cell

interface CellProcessor<T> {

    fun name(): String

    fun accept(entity: T, cell: Cell, context: Context)
}