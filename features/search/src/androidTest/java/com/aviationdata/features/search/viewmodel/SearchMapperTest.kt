package com.aviationdata.features.search.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.aviationdata.features.search.business.Aircraft
import com.aviationdata.features.search.business.Identification
import com.aviationdata.features.search.business.Information
import com.aviationdata.features.search.business.Operation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchMapperTest {

    private val mapper = SearchMapper(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test
    fun verifyIdentificationMappedToPlaceHolderWhenValuesNotAvailable() {
        val aircraft = Aircraft(identification = Identification())
        val result = mapper.toPresentation(aircraft)
        assertEquals("-", result.identification)
    }

    @Test
    fun verifyIdentificationMappedWhenRegistrationAvailable() {
        val aircraft = Aircraft(
            identification = Identification(
                icao24 = "icao24",
                registration = "registration"
            )
        )
        val result = mapper.toPresentation(aircraft)
        assertEquals("registration", result.identification)
    }

    @Test
    fun verifyIdentificationMappedWhenOnlyICAOAvailable() {
        val aircraft = Aircraft(
            identification = Identification(
                icao24 = "icao24"
            )
        )
        val result = mapper.toPresentation(aircraft)
        assertEquals("icao24", result.identification)
    }

    @Test
    fun verifyOperationMappedToPlaceHolderWhenValuesNotAvailable() {
        val aircraft = Aircraft(operation = Operation())
        val result = mapper.toPresentation(aircraft)
        assertEquals("Operated in - by -", result.operation)
    }

    @Test
    fun verifyOperationMappedWhenValuesAvailable() {
        val aircraft = Aircraft(
            operation = Operation(
                operator = "operator",
                country = "country"
            )
        )
        val result = mapper.toPresentation(aircraft)
        assertEquals("Operated in country by operator", result.operation)
    }

    @Test
    fun verifyModelMappedWhenValueNotAvailable() {
        val aircraft = Aircraft(
            information = Information()
        )
        val result = mapper.toPresentation(aircraft)
        assertEquals("", result.model)
    }

    @Test
    fun verifyModelMappedWhenValueAvailable() {
        val aircraft = Aircraft(
            information = Information(
                model = "model"
            )
        )
        val result = mapper.toPresentation(aircraft)
        assertEquals("model", result.model)
    }
}