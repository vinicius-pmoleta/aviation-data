package com.aviationdata.features.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aviationdata.common.core.structure.AppDispatchers
import com.aviationdata.features.gallery.business.GalleryBusiness

class GalleryViewModelFactory(
    private val business: GalleryBusiness,
    private val mapper: GalleryMapper,
    private val dispatchers: AppDispatchers
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(
            business,
            mapper,
            dispatchers
        ) as T
    }
}