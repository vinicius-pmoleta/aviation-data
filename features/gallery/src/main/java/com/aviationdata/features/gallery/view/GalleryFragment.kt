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
import com.aviationdata.common.core.structure.ViewState.*
import com.aviationdata.features.gallery.R
import com.aviationdata.features.gallery.galleryComponent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.kodein.di.generic.instance
import com.aviationdata.common.core.R as coreR

class GalleryFragment : Fragment(), ViewHandler<GalleryState> {

    private val arguments: GalleryFragmentArgs by navArgs()
    private val viewModelHandler: ViewModelHandler<GalleryState> by selfBind(galleryComponent).instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configurePhotoGallery()

        viewModelHandler.liveState().observe(this, Observer { handle(it) })
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
        val registration = arguments.registration
        updateTitle(getString(R.string.gallery_screen_title, registration))

        viewModelHandler.handle(GalleryInteraction.Load(registration))
    }

    private fun prepareExecution() {
        gallery_loading.visibility = View.VISIBLE
        gallery_photos.visibility = View.GONE
    }

    private fun handlePresentation(state: GalleryState) {
        (gallery_photos.adapter as PhotosAdapter).updateResults(state.results)
        gallery_loading.visibility = View.GONE
        gallery_photos.visibility = View.VISIBLE
    }

    private fun handleError() {
        gallery_loading.visibility = View.GONE
        Snackbar.make(gallery_root, R.string.gallery_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(coreR.string.default_retry_action) {
                viewModelHandler.handle(GalleryInteraction.Retry(arguments.registration))
            }
            .show()
    }
}