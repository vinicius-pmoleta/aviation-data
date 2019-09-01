package com.aviationdata.features.search.viewmodel

import android.content.Context
import com.aviationdata.R
import com.aviationdata.core.structure.Aircraft
import com.aviationdata.features.search.view.SearchResult

class SearchMapper(private val context: Context) {

    private val noValuePlaceHolder: String =
        context.getString(R.string.default_no_value_placeholder)

    fun toPresentation(aircraft: Aircraft): SearchResult {
        return with(aircraft) {
            SearchResult(
                identification = context.getString(
                    R.string.search_result_identification,
                    identification.registration,
                    identification.icao24
                ),
                operation = context.getString(
                    R.string.search_result_operation,
                    operation.country.ifEmpty { noValuePlaceHolder },
                    operation.operator.ifEmpty { noValuePlaceHolder }
                ),
                model = information.model)
        }
    }
}