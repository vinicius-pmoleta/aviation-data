package com.aviationdata.features.search

import android.widget.Button
import com.aviationdata.R
import com.aviationdata.core.robot.BaseRobot
import com.aviationdata.core.robot.common.LoadingRobot
import com.aviationdata.features.search.view.SearchResult
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
        assertToolbarTitle(SearchPage.toolbar, text)
        return this
    }

    fun assertInputFocused(): SearchRobot {
        assertFocused(SearchPage.input)
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
        assertDisplayed(SearchPage.results)
        assertListItemCount(SearchPage.results, size)
        return this
    }

    fun assertResultsHidden(): SearchRobot {
        assertNotDisplayed(SearchPage.results)
        return this
    }

    fun assertResults(results: List<SearchResult>): SearchRobot {
        for ((position, result) in results.withIndex()) {
            SearchResultRobot(SearchPage.results, position)
                .assertIdentification(result.identification)
                .assertModel(result.model)
                .assertOperation(result.operation)
        }
        return this
    }

    fun scrollResultsToPosition(position: Int): SearchRobot {
        scrollListToPosition(SearchPage.results, position)
        return this
    }

    fun writeQuery(query: String): SearchRobot {
        writeTo(SearchPage.input, query)
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
        const val toolbar = R.id.search_toolbar
        const val input = R.id.search_input
        const val results = R.id.search_results
    }
}