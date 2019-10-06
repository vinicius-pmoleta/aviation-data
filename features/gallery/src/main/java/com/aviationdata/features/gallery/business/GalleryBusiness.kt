package com.aviationdata.features.gallery.business

import com.aviationdata.features.gallery.data.GalleryRemoteRepository

class GalleryBusiness(private val remoteRepository: GalleryRemoteRepository) {

    suspend fun loadPhotosFor(registration: String): List<PhotoData> {
        return remoteRepository.loadPhotosFor(registration)
    }
}