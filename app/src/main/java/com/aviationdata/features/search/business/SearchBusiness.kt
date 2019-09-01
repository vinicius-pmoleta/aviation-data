package com.aviationdata.features.search.business

import com.aviationdata.core.structure.Aircraft
import com.aviationdata.features.search.data.SearchRemoteRepository

class SearchBusiness(private val remoteRepository: SearchRemoteRepository) {

    suspend fun search(query: String): List<Aircraft> {
        return remoteRepository.search(query, 1)
    }
}