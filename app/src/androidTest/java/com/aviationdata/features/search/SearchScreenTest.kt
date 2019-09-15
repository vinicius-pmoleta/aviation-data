package com.aviationdata.features.search

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.R
import com.aviationdata.core.resources
import com.aviationdata.core.rules.DependencyOverrideRule
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.view.SearchActivity
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
    val dependenciesRule = DependencyOverrideRule {
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
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Initializing)

        SearchRobot.assertInputFocused()
    }

    @Test
    fun verifyScreenStateWhenIsLoading() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Loading.FromEmpty)

        SearchRobot
            .assertTitle(resources().getString(R.string.search_screen_title_loading))
            .assertLoadingDisplayed()
            .assertResultsHidden()
    }

    @Test
    fun verifyScreenStateWhenSearchSucceeded() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        val query = "Test"
        val results = listOf(
            SearchResult("identification1", "operation1", "model1"),
            SearchResult("identification2", "operation2", "model2"),
            SearchResult("identification3", "operation3", "model3")
        )
        val searchState = SearchState(query = query, results = results)

        liveState.postValue(ViewState.Success(searchState))

        SearchRobot
            .assertTitle(resources().getString(R.string.search_screen_title_with_query, query))
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(results.size)
            .assertResults(results)
    }

    @Test
    fun verifyScreenStateWhenSearchFailedAndRetry() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Failed(reason = Throwable()))

        SearchRobot
            .assertTitle(resources().getString(R.string.search_screen_title))
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(0)
            .assertSnackbarText(resources().getString(R.string.search_error))
            .assertSnackbarAction(resources().getString(R.string.default_retry_action))
            .performSnackbarAction()

        verify(viewModelHandler).handle(SearchInteraction.Retry)
    }

    @Test
    fun verifyScreenStateWhenReachPageResultsEnd() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        val query = "Test"
        val results = mutableListOf<SearchResult>()
        repeat(20) {
            results.add(SearchResult("identification$it", "operation$it", "model$it"))
        }
        val searchState = SearchState(query = query, results = results)

        liveState.postValue(ViewState.Success(searchState))

        SearchRobot
            .assertTitle(resources().getString(R.string.search_screen_title_with_query, query))
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(results.size)
            .assertResults(results)
            .scrollResultsToPosition(results.size)

        verify(viewModelHandler).handle(SearchInteraction.More)
    }

    @Test
    fun verifyScreenStateWhenSearchReset() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Initializing)

        SearchRobot
            .writeQuery("Test")
            .resetSearch()

        verify(viewModelHandler, times(2)).handle(SearchInteraction.Reset)
    }

    @Test
    fun verifyActionTriggeredWhenSearchSubmitted() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Initializing)

        val query = "Test"
        SearchRobot
            .writeQuery(query)
            .search()

        verify(viewModelHandler).handle(SearchInteraction.Search(query))
    }

    @Test
    fun verifyActionTriggeredWhenSearchReset() {
    }

    @Test
    fun verifyActionTriggeredWhenSearchResultsReachEndPageResults() {
    }
}