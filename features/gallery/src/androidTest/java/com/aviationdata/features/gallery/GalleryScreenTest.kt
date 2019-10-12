package com.aviationdata.features.gallery

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aviationdata.common.core.androidtest.base.launchParameterizedFragmentTest
import com.aviationdata.common.core.androidtest.resources
import com.aviationdata.common.core.androidtest.rules.DependencyOverrideRule
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.common.core.structure.ViewState
import com.aviationdata.features.gallery.view.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import com.aviationdata.common.core.R as coreR

private const val DEFAULT_REGISTRATION = "registration"

@RunWith(AndroidJUnit4::class)
@LargeTest
class GalleryScreenTest {

    @get:Rule
    val dependenciesRule = DependencyOverrideRule(galleryComponent) {
        bind<ViewModelHandler<GalleryState>>(overrides = true) with provider { viewModelHandler }
    }

    private val viewModelHandler: ViewModelHandler<GalleryState> = mock()
    private val liveState = MutableLiveData<ViewState<GalleryState>>()

    @Before
    fun setup() {
        whenever(viewModelHandler.liveState()).thenReturn(liveState)
    }

    @Test
    fun verifyScreenStateWhenIsInitializing() {
        val bundle = GalleryFragmentArgs(DEFAULT_REGISTRATION).toBundle()
        launchParameterizedFragmentTest<GalleryFragment>(bundle)

        liveState.postValue(ViewState.Initializing)

        GalleryRobot
            .assertLoadingDisplayed()

        verify(viewModelHandler).handle(GalleryInteraction.Load(DEFAULT_REGISTRATION))
    }

    @Test
    fun verifyScreenStateWhenRequestSucceeded() {
        val bundle = GalleryFragmentArgs(DEFAULT_REGISTRATION).toBundle()
        launchParameterizedFragmentTest<GalleryFragment>(bundle)

        val results = generateMockResults(5)
        val state = GalleryState(registration = DEFAULT_REGISTRATION, results = results)

        liveState.postValue(ViewState.Success(state))

        GalleryRobot
            .assertLoadingHidden()
            .assertResultsDisplayedWithSize(results.size)
            .assertResults(results)
    }

    @Test
    fun verifyScreenStateWhenRequestFailedAndRetry() {
        val bundle = GalleryFragmentArgs(DEFAULT_REGISTRATION).toBundle()
        launchParameterizedFragmentTest<GalleryFragment>(bundle)

        liveState.postValue(ViewState.Failed(reason = Throwable()))

        GalleryRobot
            .assertLoadingHidden()
            .assertResultsHidden()
            .assertSnackbarText(resources().getString(R.string.gallery_error))
            .assertSnackbarAction(resources().getString(coreR.string.default_retry_action))
            .performSnackbarAction()

        verify(viewModelHandler).handle(GalleryInteraction.Retry(DEFAULT_REGISTRATION))
    }

    private fun generateMockResults(size: Int): List<PhotoResult> {
        val results = mutableListOf<PhotoResult>()
        repeat(size) {
            results.add(PhotoResult("photographer$it", "notes$it", "url$it"))
        }
        return results
    }
}