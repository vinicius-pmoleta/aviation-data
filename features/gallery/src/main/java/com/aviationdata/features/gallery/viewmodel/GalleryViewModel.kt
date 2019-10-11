package com.aviationdata.features.gallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviationdata.common.core.structure.AppDispatchers
import com.aviationdata.common.core.structure.UserInteraction
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.common.core.structure.ViewState
import com.aviationdata.features.gallery.business.GalleryBusiness
import com.aviationdata.features.gallery.view.GalleryInteraction
import com.aviationdata.features.gallery.view.GalleryState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(
    private val business: GalleryBusiness,
    private val mapper: GalleryMapper,
    private val dispatchers: AppDispatchers
) : ViewModel(), ViewModelHandler<GalleryState> {

    private val _stateLiveData = MutableLiveData<ViewState<GalleryState>>()
    private val stateLiveData: LiveData<ViewState<GalleryState>>
        get() = _stateLiveData

    init {
        emit(ViewState.Initializing)
    }

    override fun handle(interaction: UserInteraction) {
        when (interaction) {
            is GalleryInteraction.Load -> fetchPhotosFor(interaction.registration)
            is GalleryInteraction.Retry -> fetchPhotosFor(interaction.registration)
        }
    }

    override fun liveState(): LiveData<ViewState<GalleryState>> {
        return stateLiveData
    }

    private fun fetchPhotosFor(registration: String) {
        emit(ViewState.Loading.FromEmpty)
        viewModelScope.launch {
            try {
                val results = withContext(dispatchers.io) {
                    business.loadPhotosFor(registration)
                }.map {
                    mapper.toPresentation(it)
                }

                emit(
                    ViewState.Success(
                        GalleryState(
                            registration = registration,
                            results = results
                        )
                    )
                )
            } catch (error: Throwable) {
                emit(ViewState.Failed(error))
            }
        }
    }

    private fun emit(state: ViewState<GalleryState>) {
        _stateLiveData.value = state
    }
}