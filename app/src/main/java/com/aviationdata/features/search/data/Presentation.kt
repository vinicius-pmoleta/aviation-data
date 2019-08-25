package com.aviationdata.features.search.data

data class SearchResult(
    val identification: String = "",
    val operation: String = "",
    val model: String = ""
)

data class SearchState(
    val query: String = "",
    val results: List<SearchResult> = mutableListOf()
)