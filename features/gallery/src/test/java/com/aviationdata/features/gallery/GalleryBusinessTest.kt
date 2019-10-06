package com.aviationdata.features.gallery

import com.aviationdata.features.gallery.business.GalleryBusiness
import com.aviationdata.features.gallery.business.PhotoData
import com.aviationdata.features.gallery.data.GalleryRemoteRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

@ExperimentalCoroutinesApi
class GalleryBusinessTest {

    private val remoteRepository: GalleryRemoteRepository = mock()
    private val business = GalleryBusiness(remoteRepository)

    @Test
    fun `verify load is performed on remote repository`() = runBlockingTest {
        val registration = "registration"

        val data = listOf(
            PhotoData(
                url = "url",
                photographer = "photographer",
                notes = "notes"
            )
        )
        whenever(remoteRepository.loadPhotosFor(registration)).thenReturn(data)

        val results = business.loadPhotosFor(registration)

        assertNotNull(results)
        assertEquals(data, results)
    }
}