package com.aviationdata.features.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.core.structure.UserInteraction
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.data.SearchInteraction
import com.aviationdata.features.search.data.SearchState
import com.aviationdata.features.search.data.toResult
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel(
    private val business: SearchBusiness
) : ViewModel(), ViewModelHandler<SearchState> {

    private val _stateLiveData = MutableLiveData<ViewState<SearchState>>()
    private val stateLiveData: LiveData<ViewState<SearchState>>
        get() = _stateLiveData

    init {
        emit(ViewState.FirstLaunch)
    }

    override fun handle(interaction: UserInteraction) {
        when (interaction) {
            is SearchInteraction -> with(interaction) { runSearch(context, query) }
        }
    }

    override fun state(): LiveData<ViewState<SearchState>> {
        return stateLiveData
    }

    private fun runSearch(context: Context, query: String) {
        viewModelScope.launch {
            try {
                emit(ViewState.Loading.FromEmpty)

                val results = business.search(query)
                    .map { it.toResult(context) }

                emit(ViewState.Success(SearchState(query, results)))
            } catch (error: Exception) {
                emit(ViewState.Failed(error))
            }
        }
    }

    private fun emit(state: ViewState<SearchState>) {
        _stateLiveData.value = state
    }
}