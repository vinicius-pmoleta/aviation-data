package com.aviationdata.features.search.data

import com.aviationdata.features.search.business.Aircraft
import com.aviationdata.features.search.business.Identification
import com.aviationdata.features.search.business.Information
import com.aviationdata.features.search.business.Operation
import com.google.gson.annotations.SerializedName

data class RawSearchResponse(
    @SerializedName("content") val results: List<RawSearchResult> = emptyList(),
    @SerializedName("totalElements") val resultsSize: Int = 0,
    @SerializedName("totalPages") val pages: Int = 0,
    @SerializedName("size") val resultsPerPage: Int = 0
)

data class RawSearchResult(
    val icao24: String = "",
    val registration: String = "",
    val model: String = "",
    val operator: String = "",
    val country: String = ""
)

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
