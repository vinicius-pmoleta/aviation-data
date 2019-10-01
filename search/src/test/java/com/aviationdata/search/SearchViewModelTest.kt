package com.aviationdata.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aviationdata.core.test.CoroutinesTestRule
import com.aviationdata.core.structure.Aircraft
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.nhaarman.mockitokotlin2.*
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

    private val business: com.aviationdata.search.business.SearchBusiness = mock()
    private val mapper: com.aviationdata.search.viewmodel.SearchMapper = mock()
    private val observer: Observer<ViewState<com.aviationdata.search.view.SearchState>> = mock()

    private lateinit var viewModelHandler: ViewModelHandler<com.aviationdata.search.view.SearchState>

    @Before
    fun setUp() {
        viewModelHandler = com.aviationdata.search.viewmodel.SearchViewModel(
            business,
            mapper,
            coroutinesRule.dispatchers
        )
        viewModelHandler.liveState().observeForever(observer)
    }

    private fun internalData() =
        (viewModelHandler as com.aviationdata.search.viewmodel.SearchViewModel).internalData

    private fun mockInternalData(data: com.aviationdata.search.viewmodel.InternalData) {
        (viewModelHandler as com.aviationdata.search.viewmodel.SearchViewModel).internalData = data
    }

    @Test
    fun `verify initialization emits first launch state`() {
        verify(observer).onChanged(ViewState.Initializing)
    }

    @Test
    fun `verify search succeeds when search interaction is handled`() = runBlockingTest {
        val aircraft = Aircraft()
        val data = com.aviationdata.search.business.SearchData(
            results = listOf(aircraft),
            pagination = com.aviationdata.search.business.Pagination(page = 2, totalPages = 10)
        )
        whenever(business.search(DEFAULT_QUERY, 1)).thenReturn(data)

        val result = com.aviationdata.search.view.SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(com.aviationdata.search.view.SearchInteraction.Search(DEFAULT_QUERY))

        verify(business).search(DEFAULT_QUERY)
        verify(mapper).toPresentation(aircraft)

        argumentCaptor<ViewState<com.aviationdata.search.view.SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)

            val state = com.aviationdata.search.view.SearchState(DEFAULT_QUERY, listOf(result))
            assertEquals(ViewState.Success(state), thirdValue)

            assertEquals(data.pagination, internalData().pagination)
            assertEquals(state, internalData().state)
        }
    }

    @Test
    fun `verify search fails when search interaction is handled`() = runBlockingTest {
        mockInternalData(
            com.aviationdata.search.viewmodel.InternalData(
                pagination = com.aviationdata.search.business.Pagination(page = 1),
                state = com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY)
            )
        )

        val reason = RuntimeException()
        whenever(business.search(DEFAULT_QUERY, 1)).thenThrow(reason)

        viewModelHandler.handle(com.aviationdata.search.view.SearchInteraction.Search(DEFAULT_QUERY))

        verify(business).search(DEFAULT_QUERY, 1)

        argumentCaptor<ViewState<com.aviationdata.search.view.SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(ViewState.Failed(reason), thirdValue)

            assertEquals(
                com.aviationdata.search.business.Pagination(page = 1),
                internalData().pagination
            )
            assertEquals(
                com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY),
                internalData().state
            )
        }
    }

    @Test
    fun `verify search executed when retry interaction is handled`() = runBlockingTest {
        mockInternalData(
            com.aviationdata.search.viewmodel.InternalData(
                pagination = com.aviationdata.search.business.Pagination(page = 1),
                state = com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY)
            )
        )

        val aircraft = Aircraft()
        val data = com.aviationdata.search.business.SearchData(
            results = listOf(aircraft),
            pagination = com.aviationdata.search.business.Pagination(page = 1)
        )
        whenever(business.search(DEFAULT_QUERY, 1)).thenReturn(data)

        val result = com.aviationdata.search.view.SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(com.aviationdata.search.view.SearchInteraction.Retry)

        verify(business).search(DEFAULT_QUERY, 1)

        argumentCaptor<ViewState<com.aviationdata.search.view.SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(
                ViewState.Loading.FromPrevious(com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY)),
                secondValue
            )
            assertEquals(
                ViewState.Success(
                    com.aviationdata.search.view.SearchState(
                        DEFAULT_QUERY,
                        listOf(result)
                    )
                ), thirdValue
            )

            assertEquals(data.pagination, internalData().pagination)
            assertEquals(
                com.aviationdata.search.view.SearchState(
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
            com.aviationdata.search.viewmodel.InternalData(
                pagination = com.aviationdata.search.business.Pagination(page = 1, totalPages = 10),
                state = com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY)
            )
        )

        val aircraft = Aircraft()
        val data = com.aviationdata.search.business.SearchData(
            results = listOf(aircraft),
            pagination = com.aviationdata.search.business.Pagination(page = 2, totalPages = 10)
        )
        whenever(business.search(DEFAULT_QUERY, 2)).thenReturn(data)

        val result = com.aviationdata.search.view.SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(com.aviationdata.search.view.SearchInteraction.More)

        verify(business).search(DEFAULT_QUERY, 2)

        argumentCaptor<ViewState<com.aviationdata.search.view.SearchState>> {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)

            val expectedState =
                com.aviationdata.search.view.SearchState(DEFAULT_QUERY, listOf(result))
            assertEquals(ViewState.Success(expectedState), secondValue)

            assertEquals(data.pagination, internalData().pagination)
            assertEquals(
                com.aviationdata.search.view.SearchState(
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
            val data = com.aviationdata.search.viewmodel.InternalData(
                pagination = com.aviationdata.search.business.Pagination(page = 1, totalPages = 1),
                state = com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY)
            )
            mockInternalData(data)

            viewModelHandler.handle(com.aviationdata.search.view.SearchInteraction.More)

            verify(business, never()).search(DEFAULT_QUERY, 2)
        }

    @Test
    fun `verify search reset when reset interaction is handled`() = runBlockingTest {
        val data = com.aviationdata.search.viewmodel.InternalData(
            pagination = com.aviationdata.search.business.Pagination(page = 1, totalPages = 10),
            state = com.aviationdata.search.view.SearchState(query = DEFAULT_QUERY)
        )
        mockInternalData(data)

        viewModelHandler.handle(com.aviationdata.search.view.SearchInteraction.Reset)

        argumentCaptor<ViewState<com.aviationdata.search.view.SearchState>> {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Initializing, secondValue)

            assertEquals(com.aviationdata.search.business.Pagination(), internalData().pagination)
            assertEquals(com.aviationdata.search.view.SearchState(), internalData().state)
        }
    }
}