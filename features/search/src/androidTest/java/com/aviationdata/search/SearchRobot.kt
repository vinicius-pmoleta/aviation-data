package com.aviationdata.search

import com.aviationdata.core.androidtest.robot.BaseRobot
import com.aviationdata.core.androidtest.robot.common.LoadingRobot
import com.aviationdata.core.androidtest.waitForView
import com.google.android.material.internal.CheckableImageButton
import com.schibsted.spain.barista.assertion.BaristaAssertions.assertAny
import com.schibsted.spain.barista.assertion.BaristaFocusedAssertions.assertFocused
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.pressImeActionButton
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition

object SearchRobot : BaseRobot() {

    fun assertTitle(text: String): SearchRobot {
        assertToolbarTitle(SearchRobot.SearchPage.toolbar, text)
        return this
    }

    fun assertInputFocused(): SearchRobot {
        assertFocused(SearchRobot.SearchPage.input)
        return this
    }

    fun assertLoadingDisplayed(): SearchRobot {
        LoadingRobot.assertLabelDisplayed()
        return this
    }

    fun assertLoadingHidden(): SearchRobot {
        LoadingRobot.assertLabelHidden()
        return this
    }

    fun assertResultsDisplayedWithSize(size: Int): SearchRobot {
        waitForView(SearchRobot.SearchPage.results) {
            assertDisplayed(SearchRobot.SearchPage.results)
            assertListItemCount(SearchRobot.SearchPage.results, size)
        }
        return this
    }

    fun assertResultsHidden(): SearchRobot {
        waitForView(SearchRobot.SearchPage.results) {
            assertNotDisplayed(SearchRobot.SearchPage.results)
        }
        return this
    }

    fun assertResults(results: List<com.aviationdata.search.view.SearchResult>): SearchRobot {
        waitForView(SearchRobot.SearchPage.results) {
            for ((position, result) in results.withIndex()) {
                SearchResultRobot(
                    SearchRobot.SearchPage.results,
                    position
                )
                    .assertIdentification(result.identification)
                    .assertModel(result.model)
                    .assertOperation(result.operation)
            }
        }
        return this
    }

    fun scrollResultsToPosition(position: Int): SearchRobot {
        waitForView(SearchRobot.SearchPage.results) {
            scrollListToPosition(SearchRobot.SearchPage.results, position)
        }
        return this
    }

    fun writeQuery(query: String): SearchRobot {
        waitForView(SearchRobot.SearchPage.input) {
            writeTo(SearchRobot.SearchPage.input, query)
        }
        return this
    }

    fun search(): SearchRobot {
        pressImeActionButton()
        return this
    }

    fun resetSearch(): SearchRobot {
        assertAny<CheckableImageButton>(com.google.android.material.R.id.text_input_end_icon) {
            it.performClick()
        }
        return this
    }

    private object SearchPage {
        val toolbar = R.id.search_toolbar
        val input = R.id.search_input
        val results = R.id.search_results
    }
}