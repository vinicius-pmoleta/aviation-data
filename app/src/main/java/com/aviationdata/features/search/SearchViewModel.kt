package com.aviationdata.features.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val model = SearchModel(SearchRemoteRepository())

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val aircrafts = model.search(query)
                Log.d("TEST", aircrafts.toString())
            } catch (error: Exception) {
                Log.e("TEST", "Error", error)
            }
        }
    }
}