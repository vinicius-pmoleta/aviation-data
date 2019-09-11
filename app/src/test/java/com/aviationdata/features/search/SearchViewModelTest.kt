package com.aviationdata.features.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aviationdata.core.structure.Aircraft
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.business.Pagination
import com.aviationdata.features.search.business.SearchBusiness
import com.aviationdata.features.search.business.SearchData
import com.aviationdata.features.search.view.SearchInteraction
import com.aviationdata.features.search.view.SearchResult
import com.aviationdata.features.search.view.SearchState
import com.aviationdata.features.search.viewmodel.SearchMapper
import com.aviationdata.features.search.viewmodel.SearchViewModel
import com.aviationdata.rules.CoroutinesTestRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val executionRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    private val business: SearchBusiness = mock()
    private val mapper: SearchMapper = mock()
    private val observer: Observer<ViewState<SearchState>> = mock()

    private lateinit var viewModelHandler: ViewModelHandler<SearchState>

    @Before
    fun setUp() {
        viewModelHandler = SearchViewModel(
            business,
            mapper,
            coroutinesRule.dispatchers
        )
        viewModelHandler.liveState().observeForever(observer)
    }

    private fun getInternalState() = (viewModelHandler as SearchViewModel).state

    private fun getInternalPagination() = (viewModelHandler as SearchViewModel).pagination

    @Test
    fun `verify initialization emits first launch state`() {
        verify(observer).onChanged(ViewState.FirstLaunch)
    }

    @Test
    fun `verify search succeeds when search interaction is handled`() = runBlockingTest {
        val query = "query"

        val aircraft = Aircraft()
        val data = SearchData(
            results = listOf(aircraft),
            pagination = Pagination(page = 1, totalPages = 10)
        )
        whenever(business.search(query, 1)).thenReturn(data)

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction.Search(query))

        verify(business).search(query)
        verify(mapper).toPresentation(aircraft)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.FirstLaunch, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)

            val state = SearchState(query, listOf(result))
            assertEquals(ViewState.Success(state), thirdValue)

            assertEquals(data.pagination, getInternalPagination())
            assertEquals(state, getInternalState())
        }
    }

    @Test
    fun `verify search fails when search interaction is handled`() = runBlockingTest {
        val query = "query"
        val page = 1

        getInternalPagination().page = page

        val reason = RuntimeException()
        whenever(business.search(query, page)).thenThrow(reason)

        viewModelHandler.handle(SearchInteraction.Search(query))

        verify(business).search(query, page)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.FirstLaunch, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(ViewState.Failed(reason), thirdValue)

            assertEquals(Pagination(page = 1), getInternalPagination())
            assertEquals(SearchState(query = query), getInternalState())
        }
    }

    @Test
    fun `verify search executed when retry interaction is handled`() = runBlockingTest {
        val query = "query"
        val page = 2

        getInternalPagination().page = page

        val aircraft = Aircraft()
        whenever(business.search(query, page)).thenReturn(
            SearchData(
                results = listOf(aircraft),
                pagination = Pagination()
            )
        )

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction.Retry)

        verify(business).search(query, page)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(2)).onChanged(capture())

            assertEquals(ViewState.Loading.FromPrevious(getInternalState()), firstValue)
            assertEquals(
                ViewState.Success(
                    SearchState(
                        query,
                        listOf(result)
                    )
                ), secondValue
            )
        }
    }

    @Test
    fun `verify next page searched when more results interaction is handled`() = runBlockingTest {
        val query = "query"
        val page = 3

        getInternalPagination().let {
            it.page = 2
            it.totalPages = 3
        }

        val aircraft = Aircraft()
        whenever(business.search(query, page)).thenReturn(
            SearchData(
                results = listOf(aircraft),
                pagination = Pagination(page = page, totalPages = 3)
            )
        )

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction.More)

        verify(business).search(query, page)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(1)).onChanged(capture())

            val expectedState = SearchState(query, listOf(result))
            assertEquals(ViewState.Success(expectedState), firstValue)

            assertEquals(Pagination(page = page), getInternalPagination())
            assertEquals(SearchState(query = query), getInternalState())
        }
    }

    @Test
    fun `verify next page not searched when more results interaction is handled without more pages`() =
        runBlockingTest {
            val query = "query"

            getInternalPagination().apply {
                page = 2
                totalPages = 2
            }

            viewModelHandler.handle(SearchInteraction.More)

            verify(business, never()).search(eq(query), anyInt())
        }

    @Test
    fun `verify search reset when reset interaction is handled`() = runBlockingTest {
        getInternalPagination().page = 2

        viewModelHandler.handle(SearchInteraction.Reset)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(ViewState.FirstLaunch, firstValue)

            assertEquals(Pagination(), getInternalPagination())
            assertEquals(SearchState(), getInternalState())
        }
    }
}