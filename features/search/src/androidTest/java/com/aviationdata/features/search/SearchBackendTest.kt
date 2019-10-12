package com.aviationdata.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aviationdata.common.core.androidtest.base.launchFragmentTest
import com.aviationdata.common.core.androidtest.readFile
import com.aviationdata.common.core.androidtest.resources
import com.aviationdata.common.core.androidtest.rules.BackendRule
import com.aviationdata.common.core.androidtest.rules.DependencyOverrideRule
import com.aviationdata.common.core.dependencies.KodeinTags
import com.aviationdata.common.core.dependencies.modules.RetrofitBuilder
import com.aviationdata.features.search.view.SearchFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchBackendTest {

    @get:Rule
    val backendRule = BackendRule()

    @get:Rule
    val dependenciesRule = DependencyOverrideRule(searchComponent) {
        bind<Retrofit>(overrides = true, tag = KodeinTags.REMOTE_SOURCE_OPEN_SKY) with provider {
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

        launchFragmentTest<SearchFragment>()

        SearchRobot
            .writeQuery("747")
            .search()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "747")
        )

        SearchRobot
            .assertResultsDisplayedWithSize(25)
            .scrollResultsToPosition(24)

        backendRule.checkRequest(
            parameters = mapOf("p" to "2", "q" to "747")
        )

        SearchRobot
            .assertResultsDisplayedWithSize(50)
    }

    @Test
    fun verifyEmptyStateDisplayedWhenSearchIsCompletedWithoutResults() {
        backendRule
            .prepare(200, readFile("search_200_no_results.json"))

        launchFragmentTest<SearchFragment>()

        SearchRobot
            .writeQuery("Nothing")
            .search()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "Nothing")
        )

        SearchRobot
            .assertResultsDisplayedWithSize(0)
    }

    @Test
    fun verifyErrorMessageDisplayedWhenSearchFailsToComplete() {
        backendRule
            .prepare(500)
            .prepare(200, readFile("search_200_results_page_1.json"))

        launchFragmentTest<SearchFragment>()

        SearchRobot
            .writeQuery("747")
            .search()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "747")
        )

        SearchRobot
            .assertResultsHidden()
            .assertSnackbarText(resources().getString(R.string.search_error))
            .performSnackbarAction()

        backendRule.checkRequest(
            parameters = mapOf("p" to "1", "q" to "747")
        )

        SearchRobot
            .assertResultsDisplayedWithSize(25)
    }
}