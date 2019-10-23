package com.aviationdata.features.gallery

import com.aviationdata.features.gallery.business.PhotoData
import com.aviationdata.features.gallery.data.DEFAULT_PHOTOS_LIMIT
import com.aviationdata.features.gallery.data.GalleryRemoteRepository
import com.aviationdata.features.gallery.data.GalleryService
import com.aviationdata.features.gallery.data.RawPhotoResponse
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class GalleryRemoteRepositoryTest {

    private val service: GalleryService = mock()
    private val repository = GalleryRemoteRepository(service)

    @Test
    fun `verify service empty raw models converted to empty domain models`() = runBlockingTest {
        val registration = "registration"
        mockGalleryService(registration, mockPhotoRawModels())

        val data = repository.loadPhotosFor(registration)

        assertNotNull(data)
        assertTrue(data.isEmpty())
    }

    @Test
    fun `verify service results are converted from raw to domain models`() = runBlockingTest {
        val registration = "registration"
        val rawResponse = mockPhotoRawModels(1)
        mockGalleryService(registration, rawResponse)

        val data = repository.loadPhotosFor(registration)

        assertNotNull(data)
        assertEquals(1, data.size)
        assertRawToDomainConversion(rawResponse.first(), data.first())
    }

    private suspend fun mockGalleryService(
        registration: String,
        rawResponse: List<RawPhotoResponse>
    ) {
        whenever(service.photos(registration, DEFAULT_PHOTOS_LIMIT)).thenReturn(rawResponse)
    }

    private fun mockPhotoRawModels(size: Int = 0): List<RawPhotoResponse> {
        val rawResults = mutableListOf<RawPhotoResponse>()
        repeat(size) {
            rawResults.add(
                RawPhotoResponse(
                    url = "url$it",
                    photographer = "photographer$it",
                    notes = "notes$it"
                )
            )
        }
        return rawResults
    }

    private fun assertRawToDomainConversion(raw: RawPhotoResponse, domain: PhotoData) {
        assertEquals(raw.url, domain.url)
        assertEquals(raw.photographer, domain.photographer)
        assertEquals(raw.notes, domain.notes)
    }
}