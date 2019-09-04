package com.aviationdata.features.search

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.features.search.view.SearchActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchScreenTest {

    @get:Rule
    val activityRule = ActivityTestRule(SearchActivity::class.java)

    @Before
    fun setup() {
    }

    @Test
    fun verifyScreenStateWhenIsFirstLaunch() {
        activityRule.launchActivity(Intent())
    }

    @Test
    fun verifyScreenStateWhenIsLoading() {
    }

    @Test
    fun verifyScreenStateWhenOperationSucceeded() {
    }

    @Test
    fun verifyScreenStateWhenOperationFailed() {
    }

    @Test
    fun verifyActionTriggeredWhenSearchSubmitted() {
    }
}