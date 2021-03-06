package com.aviationdata.features.gallery.view

import com.aviationdata.common.core.structure.UserInteraction

data class PhotoResult(
    val photographer: String = "",
    val notes: String = "",
    val url: String = ""
)

data class GalleryState(
    val registration: String = "",
    val results: List<PhotoResult> = listOf()
)

sealed class GalleryInteraction : UserInteraction {
    data class Load(val registration: String) : GalleryInteraction()
    data class Retry(val registration: String) : GalleryInteraction()
}