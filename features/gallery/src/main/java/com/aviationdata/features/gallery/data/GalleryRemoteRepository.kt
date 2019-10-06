package com.aviationdata.features.gallery.data

import com.aviationdata.features.gallery.business.PhotoData

internal const val DEFAULT_PHOTOS_LIMIT = 10

class GalleryRemoteRepository(private val service: GalleryService) {

    suspend fun loadPhotosFor(registration: String): List<PhotoData> {
        return service.photos(registration, DEFAULT_PHOTOS_LIMIT).map { rawPhoto ->
            PhotoData(
                url = rawPhoto.url,
                notes = rawPhoto.notes,
                photographer = rawPhoto.photographer
            )
        }
    }
}