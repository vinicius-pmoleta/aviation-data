package com.aviationdata.features.search.data

import android.content.Context
import com.aviationdata.R
import com.aviationdata.core.structure.Aircraft
import com.aviationdata.core.structure.Identification
import com.aviationdata.core.structure.Information
import com.aviationdata.core.structure.Operation

fun RawSearchResult.toAircraft() = Aircraft(
    identification = Identification(
        icao24 = icao24,
        registration = registration
    ),
    operation = Operation(
        operator = operator,
        country = country
    ),
    information = Information(
        model = model
    )
)

fun Aircraft.toResult(context: Context) = SearchResult(
    identification = context.getString(
        R.string.search_result_identification,
        identification.registration,
        identification.icao24
    ),
    operation = context.getString(
        R.string.search_result_operation,
        operation.country.ifEmpty { "-" },
        operation.operator.ifEmpty { "-" }
    ),
    model = information.model
)