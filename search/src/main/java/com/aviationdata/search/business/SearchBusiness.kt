package com.aviationdata.search.business

import com.aviationdata.search.data.SearchRemoteRepository

class SearchBusiness(private val remoteRepository: SearchRemoteRepository) {

    suspend fun search(query: String, page: Int = 1): SearchData {
        return remoteRepository.search(query, page)
    }
}