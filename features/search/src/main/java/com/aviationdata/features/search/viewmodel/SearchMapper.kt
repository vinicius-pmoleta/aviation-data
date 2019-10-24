package com.aviationdata.features.search.viewmodel

import android.content.Context
import com.aviationdata.common.core.R as coreR
import com.aviationdata.features.search.R
import com.aviationdata.features.search.business.Aircraft
import com.aviationdata.features.search.view.SearchResult

class SearchMapper(private val context: Context) {

    private val noValuePlaceHolder: String =
        context.getString(coreR.string.default_no_value_placeholder)

    fun toPresentation(aircraft: Aircraft): SearchResult {
        return with(aircraft) {
            SearchResult(
                identification = identification.registration.ifEmpty {
                    identification.icao24.ifEmpty { noValuePlaceHolder }
                },
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