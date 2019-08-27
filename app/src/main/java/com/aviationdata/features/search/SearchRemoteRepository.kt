package com.aviationdata.features.search

import com.aviationdata.core.structure.Aircraft
import com.aviationdata.features.search.data.toAircraft

private const val DEFAULT_SEARCH_RESULTS_PER_PAGE = 50

class SearchRemoteRepository(private val service: SearchService) {

    suspend fun search(query: String, page: Int): List<Aircraft> {
        return service
            .search(query, page, DEFAULT_SEARCH_RESULTS_PER_PAGE, "", "")
            .results
            .map { it.toAircraft() }
    }
}