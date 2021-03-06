package com.aviationdata.features.gallery

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aviationdata.common.core.dependencies.DependenciesUtil
import com.aviationdata.common.core.dependencies.coreComponent
import com.aviationdata.common.core.structure.AppDispatchers
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.features.gallery.business.GalleryBusiness
import com.aviationdata.features.gallery.data.GalleryRemoteRepository
import com.aviationdata.features.gallery.data.GalleryService
import com.aviationdata.features.gallery.view.GalleryState
import com.aviationdata.features.gallery.viewmodel.GalleryMapper
import com.aviationdata.features.gallery.viewmodel.GalleryViewModel
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

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

internal val galleryModule = Kodein.Module("gallery") {

    bind() from provider {
        val retrofit = instance<Retrofit>(DependenciesUtil.REMOTE_SOURCE_JET_PHOTOS)
        val service = retrofit.create(GalleryService::class.java)

        GalleryRemoteRepository(service)
    }

    bind() from provider {
        GalleryBusiness(instance())
    }

    bind() from provider {
        val owner = instance<Fragment>(DependenciesUtil.HOST)
        GalleryMapper(owner.requireActivity())
    }

    bind<ViewModelHandler<GalleryState>>() with provider {
        val factory = GalleryViewModelFactory(
            business = instance(),
            mapper = instance(),
            dispatchers = instance()
        )
        val owner = instance<Fragment>(DependenciesUtil.HOST)

        ViewModelProvider(owner, factory).get(GalleryViewModel::class.java)
    }
}

internal val galleryComponent = ConfigurableKodein(mutable = true).apply {
    addExtend(coreComponent)
    addImport(galleryModule)
}