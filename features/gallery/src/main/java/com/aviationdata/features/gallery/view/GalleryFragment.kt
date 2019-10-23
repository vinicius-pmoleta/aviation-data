package com.aviationdata.features.gallery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.aviationdata.common.core.extension.selfBind
import com.aviationdata.common.core.extension.updateTitle
import com.aviationdata.common.core.structure.ViewHandler
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.common.core.structure.ViewState
import com.aviationdata.common.core.structure.ViewState.Failed
import com.aviationdata.common.core.structure.ViewState.Initializing
import com.aviationdata.common.core.structure.ViewState.Loading
import com.aviationdata.common.core.structure.ViewState.Success
import com.aviationdata.features.gallery.R
import com.aviationdata.features.gallery.galleryComponent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.kodein.di.generic.instance
import com.aviationdata.common.core.R as coreR

private const val EXTRA_GALLERY_POSITION = "extra_gallery_position"

class GalleryFragment : Fragment(), ViewHandler<GalleryState> {

    private val viewModelHandler: ViewModelHandler<GalleryState> by selfBind(galleryComponent).instance()

    private val arguments: GalleryFragmentArgs by navArgs()
    private var stateBundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stateBundle = savedInstanceState

        configurePhotoGallery()

        viewModelHandler.liveState().observe(this, Observer { handle(it) })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(EXTRA_GALLERY_POSITION, gallery_photos.currentItem)
        super.onSaveInstanceState(outState)
    }

    private fun configurePhotoGallery() {
        gallery_photos.apply {
            adapter = PhotosAdapter()
        }
    }

    override fun handle(state: ViewState<GalleryState>) {
        when (state) {
            is Initializing -> initialize()
            is Loading.FromEmpty -> prepareExecution()
            is Loading.FromPrevious -> handlePresentation(state.previous)
            is Success -> handlePresentation(state.value)
            is Failed -> handleError()
        }
    }

    private fun initialize() {
        updateScreenTitle()
        viewModelHandler.handle(GalleryInteraction.Load(arguments.registration))
    }

    private fun prepareExecution() {
        gallery_loading.visibility = View.VISIBLE
        gallery_photos.visibility = View.GONE
    }

    private fun handlePresentation(state: GalleryState) {
        updateScreenTitle()

        (gallery_photos.adapter as PhotosAdapter).updateResults(state.results)
        gallery_loading.visibility = View.GONE
        gallery_photos.visibility = View.VISIBLE

        restoreState()
    }

    private fun handleError() {
        gallery_loading.visibility = View.GONE
        Snackbar.make(gallery_root, R.string.gallery_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(coreR.string.default_retry_action) {
                viewModelHandler.handle(GalleryInteraction.Retry(arguments.registration))
            }
            .show()
    }

    private fun updateScreenTitle() {
        val registration = arguments.registration
        updateTitle(getString(R.string.gallery_screen_title, registration))
    }

    private fun restoreState() {
        val position = stateBundle?.getInt(EXTRA_GALLERY_POSITION, 0) ?: 0
        gallery_photos.setCurrentItem(position, false)
    }
}