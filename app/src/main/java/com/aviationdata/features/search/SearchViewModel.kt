package com.aviationdata.features.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.features.search.data.SearchState
import com.aviationdata.features.search.data.toResult
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel : ViewModel() {

    private val model = SearchModel(SearchRemoteRepository())

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState>
        get() = _stateLiveData


    fun search(context: Context, query: String) {
        viewModelScope.launch {
            try {
                val results = model.search(query)
                    .map { it.toResult(context) }

                _stateLiveData.value = SearchState(results)

                Log.d(TAG, results.toString())
            } catch (error: Exception) {
                Log.e(TAG, "Error", error)
            }
        }
    }
}