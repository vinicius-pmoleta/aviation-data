package com.aviationdata.features.gallery.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.aviationdata.core.R
import com.aviationdata.core.androidtest.resources
import com.aviationdata.features.gallery.business.PhotoData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class GalleryMapperTest {

    private val mapper = GalleryMapper(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test
    fun verifyValuesMappedToPlaceHolderWhenValuesNotAvailable() {
        val data = PhotoData()
        val result = mapper.toPresentation(data)

        assertNotNull(result)
        assertEquals("", result.url)
        assertEquals(resources().getString(R.string.default_no_value_placeholder), result.notes)
        assertEquals(
            resources().getString(R.string.default_no_value_placeholder),
            result.photographer
        )
    }

    @Test
    fun verifyIdentificationMappedWhenValuesAvailable() {
        val data = PhotoData(url = "url", notes = "notes", photographer = "photographer")
        val result = mapper.toPresentation(data)
        assertEquals(data.url, result.url)
        assertEquals(data.notes, result.notes)
        assertEquals(data.photographer, result.photographer)
    }
}