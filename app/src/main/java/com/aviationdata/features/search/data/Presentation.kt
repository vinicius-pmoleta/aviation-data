package com.aviationdata.features.search.data

import android.content.Context
import com.aviationdata.core.structure.UserInteraction

data class SearchResult(
    val identification: String = "",
    val operation: String = "",
    val model: String = ""
)

data class SearchState(
    val query: String = "",
    val results: List<SearchResult> = mutableListOf()
)

data class SearchInteraction(
    val context: Context,
    val query: String
) : UserInteraction