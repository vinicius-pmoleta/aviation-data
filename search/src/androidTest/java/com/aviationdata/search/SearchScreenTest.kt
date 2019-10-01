package com.aviationdata.search

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.core.androidtest.resources
import com.aviationdata.core.androidtest.rules.DependencyOverrideRule
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.search.view.SearchActivity
import com.aviationdata.search.view.SearchInteraction
import com.aviationdata.search.view.SearchResult
import com.aviationdata.search.view.SearchState
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
import com.aviationdata.core.R as coreR

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

        SearchRobot.assertResultsHidden()
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

        SearchRobot.assertResults(results)
    }

    @Test
    fun verifyScreenStateWhenSearchFailedAndRetry() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Failed(reason = Throwable()))

        SearchRobot.assertResultsDisplayedWithSize(0)
            .assertSnackbarText(resources().getString(R.string.search_error))
            .assertSnackbarAction(resources().getString(coreR.string.default_retry_action))
            .performSnackbarAction()

        verify(viewModelHandler).handle(SearchInteraction.Retry)
    }

    @Test
    fun verifyScreenStateWhenReachPageResultsEnd() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        val query = "Test"
        val results = mutableListOf<SearchResult>()
        repeat(20) {
            results.add(
                SearchResult(
                    "identification$it",
                    "operation$it",
                    "model$it"
                )
            )
        }
        val searchState = SearchState(query = query, results = results)

        liveState.postValue(ViewState.Success(searchState))

        SearchRobot.scrollResultsToPosition(results.size)

        verify(viewModelHandler).handle(SearchInteraction.More)
    }

    @Test
    fun verifyScreenStateWhenSearchReset() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Initializing)

        SearchRobot.resetSearch()

        verify(
            viewModelHandler,
            times(2)
        ).handle(SearchInteraction.Reset)
    }

    @Test
    fun verifyActionTriggeredWhenSearchSubmitted() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(
            Intent()
        )

        liveState.postValue(ViewState.Initializing)

        val query = "Test"
        SearchRobot.search()

        verify(viewModelHandler).handle(SearchInteraction.Search(query))
    }

    @Test
    fun verifyActionTriggeredWhenSearchReset() {
    }

    @Test
    fun verifyActionTriggeredWhenSearchResultsReachEndPageResults() {
    }
}