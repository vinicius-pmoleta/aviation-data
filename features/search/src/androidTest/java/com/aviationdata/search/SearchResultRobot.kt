package com.aviationdata.search

import androidx.annotation.IdRes
import com.aviationdata.core.androidtest.robot.BaseRobot
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition

class SearchResultRobot(
    @IdRes private val container: Int,
    private val position: Int
) : BaseRobot() {

    fun assertIdentification(identification: String): SearchResultRobot {
        assertDisplayedAtPosition(
            container,
            position,
            SearchResultPage.identification,
            identification
        )
        return this
    }

    fun assertModel(model: String): SearchResultRobot {
        assertDisplayedAtPosition(
            container,
            position,
            SearchResultPage.model,
            model
        )
        return this
    }

    fun assertOperation(operation: String): SearchResultRobot {
        assertDisplayedAtPosition(
            container,
            position,
            SearchResultPage.operation,
            operation
        )
        return this
    }

    private object SearchResultPage {
        val identification = R.id.search_result_identification
        val model = R.id.search_result_model
        val operation = R.id.search_result_operation
    }
}