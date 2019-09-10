package com.aviationdata.features.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.core.structure.AppDispatchers
import com.aviationdata.core.structure.UserInteraction
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.business.Pagination
import com.aviationdata.features.search.business.SearchBusiness
import com.aviationdata.features.search.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val business: SearchBusiness,
    private val mapper: SearchMapper,
    private val dispatchers: AppDispatchers
) : ViewModel(), ViewModelHandler<SearchState> {

    private var pagination = Pagination()
    private var state = SearchState()

    private val _stateLiveData = MutableLiveData<ViewState<SearchState>>()
    private val stateLiveData: LiveData<ViewState<SearchState>>
        get() = _stateLiveData

    init {
        clearSearch()
    }

    override fun handle(interaction: UserInteraction) {
        when (interaction) {
            is SearchInteraction -> runSearch(interaction.query)
            is MoreResultsInteraction -> loadMoreResults()
            is RetryExecutionInteraction -> retryLastExecution()
            is ClearSearchInteraction -> clearSearch()
        }
    }

    override fun state(): LiveData<ViewState<SearchState>> {
        return stateLiveData
    }

    private fun runSearch(query: String) {
        emit(ViewState.Loading.FromEmpty)
        pagination = Pagination(page = 1)
        state = SearchState(query = query)
        execute(pagination.page)
    }

    private fun loadMoreResults() {
        if (pagination.hasMore()) {
            execute(pagination.page)
        }
    }

    private fun retryLastExecution() {
        emit(ViewState.Loading.FromEmpty)
        execute(pagination.page)
    }

    private fun clearSearch() {
        pagination = Pagination()
        state = SearchState()
        emit(ViewState.FirstLaunch)
    }

    private fun execute(page: Int) {
        viewModelScope.launch {
            try {
                val data = withContext(dispatchers.io) {
                    business.search(state.query, page)
                }
                val newResults = data.results.map { mapper.toPresentation(it) }
                pagination = data.pagination.copy(page = page + 1)

                state = state.let {
                    it.copy(
                        results = it.results + newResults
                    )
                }
                emit(ViewState.Success(state))
            } catch (error: Throwable) {
                emit(ViewState.Failed(error))
            }
        }
    }

    private fun emit(state: ViewState<SearchState>) {
        _stateLiveData.value = state
    }
}