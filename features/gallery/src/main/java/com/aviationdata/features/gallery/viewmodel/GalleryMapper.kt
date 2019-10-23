package com.aviationdata.features.gallery.viewmodel

import android.content.Context
import com.aviationdata.common.core.R as coreR
import com.aviationdata.features.gallery.business.PhotoData
import com.aviationdata.features.gallery.view.PhotoResult

class GalleryMapper(context: Context) {

    private val noValuePlaceHolder: String =
        context.getString(coreR.string.default_no_value_placeholder)

    fun toPresentation(data: PhotoData): PhotoResult {
        return with(data) {
            PhotoResult(
                photographer = photographer ?: noValuePlaceHolder,
                notes = notes ?: noValuePlaceHolder,
                url = url ?: ""
            )
        }
    }
}