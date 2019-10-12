package com.aviationdata.features.gallery

import com.aviationdata.common.core.androidtest.robot.BaseRobot
import com.aviationdata.common.core.androidtest.robot.common.LoadingRobot
import com.aviationdata.common.core.androidtest.waitForView
import com.aviationdata.features.gallery.view.PhotoResult
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed

object GalleryRobot : BaseRobot() {

    fun assertLoadingDisplayed(): GalleryRobot {
        LoadingRobot.assertLabelDisplayed()
        return this
    }

    fun assertLoadingHidden(): GalleryRobot {
        LoadingRobot.assertLabelHidden()
        return this
    }

    fun assertResultsDisplayedWithSize(size: Int): GalleryRobot {
        waitForView(GalleryPage.photos) {
            assertDisplayed(GalleryPage.photos)
            assertListItemCount(GalleryPage.photos, size)
        }
        return this
    }

    fun assertResultsHidden(): GalleryRobot {
        waitForView(GalleryPage.photos) {
            assertNotDisplayed(GalleryPage.photos)
        }
        return this
    }

    fun assertResults(results: List<PhotoResult>): GalleryRobot {
        waitForView(GalleryPage.photos) {
            for ((position, result) in results.withIndex()) {
                GalleryPhotoRobot(GalleryPage.photos, position)
                    .assertPhotographer(result.photographer)
                    .assertNotes(result.notes)
            }
        }
        return this
    }

    private object GalleryPage {
        val photos = R.id.gallery_photos
    }
}