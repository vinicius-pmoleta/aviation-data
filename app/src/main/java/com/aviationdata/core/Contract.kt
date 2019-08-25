package com.aviationdata.core

sealed class ViewState<out T> {

    object FirstLaunch : ViewState<Nothing>()

    sealed class Loading<T> : ViewState<T>() {
        object FromEmpty : Loading<Nothing>()
        data class FromPrevious<T>(val previous: T) : Loading<T>()
    }

    data class Success<T>(val value: T) : ViewState<T>()
    data class Failed(val reason: Throwable) : ViewState<Nothing>()

}

interface UserInteraction

interface InteractionHandler<in T> {
    fun handle(interaction: UserInteraction)
}

interface StateHandler<in T> {
    fun handle(state: ViewState<T>)
}