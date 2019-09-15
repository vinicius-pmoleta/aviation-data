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
    fun verifyScreenStateWhenIsFirstLaunch() {
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
    fun verifyScreenStateWhenOperationSucceeded() {
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
    fun verifyScreenStateWhenOperationFailed() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Failed(reason = Throwable()))

        SearchRobot
            .assertTitle(resources().getString(R.string.search_screen_title))
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(0)
            .assertSnackbarText(resources().getString(R.string.search_error))
    }

    @Test
    fun verifyActionTriggeredWhenSearchSubmitted() {
        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        liveState.postValue(ViewState.Initializing)

        val query = "Test"
        SearchRobot.searchFor(query)

        verify(viewModelHandler).handle(SearchInteraction.Search(query))
    }
}