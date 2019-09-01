package com.aviationdata.features.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.core.structure.AppDispatchers
import com.aviationdata.core.structure.UserInteraction
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.business.SearchBusiness
import com.aviationdata.features.search.view.SearchInteraction
import com.aviationdata.features.search.view.SearchState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val business: SearchBusiness,
    private val mapper: SearchMapper,
    private val dispatchers: AppDispatchers
) : ViewModel(), ViewModelHandler<SearchState> {

    private val _stateLiveData = MutableLiveData<ViewState<SearchState>>()
    private val stateLiveData: LiveData<ViewState<SearchState>>
        get() = _stateLiveData

    init {
        emit(ViewState.FirstLaunch)
    }

    override fun handle(interaction: UserInteraction) {
        when (interaction) {
            is SearchInteraction -> with(interaction) { runSearch(query) }
        }
    }

    override fun state(): LiveData<ViewState<SearchState>> {
        return stateLiveData
    }

    private fun runSearch(query: String) {
        viewModelScope.launch {
            try {
                emit(ViewState.Loading.FromEmpty)

                val results = withContext(dispatchers.io) {
                    business.search(query)
                }.map {
                    mapper.toPresentation(it)
                }

                emit(ViewState.Success(
                    SearchState(
                        query,
                        results
                    )
                ))
            } catch (error: Throwable) {
                emit(ViewState.Failed(error))
            }
        }
    }

    private fun emit(state: ViewState<SearchState>) {
        _stateLiveData.value = state
    }
}