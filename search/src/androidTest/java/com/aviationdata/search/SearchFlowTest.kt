package com.aviationdata.search

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.aviationdata.core.androidtest.readFile
import com.aviationdata.core.androidtest.resources
import com.aviationdata.core.androidtest.rules.BackendRule
import com.aviationdata.core.androidtest.rules.DependencyOverrideRule
import com.aviationdata.core.dependencies.modules.RetrofitBuilder
import com.aviationdata.search.view.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFlowTest {

    @get:Rule
    val backendRule = BackendRule()

    @get:Rule
    val dependenciesRule = DependencyOverrideRule {
        bind<Retrofit>(overrides = true) with provider {
            RetrofitBuilder.build(
                url = backendRule.baseUrl,
                client = instance()
            )
        }
    }

    @Test
    fun verifyResultsDisplayedWhenSearchIsCompletedWithResults() {
        backendRule
            .prepare(200, readFile("search_200_results_page_1.json"))
            .prepare(200, readFile("search_200_results_page_2.json"))

        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        SearchRobot.search()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "747")
        )

        SearchRobot.scrollResultsToPosition(24)

        backendRule.checkRequest(
            parameters = mapOf("p" to "2", "q" to "747")
        )

        SearchRobot.assertResultsDisplayedWithSize(50)
    }

    @Test
    fun verifyEmptyStateDisplayedWhenSearchIsCompletedWithoutResults() {
        backendRule
            .prepare(200, readFile("search_200_no_results.json"))

        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        SearchRobot.search()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "Nothing")
        )

        SearchRobot.assertResultsDisplayedWithSize(0)
    }

    @Test
    fun verifyErrorMessageDisplayedWhenSearchFailsToComplete() {
        backendRule
            .prepare(500)
            .prepare(200, readFile("search_200_results_page_1.json"))

        ActivityTestRule(SearchActivity::class.java).launchActivity(Intent())

        SearchRobot.search()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "747")
        )

        SearchRobot
            .assertSnackbarText(resources().getString(R.string.search_error))
            .performSnackbarAction()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "747")
        )

        SearchRobot.assertResultsDisplayedWithSize(25)
    }
}