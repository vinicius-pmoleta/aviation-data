package com.aviationdata.features.search

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.core.rules.DependencyOverrideRule
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.features.search.view.SearchActivity
import com.aviationdata.features.search.view.SearchState
import com.nhaarman.mockitokotlin2.mock
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

    private val activityRule = ActivityTestRule(SearchActivity::class.java)

    private val viewModelHandler: ViewModelHandler<SearchState> = mock()
    private val state: MutableLiveData<ViewState<SearchState>> = mock()

    @Before
    fun setup() {
        whenever(viewModelHandler.state()).thenReturn(state)
    }

    @Test
    fun verifyScreenStateWhenIsFirstLaunch() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun verifyScreenStateWhenIsLoading() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun verifyScreenStateWhenOperationSucceeded() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun verifyScreenStateWhenOperationFailed() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun verifyActionTriggeredWhenSearchSubmitted() {
        activityRule.launchActivity(Intent())
    }
}