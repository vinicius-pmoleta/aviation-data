package com.aviationdata.common.core.structure

import androidx.lifecycle.LiveData

sealed class ViewState<out T> {

    object Initializing : ViewState<Nothing>()

    sealed class Loading<T> : ViewState<T>() {
        object FromEmpty : Loading<Nothing>()
        data class FromPrevious<T>(val previous: T) : Loading<T>()
    }

    data class Success<T>(val value: T) : ViewState<T>()
    data class Failed(val reason: Throwable) : ViewState<Nothing>()

}

interface UserInteraction

interface ViewModelHandler<T> {
    fun handle(interaction: UserInteraction)
    fun liveState(): LiveData<ViewState<T>>
}

interface ViewHandler<in T> {
    fun handle(state: ViewState<T>)
}