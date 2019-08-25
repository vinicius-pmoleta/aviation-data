package com.aviationdata.features.search.data

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
