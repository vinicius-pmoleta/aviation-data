package com.aviationdata.features.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aviationdata.core.structure.Aircraft
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.data.SearchInteraction
import com.aviationdata.features.search.data.SearchResult
import com.aviationdata.features.search.data.SearchState
import com.aviationdata.rules.CoroutinesTestRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        viewModelHandler = SearchViewModel(business, mapper, coroutinesRule.dispatchers)
        viewModelHandler.state().observeForever(observer)
    }

    @Test
    fun `verify initialization emits first launch state`() {
        verify(observer).onChanged(ViewState.FirstLaunch)
    }

    @Test
    fun `verify search succeeds when search interaction is handled`() = runBlockingTest {
        val query = "query"

        val aircraft = Aircraft()
        whenever(business.search(query)).thenReturn(listOf(aircraft))

        val result = SearchResult()
        whenever(mapper.toPresentation(aircraft)).thenReturn(result)

        viewModelHandler.handle(SearchInteraction(query))

        verify(business).search(query)
        verify(mapper).toPresentation(aircraft)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.FirstLaunch, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(ViewState.Success(SearchState(query, listOf(result))), thirdValue)
        }
    }

    @Test
    fun `verify search fails when search interaction is handled`() = runBlockingTest {
        val query = "query"

        val reason = RuntimeException()
        whenever(business.search(query)).thenThrow(reason)

        viewModelHandler.handle(SearchInteraction(query))

        verify(business).search(query)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.FirstLaunch, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(ViewState.Failed(reason), thirdValue)
        }
    }
}