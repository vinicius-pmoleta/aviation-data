package com.aviationdata.features.search.data

import com.aviationdata.features.search.business.Pagination
import com.aviationdata.features.search.business.SearchData

internal const val DEFAULT_SEARCH_RESULTS_PER_PAGE = 50
internal const val DEFAULT_SEARCH_SORT_BY_FIELD = ""
internal const val DEFAULT_SEARCH_SORT_ORDER_FIELD = ""

class SearchRemoteRepository(private val service: SearchService) {

    suspend fun search(query: String, page: Int): SearchData {
        val response = service
            .search(
                query,
                page,
                DEFAULT_SEARCH_RESULTS_PER_PAGE,
                DEFAULT_SEARCH_SORT_BY_FIELD,
                DEFAULT_SEARCH_SORT_ORDER_FIELD
            )

        return SearchData(
            results = response.results.map { it.toAircraft() },
            query = query,
            pagination = Pagination(
                page = page,
                totalPages = response.pages
            )
        )
    }
}