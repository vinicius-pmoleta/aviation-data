package com.aviationdata.features.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.core.UserInteraction
import com.aviationdata.core.ViewState
import com.aviationdata.features.search.data.SearchState
import com.aviationdata.features.search.data.toResult
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel : ViewModel() {

    private val model = SearchModel(SearchRemoteRepository())

    private val _stateLiveData = MutableLiveData<ViewState<SearchState>>()
    val stateLiveData: LiveData<ViewState<SearchState>>
        get() = _stateLiveData

    init {
        emit(ViewState.FirstLaunch)
    }

    fun handle(interaction: UserInteraction) {
        // TODO single entry point for user interactions and interpretation to transactions
    }

    fun search(context: Context, query: String) {
        viewModelScope.launch {
            try {
                emit(ViewState.Loading.FromEmpty)

                val results = model.search(query)
                    .map { it.toResult(context) }

                emit(ViewState.Success(SearchState(query, results)))
            } catch (error: Exception) {
                Log.e(TAG, "Error", error)
                emit(ViewState.Failed(error))
            }
        }
    }

    private fun emit(state: ViewState<SearchState>) {
        _stateLiveData.value = state
    }
}