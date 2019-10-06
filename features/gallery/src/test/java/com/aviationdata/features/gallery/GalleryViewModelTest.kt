package com.aviationdata.features.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.core.test.CoroutinesTestRule
import com.aviationdata.features.gallery.business.GalleryBusiness
import com.aviationdata.features.gallery.business.PhotoData
import com.aviationdata.features.gallery.view.GalleryInteraction
import com.aviationdata.features.gallery.view.GalleryState
import com.aviationdata.features.gallery.view.PhotoResult
import com.aviationdata.features.gallery.viewmodel.GalleryMapper
import com.aviationdata.features.gallery.viewmodel.GalleryViewModel
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val DEFAULT_REGISTRATION = "registration"

@ExperimentalCoroutinesApi
class GalleryViewModelTest {

    @get:Rule
    val executionRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    private val business: GalleryBusiness = mock()
    private val mapper: GalleryMapper = mock()
    private val observer: Observer<ViewState<GalleryState>> = mock()

    private lateinit var viewModelHandler: ViewModelHandler<GalleryState>

    @Before
    fun setUp() {
        viewModelHandler = GalleryViewModel(
            business,
            mapper,
            coroutinesRule.dispatchers
        )
        viewModelHandler.liveState().observeForever(observer)
    }

    @Test
    fun `verify initialization emits first launch state`() {
        verify(observer).onChanged(ViewState.Initializing)
    }

    @Test
    fun `verify load succeeds when loading interaction is handled`() = runBlockingTest {
        val data = PhotoData(url = "url", photographer = "photographer", notes = "notes")
        whenever(business.loadPhotosFor(DEFAULT_REGISTRATION)).thenReturn(listOf(data))

        val result = PhotoResult(url = "url", photographer = "photographer", notes = "notes")
        whenever(mapper.toPresentation(data)).thenReturn(result)

        viewModelHandler.handle(GalleryInteraction.Load(DEFAULT_REGISTRATION))

        verify(business).loadPhotosFor(DEFAULT_REGISTRATION)
        verify(mapper).toPresentation(data)

        argumentCaptor<ViewState<GalleryState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)

            val state = GalleryState(DEFAULT_REGISTRATION, listOf(result))
            assertEquals(ViewState.Success(state), thirdValue)
        }
    }

    @Test
    fun `verify search fails when search interaction is handled`() = runBlockingTest {
        val reason = RuntimeException()
        whenever(business.loadPhotosFor(DEFAULT_REGISTRATION)).thenThrow(reason)

        viewModelHandler.handle(GalleryInteraction.Load(DEFAULT_REGISTRATION))

        verify(business).loadPhotosFor(DEFAULT_REGISTRATION)

        argumentCaptor<ViewState<GalleryState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)
            assertEquals(ViewState.Failed(reason), thirdValue)
        }
    }

    @Test
    fun `verify search executed when retry interaction is handled`() = runBlockingTest {
        val data = PhotoData(url = "url", photographer = "photographer", notes = "notes")
        whenever(business.loadPhotosFor(DEFAULT_REGISTRATION)).thenReturn(listOf(data))

        val result = PhotoResult(url = "url", photographer = "photographer", notes = "notes")
        whenever(mapper.toPresentation(data)).thenReturn(result)

        viewModelHandler.handle(GalleryInteraction.Retry(DEFAULT_REGISTRATION))

        verify(business).loadPhotosFor(DEFAULT_REGISTRATION)
        verify(mapper).toPresentation(data)

        argumentCaptor<ViewState<GalleryState>> {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(ViewState.Initializing, firstValue)
            assertEquals(ViewState.Loading.FromEmpty, secondValue)

            val state = GalleryState(DEFAULT_REGISTRATION, listOf(result))
            assertEquals(ViewState.Success(state), thirdValue)
        }
    }
}