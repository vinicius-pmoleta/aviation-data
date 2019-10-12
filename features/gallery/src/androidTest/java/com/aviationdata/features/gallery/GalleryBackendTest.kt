package com.aviationdata.features.gallery

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.aviationdata.common.core.androidtest.base.launchParameterizedFragmentTest
import com.aviationdata.common.core.androidtest.readFile
import com.aviationdata.common.core.androidtest.resources
import com.aviationdata.common.core.androidtest.rules.BackendRule
import com.aviationdata.common.core.androidtest.rules.DependencyOverrideRule
import com.aviationdata.common.core.dependencies.KodeinTags
import com.aviationdata.common.core.dependencies.modules.RetrofitBuilder
import com.aviationdata.features.gallery.view.GalleryFragment
import com.aviationdata.features.gallery.view.GalleryFragmentArgs
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

private const val DEFAULT_REGISTRATION = "registration"

@RunWith(AndroidJUnit4::class)
@LargeTest
class GalleryBackendTest {

    @get:Rule
    val backendRule = BackendRule()

    @get:Rule
    val dependenciesRule = DependencyOverrideRule(galleryComponent) {
        bind<Retrofit>(overrides = true, tag = KodeinTags.REMOTE_SOURCE_JET_PHOTOS) with provider {
            RetrofitBuilder.build(
                url = backendRule.baseUrl,
                client = instance()
            )
        }
    }

    @Test
    fun verifyPhotosDisplayedWhenRequestIsCompletedWithResults() {
        backendRule
            .prepare(200, readFile("gallery_200_results.json"))

        val bundle = GalleryFragmentArgs(DEFAULT_REGISTRATION).toBundle()
        launchParameterizedFragmentTest<GalleryFragment>(bundle)

        backendRule.checkRequest(
            parameters = mapOf("reg" to DEFAULT_REGISTRATION, "limit" to "10")
        )

        GalleryRobot
            .assertResultsDisplayedWithSize(10)
    }

    @Test
    fun verifyEmptyStateDisplayedWhenRequestIsCompletedWithoutResults() {
        backendRule
            .prepare(200, readFile("gallery_200_no_results.json"))

        val bundle = GalleryFragmentArgs(DEFAULT_REGISTRATION).toBundle()
        launchParameterizedFragmentTest<GalleryFragment>(bundle)

        backendRule.checkRequest(
            parameters = mapOf("reg" to DEFAULT_REGISTRATION, "limit" to "10")
        )

        GalleryRobot
            .assertResultsDisplayedWithSize(0)
    }

    @Test
    fun verifyErrorMessageDisplayedWhenRequestFailsToComplete() {
        backendRule
            .prepare(500)
            .prepare(200, readFile("gallery_200_results.json"))

        val bundle = GalleryFragmentArgs(DEFAULT_REGISTRATION).toBundle()
        launchParameterizedFragmentTest<GalleryFragment>(bundle)

        backendRule.checkRequest(
            parameters = mapOf("reg" to DEFAULT_REGISTRATION, "limit" to "10")
        )

        GalleryRobot
            .assertResultsHidden()
            .assertSnackbarText(resources().getString(R.string.gallery_error))
            .performSnackbarAction()

        backendRule.checkRequest(
            parameters = mapOf("reg" to DEFAULT_REGISTRATION, "limit" to "10")
        )

        GalleryRobot
            .assertResultsDisplayedWithSize(10)
    }
}