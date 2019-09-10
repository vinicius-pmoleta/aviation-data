package com.aviationdata.features.search.business

import com.aviationdata.features.search.data.SearchRemoteRepository

class SearchBusiness(private val remoteRepository: SearchRemoteRepository) {

    suspend fun search(query: String, page: Int = 1): SearchData {
        return remoteRepository.search(query, page)
    }
}