package com.aviationdata.features.search.data

import com.aviationdata.core.structure.Aircraft

internal const val DEFAULT_SEARCH_RESULTS_PER_PAGE = 50
internal const val DEFAULT_SEARCH_SORT_BY_FIELD = ""
internal const val DEFAULT_SEARCH_SORT_ORDER_FIELD = ""

class SearchRemoteRepository(private val service: SearchService) {

    suspend fun search(query: String, page: Int): List<Aircraft> {
        return service
            .search(
                query,
                page,
                DEFAULT_SEARCH_RESULTS_PER_PAGE,
                DEFAULT_SEARCH_SORT_BY_FIELD,
                DEFAULT_SEARCH_SORT_ORDER_FIELD
            )
            .results
            .map { it.toAircraft() }
    }
}