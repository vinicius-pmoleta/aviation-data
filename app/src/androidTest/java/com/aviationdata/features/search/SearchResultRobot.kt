package com.aviationdata.features.search

import androidx.annotation.IdRes
import com.aviationdata.R
import com.aviationdata.core.robot.BaseRobot
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
        const val identification = R.id.search_result_identification
        const val model = R.id.search_result_model
        const val operation = R.id.search_result_operation
    }
}