package com.aviationdata.features.gallery

import androidx.annotation.IdRes
import com.aviationdata.common.core.androidtest.robot.BaseRobot
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition

class GalleryPhotoRobot(
    @IdRes private val container: Int,
    private val position: Int
) : BaseRobot() {

    fun assertPhotographer(photographer: String): GalleryPhotoRobot {
        assertDisplayedAtPosition(
            container,
            position,
            GalleryPhotoPage.photographer,
            photographer
        )
        return this
    }

    fun assertNotes(notes: String): GalleryPhotoRobot {
        assertDisplayedAtPosition(
            container,
            position,
            GalleryPhotoPage.notes,
            notes
        )
        return this
    }

    private object GalleryPhotoPage {
        val photographer = R.id.gallery_photo_photographer
        val notes = R.id.gallery_photo_notes
    }
}