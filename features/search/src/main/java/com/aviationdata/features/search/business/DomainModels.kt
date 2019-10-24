package com.aviationdata.features.search.business

data class Identification(
    val icao24: String = "",
    val registration: String = ""
)

data class Information(
    val model: String = "",
    val modelCode: String = "",
    val categoryDescription: String = "",
    val notes: String = ""
)

data class Construction(
    val manufacturerName: String = "",
    val constructionDate: String = "",
    val engines: String = ""
)

data class Operation(
    val operator: String = "",
    val operatorCallsign: String = "",
    val operatorIcao: String = "",
    val owner: String = "",
    val country: String = ""
)

data class Tracking(
    val lastSeen: String = "",
    val firstSeen: String = ""
)

data class Aircraft(
    val identification: Identification = Identification(),
    val information: Information = Information(),
    val construction: Construction = Construction(),
    val operation: Operation = Operation(),
    val tracking: Tracking = Tracking()
)

data class SearchData(
    val results: List<Aircraft> = emptyList(),
    val query: String = "",
    val pagination: Pagination
)

data class Pagination(var page: Int = 1, var totalPages: Int = 1) {

    fun hasMore() = page < totalPages
}