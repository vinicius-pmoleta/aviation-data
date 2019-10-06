package com.aviationdata.search.viewmodel

import android.content.Context
import com.aviationdata.core.structure.Aircraft
import com.aviationdata.search.R
import com.aviationdata.search.view.SearchResult
import com.aviationdata.core.R as coreR

class SearchMapper(private val context: Context) {

    private val noValuePlaceHolder: String =
        context.getString(coreR.string.default_no_value_placeholder)

    fun toPresentation(aircraft: Aircraft): SearchResult {
        return with(aircraft) {
            SearchResult(
                identification = context.getString(
                    R.string.search_result_identification,
                    identification.registration.ifEmpty { noValuePlaceHolder },
                    identification.icao24.ifEmpty { noValuePlaceHolder }
                ),
                operation = context.getString(
                    R.string.search_result_operation,
                    operation.country.ifEmpty { noValuePlaceHolder },
                    operation.operator.ifEmpty { noValuePlaceHolder }
                ),
                model = information.model
            )
        }
    }
}