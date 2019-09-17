package com.aviationdata.features.search

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.R
import com.aviationdata.core.dependencies.KodeinTags
import com.aviationdata.core.readFile
import com.aviationdata.core.resources
import com.aviationdata.core.rules.BackendRule
import com.aviationdata.core.rules.DependencyOverrideRule
import com.aviationdata.features.search.view.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFlowTest {

    @get:Rule
    val backendRule = BackendRule()

    @get:Rule
    val dependenciesRule = DependencyOverrideRule {
        bind<String>(
            tag = KodeinTags.BASE_URL,
            overrides = true
        ) with provider { backendRule.baseUrl }
    }

    @Test
    fun verifyResultsDisplayedWhenSearchIsCompletedWithResults() {
        backendRule
            .prepare(200, readFile("search_200_results_page_1.json"))
            .prepare(200, readFile("search_200_results_page_2.json"))

        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        SearchRobot
            .writeQuery("747")
            .search()
        SearchRobot
            .assertResultsDisplayedWithSize(25)
            .scrollResultsToPosition(24)
        SearchRobot
            .assertResultsDisplayedWithSize(50)
    }

    @Test
    fun verifyEmptyStateDisplayedWhenSearchIsCompletedWithoutResults() {
        backendRule
            .prepare(200, readFile("search_200_no_results.json"))

        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        SearchRobot
            .writeQuery("Nothing")
            .search()
        SearchRobot
            .assertResultsDisplayedWithSize(0)
    }

    @Test
    fun verifyErrorMessageDisplayedWhenSearchFailsToComplete() {
        backendRule
            .prepare(500)
            .prepare(200, readFile("search_200_results_page_1.json"))

        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        SearchRobot
            .writeQuery("Nothing")
            .search()
//        backendRule.check("GET", mapOf("p" to "1"))
        SearchRobot
            .assertSnackbarText(resources().getString(R.string.search_error))
            .performSnackbarAction()
//        backendRule.check("GET", mapOf("p" to "1"))
        SearchRobot
            .assertResultsDisplayedWithSize(25)
    }
}