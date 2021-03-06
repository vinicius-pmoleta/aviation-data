package com.aviationdata.features.search

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aviationdata.common.core.R as coreR
import com.aviationdata.common.core.androidtest.base.launchFragmentTest
import com.aviationdata.common.core.androidtest.resources
import com.aviationdata.common.core.androidtest.rules.DependencyOverrideRule
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.common.core.structure.ViewState
import com.aviationdata.features.search.view.SearchFragment
import com.aviationdata.features.search.view.SearchInteraction
import com.aviationdata.features.search.view.SearchResult
import com.aviationdata.features.search.view.SearchState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchScreenTest {

    @get:Rule
    val dependenciesRule = DependencyOverrideRule(searchComponent) {
        bind<ViewModelHandler<SearchState>>(overrides = true) with provider { viewModelHandler }
    }

    private val viewModelHandler: ViewModelHandler<SearchState> = mock()
    private val liveState = MutableLiveData<ViewState<SearchState>>()

    @Before
    fun setup() {
        whenever(viewModelHandler.liveState()).thenReturn(liveState)
    }

    @Test
    fun verifyScreenStateWhenIsInitializing() {
        launchFragmentTest<SearchFragment>()

        liveState.postValue(ViewState.Initializing)

        SearchRobot.assertInputFocused()
    }

    @Test
    fun verifyScreenStateWhenIsLoading() {
        launchFragmentTest<SearchFragment>()

        liveState.postValue(ViewState.Loading.FromEmpty)

        SearchRobot
            .assertLoadingDisplayed()
            .assertResultsHidden()
    }

    @Test
    fun verifyScreenStateWhenSearchSucceeded() {
        launchFragmentTest<SearchFragment>()

        val query = "Test"
        val results = generateMockResults(3)
        val searchState = SearchState(query = query, results = results)

        liveState.postValue(ViewState.Success(searchState))

        SearchRobot
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(results.size)
            .assertResults(results)
    }

    @Test
    fun verifyScreenStateWhenSearchFailedAndRetry() {
        launchFragmentTest<SearchFragment>()

        liveState.postValue(ViewState.Failed(reason = Throwable()))

        SearchRobot
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(0)
            .assertSnackbarText(resources().getString(R.string.search_error))
            .assertSnackbarAction(resources().getString(coreR.string.default_retry_action))
            .performSnackbarAction()

        verify(viewModelHandler).handle(SearchInteraction.Retry)
    }

    @Test
    fun verifyScreenStateWhenReachPageResultsEnd() {
        launchFragmentTest<SearchFragment>()

        val query = "Test"
        val results = generateMockResults(20)
        val searchState = SearchState(query = query, results = results)

        liveState.postValue(ViewState.Success(searchState))

        SearchRobot
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(results.size)
            .assertResults(results)
            .scrollResultsToPosition(results.size)

        verify(viewModelHandler).handle(SearchInteraction.More)
    }

    @Test
    fun verifyScreenStateWhenSearchReset() {
        launchFragmentTest<SearchFragment>()

        liveState.postValue(ViewState.Initializing)

        SearchRobot
            .writeQuery("Test")
            .resetSearch()

        verify(viewModelHandler, times(2)).handle(SearchInteraction.Reset)
    }

    @Test
    fun verifyActionTriggeredWhenSearchSubmitted() {
        launchFragmentTest<SearchFragment>()

        liveState.postValue(ViewState.Initializing)

        val query = "Test"
        SearchRobot
            .writeQuery(query)
            .search()

        verify(viewModelHandler).handle(SearchInteraction.Search(query))
    }

    private fun generateMockResults(size: Int): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        repeat(size) {
            results.add(SearchResult("identification$it", "operation$it", "model$it"))
        }
        return results
    }
}