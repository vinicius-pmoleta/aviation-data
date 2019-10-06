package com.aviationdata.features.gallery.data

import com.google.gson.annotations.SerializedName

data class RawPhotoResponse(
    @SerializedName("mediumURL") val url: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("photographerName") val photographer: String? = null
)