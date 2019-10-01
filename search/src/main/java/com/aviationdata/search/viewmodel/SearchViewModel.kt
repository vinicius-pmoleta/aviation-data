package com.aviationdata.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.core.structure.AppDispatchers
import com.aviationdata.core.structure.UserInteraction
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.search.business.Pagination
import com.aviationdata.search.business.SearchBusiness
import com.aviationdata.search.view.SearchInteraction
import com.aviationdata.search.view.SearchState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class InternalData(
    var pagination: Pagination = Pagination(),
    var state: SearchState = SearchState()
)

class SearchViewModel(
    private val business: SearchBusiness,
    private val mapper: SearchMapper,
    private val dispatchers: AppDispatchers
) : ViewModel(), ViewModelHandler<SearchState> {

    internal var internalData = InternalData()

    private val _stateLiveData = MutableLiveData<ViewState<SearchState>>()
    private val stateLiveData: LiveData<ViewState<SearchState>>
        get() = _stateLiveData

    init {
        resetSearch()
    }

    override fun handle(interaction: UserInteraction) {
        when (interaction) {
            is SearchInteraction.Search -> runSearch(interaction.query)
            is SearchInteraction.More -> loadMoreResults()
            is SearchInteraction.Retry -> retryLastExecution()
            is SearchInteraction.Reset -> resetSearch()
        }
    }

    override fun liveState(): LiveData<ViewState<SearchState>> {
        return stateLiveData
    }

    private fun runSearch(query: String) {
        emit(ViewState.Loading.FromEmpty)
        internalData = InternalData(
            pagination = Pagination(),
            state = SearchState(query = query)
        )
        execute(internalData.pagination.page)
    }

    private fun loadMoreResults() {
        if (internalData.pagination.hasMore()) {
            execute(internalData.pagination.page + 1)
        }
    }

    private fun retryLastExecution() {
        emit(ViewState.Loading.FromPrevious(internalData.state))
        execute(internalData.pagination.page)
    }

    private fun resetSearch() {
        internalData = InternalData()
        emit(ViewState.Initializing)
    }

    private fun execute(page: Int) {
        viewModelScope.launch {
            try {
                val data = withContext(dispatchers.io) {
                    business.search(internalData.state.query, page)
                }
                val newResults = data.results.map { mapper.toPresentation(it) }
                internalData.pagination = data.pagination

                internalData.state = internalData.state.let {
                    it.copy(
                        results = it.results + newResults
                    )
                }
                emit(ViewState.Success(internalData.state))
            } catch (error: Throwable) {
                emit(ViewState.Failed(error))
            }
        }
    }

    private fun emit(state: ViewState<SearchState>) {
        _stateLiveData.value = state
    }
}