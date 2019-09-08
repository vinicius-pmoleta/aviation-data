package com.aviationdata.features.search

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aviationdata.core.dependencies.KodeinTags
import com.aviationdata.core.structure.AppDispatchers
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.features.search.business.SearchBusiness
import com.aviationdata.features.search.data.SearchRemoteRepository
import com.aviationdata.features.search.data.SearchService
import com.aviationdata.features.search.view.SearchState
import com.aviationdata.features.search.viewmodel.SearchMapper
import com.aviationdata.features.search.viewmodel.SearchViewModel
import org.kodein.di.Kodein
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

val searchModule = Kodein.Module("search") {

    bind() from provider {
        val retrofit = instance<Retrofit>()
        val service = retrofit.create(SearchService::class.java)

        SearchRemoteRepository(service)
    }

    bind() from provider {
        SearchBusiness(instance())
    }

    bind() from provider {
        SearchMapper(instance(KodeinTags.HOST_ACTIVITY))
    }

    bind<ViewModelHandler<SearchState>>() with provider {
        val factory = SearchViewModelFactory(
            business = instance(),
            mapper = instance(),
            dispatchers = instance()
        )
        val owner = instance<FragmentActivity>(KodeinTags.HOST_ACTIVITY)

        ViewModelProvider(owner, factory).get(SearchViewModel::class.java)
    }
}