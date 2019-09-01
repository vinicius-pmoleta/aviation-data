package com.aviationdata.features.search

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aviationdata.core.dependencies.ApplicationComponent
import com.aviationdata.core.structure.AppDispatchers
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.features.search.business.SearchBusiness
import com.aviationdata.features.search.data.SearchRemoteRepository
import com.aviationdata.features.search.data.SearchService
import com.aviationdata.features.search.view.SearchState
import com.aviationdata.features.search.view.SearchActivity
import com.aviationdata.features.search.viewmodel.SearchMapper
import com.aviationdata.features.search.viewmodel.SearchViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SearchScope

@SearchScope
@Component(
    modules = [SearchModule::class],
    dependencies = [ApplicationComponent::class]
)
interface SearchComponent {

    fun inject(activity: SearchActivity)
}

@Module
class SearchModule(private val activity: AppCompatActivity) {

    @Provides
    @SearchScope
    fun provideViewModelFactory(
        business: SearchBusiness,
        mapper: SearchMapper,
        dispatchers: AppDispatchers
    ): SearchViewModelFactory {
        return SearchViewModelFactory(business, mapper, dispatchers)
    }

    @Provides
    @SearchScope
    fun provideMapper(): SearchMapper {
        return SearchMapper(activity)
    }

    @Provides
    @SearchScope
    fun provideViewModelHandler(factory: SearchViewModelFactory): ViewModelHandler<SearchState> {
        return ViewModelProvider(activity, factory).get(SearchViewModel::class.java)
    }

    @Provides
    @SearchScope
    fun provideBusiness(remoteRepository: SearchRemoteRepository): SearchBusiness {
        return SearchBusiness(remoteRepository)
    }

    @Provides
    @SearchScope
    fun provideService(openSkyRetrofit: Retrofit): SearchService {
        return openSkyRetrofit.create(SearchService::class.java)
    }

    @Provides
    @SearchScope
    fun provideRemoteRepository(service: SearchService): SearchRemoteRepository {
        return SearchRemoteRepository(service)
    }

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
}