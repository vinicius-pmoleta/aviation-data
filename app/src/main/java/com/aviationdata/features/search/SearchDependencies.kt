package com.aviationdata.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.aviationdata.core.dependencies.ApplicationComponent
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.features.search.data.SearchState
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
class SearchModule(private val owner: ViewModelStoreOwner) {

    @Provides
    @SearchScope
    fun provideViewModelFactory(business: SearchBusiness): SearchViewModelFactory {
        return SearchViewModelFactory(business)
    }

    @Provides
    @SearchScope
    fun provideViewModelHandler(factory: SearchViewModelFactory): ViewModelHandler<SearchState> {
        return ViewModelProvider(owner, factory).get(SearchViewModel::class.java)
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
        private val business: SearchBusiness
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(business) as T
        }
    }
}