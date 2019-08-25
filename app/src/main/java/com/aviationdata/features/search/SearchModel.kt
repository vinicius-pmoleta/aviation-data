package com.aviationdata.features.search

import com.aviationdata.core.Aircraft

class SearchModel(private val remoteRepository: SearchRemoteRepository) {

    suspend fun search(query: String): List<Aircraft> {
        return remoteRepository.search(query, 1)
    }
}