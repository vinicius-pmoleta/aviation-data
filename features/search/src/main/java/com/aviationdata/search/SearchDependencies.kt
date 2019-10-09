package com.aviationdata.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aviationdata.core.dependencies.KodeinTags
import com.aviationdata.core.dependencies.coreComponent
import com.aviationdata.core.structure.AppDispatchers
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.search.business.SearchBusiness
import com.aviationdata.search.data.SearchRemoteRepository
import com.aviationdata.search.data.SearchService
import com.aviationdata.search.view.SearchState
import com.aviationdata.search.viewmodel.SearchMapper
import com.aviationdata.search.viewmodel.SearchViewModel
import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

class SearchViewModelFactory(
    private val business: SearchBusiness,
    private val mapper: SearchMapper,
    private val dispatchers: AppDispatchers
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            business,
            mapper,
            dispatchers
        ) as T
    }
}

internal val searchModule = Kodein.Module("search") {

    bind() from provider {
        val retrofit = instance<Retrofit>(KodeinTags.REMOTE_SOURCE_OPEN_SKY)
        val service = retrofit.create(SearchService::class.java)

        SearchRemoteRepository(service)
    }

    bind() from provider {
        SearchBusiness(instance())
    }

    bind() from provider {
        val owner = instance<Fragment>(KodeinTags.HOST)
        SearchMapper(owner.requireContext())
    }

    bind<ViewModelHandler<SearchState>>() with provider {
        val factory = SearchViewModelFactory(
            business = instance(),
            mapper = instance(),
            dispatchers = instance()
        )
        val owner = instance<Fragment>(KodeinTags.HOST)

        ViewModelProvider(owner, factory).get(SearchViewModel::class.java)
    }
}

internal val searchComponent = ConfigurableKodein(mutable = true).apply {
    addExtend(coreComponent)
    addImport(searchModule)
}