package com.aviationdata.common.navigation

import android.content.Context
import android.content.Intent

object Navigator {

    fun intentToGallery(context: Context): Intent {
        return Intent(
            context,
            Class.forName("com.aviationdata.features.gallery.view.GalleryActivity")
        )
    }
}