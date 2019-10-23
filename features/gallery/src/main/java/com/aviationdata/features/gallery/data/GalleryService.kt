package com.aviationdata.features.gallery.data

import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryService {

    @GET("api/json/photos_internal.php")
    suspend fun photos(
        @Query("reg") registration: String,
        @Query("limit") limit: Int
    ): List<RawPhotoResponse>
}