package com.aviationdata.features.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aviationdata.common.core.structure.Aircraft
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.common.core.structure.ViewState
import com.aviationdata.common.core.test.CoroutinesTestRule
import com.aviationdata.features.search.business.Pagination
import com.aviationdata.features.search.business.SearchBusiness
import com.aviationdata.features.search.business.SearchData
import com.aviationdata.features.search.view.SearchInteraction
import com.aviationdata.features.search.view.SearchResult
import com.aviationdata.features.search.view.SearchState
import com.aviationdata.features.search.viewmodel.InternalData
import com.aviationdata.features.search.viewmodel.SearchMapper
import com.aviationdata.features.search.viewmodel.SearchViewModel
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val DEFAULT_QUERY = "query"

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

    private fun internalData() =
        (viewModelHandler as SearchViewModel).internalData

    private fun mockInternalData(data: InternalData) {
        (viewModelHandler as SearchViewModel).internalData = data
    }

    @Test
    fun `verify initialization emits first launch state`() {
        verify(observer).onChanged(ViewState.Initializing)
    }

    @Test
    fun `verify search succeeds when search interaction is handled`() = runBlockingTest {
        val aircraft = Aircraft()
        val data = SearchData(
            results = listOf(aircraft),
            pagination = Pagination(page = 2, totalPages = 10)
        )
        whenever(business.search(DEFAULT_QUERY, 1)).thenReturn(data)

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction.Search(DEFAULT_QUERY))

        verify(business).search(DEFAULT_QUERY)
        verify(mapper).toPresentation(aircraft)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)

            val state = SearchState(DEFAULT_QUERY, listOf(result))
            assertEquals(ViewState.Success(state), thirdValue)

            assertEquals(data.pagination, internalData().pagination)
            assertEquals(state, internalData().state)
        }
    }

    @Test
    fun `verify search fails when search interaction is handled`() = runBlockingTest {
        mockInternalData(
            InternalData(
                pagination = Pagination(page = 1),
                state = SearchState(query = DEFAULT_QUERY)
            )
        )

        val reason = RuntimeException()
        whenever(business.search(DEFAULT_QUERY, 1)).thenThrow(reason)

        viewModelHandler.handle(SearchInteraction.Search(DEFAULT_QUERY))

        verify(business).search(DEFAULT_QUERY, 1)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(ViewState.Failed(reason), thirdValue)

            assertEquals(
                Pagination(page = 1),
                internalData().pagination
            )
            assertEquals(
                SearchState(query = DEFAULT_QUERY),
                internalData().state
            )
        }
    }

    @Test
    fun `verify search executed when retry interaction is handled`() = runBlockingTest {
        mockInternalData(
            InternalData(
                pagination = Pagination(page = 1),
                state = SearchState(query = DEFAULT_QUERY)
            )
        )

        val aircraft = Aircraft()
        val data = SearchData(
            results = listOf(aircraft),
            pagination = Pagination(page = 1)
        )
        whenever(business.search(DEFAULT_QUERY, 1)).thenReturn(data)

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction.Retry)

        verify(business).search(DEFAULT_QUERY, 1)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(
                ViewState.Loading.FromPrevious(SearchState(query = DEFAULT_QUERY)),
                secondValue
            )
            assertEquals(
                ViewState.Success(
                    SearchState(
                        DEFAULT_QUERY,
                        listOf(result)
                    )
                ), thirdValue
            )

            assertEquals(data.pagination, internalData().pagination)
            assertEquals(
                SearchState(
                    query = DEFAULT_QUERY,
                    results = listOf(result)
                ),
                internalData().state
            )
        }
    }

    @Test
    fun `verify next page searched when more results interaction is handled`() = runBlockingTest {
        mockInternalData(
            InternalData(
                pagination = Pagination(page = 1, totalPages = 10),
                state = SearchState(query = DEFAULT_QUERY)
            )
        )

        val aircraft = Aircraft()
        val data = SearchData(
            results = listOf(aircraft),
            pagination = Pagination(page = 2, totalPages = 10)
        )
        whenever(business.search(DEFAULT_QUERY, 2)).thenReturn(data)

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction.More)

        verify(business).search(DEFAULT_QUERY, 2)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)

            val expectedState =
                SearchState(DEFAULT_QUERY, listOf(result))
            assertEquals(ViewState.Success(expectedState), secondValue)

            assertEquals(data.pagination, internalData().pagination)
            assertEquals(
                SearchState(
                    query = DEFAULT_QUERY,
                    results = listOf(result)
                ),
                internalData().state
            )
        }
    }

    @Test
    fun `verify next page not searched when more results interaction is handled without more pages`() =
        runBlockingTest {
            val data = InternalData(
                pagination = Pagination(page = 1, totalPages = 1),
                state = SearchState(query = DEFAULT_QUERY)
            )
            mockInternalData(data)

            viewModelHandler.handle(SearchInteraction.More)

            verify(business, never()).search(DEFAULT_QUERY, 2)
        }

    @Test
    fun `verify search reset when reset interaction is handled`() = runBlockingTest {
        val data = InternalData(
            pagination = Pagination(page = 1, totalPages = 10),
            state = SearchState(query = DEFAULT_QUERY)
        )
        mockInternalData(data)

        viewModelHandler.handle(SearchInteraction.Reset)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Initializing, secondValue)

            assertEquals(Pagination(), internalData().pagination)
            assertEquals(SearchState(), internalData().state)
        }
    }
}