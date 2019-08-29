package com.aviationdata.features.search

import android.content.Context
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
    private val observer: Observer<ViewState<SearchState>> = mock()

    private lateinit var viewModelHandler: ViewModelHandler<SearchState>

    @Before
    fun setUp() {
        viewModelHandler = SearchViewModel(business)
        viewModelHandler.state().observeForever(observer)
    }

    @Test
    fun `verify initialization emits first launch state`() {
        verify(observer).onChanged(ViewState.FirstLaunch)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `verify search triggered when search interaction is handled`() = runBlockingTest {
        val context = mock<Context>()
        val query = "query"
        val aircraft = Aircraft()
        whenever(business.search(query)).thenReturn(listOf(aircraft))

        viewModelHandler.handle(SearchInteraction(context, query))

        verify(business).search(query)

        argumentCaptor<ViewState<SearchState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.FirstLaunch, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(
                ViewState.Success(SearchState(query, listOf(SearchResult()))),
                thirdValue
            )
        }
    }
}