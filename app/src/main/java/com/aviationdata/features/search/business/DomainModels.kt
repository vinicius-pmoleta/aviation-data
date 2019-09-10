package com.aviationdata.features.search.business

import com.aviationdata.core.structure.Aircraft

data class SearchData(
    val results: List<Aircraft> = emptyList(),
    val query: String = "",
    val pagination: Pagination
)

data class Pagination(var page: Int = 1, var totalPages: Int = 1) {

    fun hasMore() = page < totalPages
}