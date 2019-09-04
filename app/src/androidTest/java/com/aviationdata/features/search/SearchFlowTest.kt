package com.aviationdata.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.features.search.view.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFlowTest {

    @get:Rule
    val activityRule = ActivityTestRule(SearchActivity::class.java)

    @Test
    fun verifyResultsDisplayedWhenSearchIsCompletedWithResults() {
    }

    @Test
    fun verifyEmptyStateDisplayedWhenSearchIsCompletedWithoutResults() {
    }

    @Test
    fun verifyErrorMessageDisplayedWhenSearchFailsToComplete() {
    }
}