package com.aviationdata.features.gallery.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aviationdata.core.dependencies.selfBind
import com.aviationdata.core.structure.ViewHandler
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.core.structure.ViewState.*
import com.aviationdata.features.gallery.R
import com.aviationdata.features.gallery.galleryComponent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_gallery.*
import org.kodein.di.generic.instance
import com.aviationdata.core.R as coreR

class GalleryActivity : AppCompatActivity(), ViewHandler<GalleryState> {

    private val viewModelHandler: ViewModelHandler<GalleryState> by selfBind(galleryComponent).instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(gallery_toolbar)

        configurePhotoGallery()

        viewModelHandler.liveState().observe(this, Observer { handle(it) })
    }

    private fun configurePhotoGallery() {
        val adapter = PhotosAdapter()
        gallery_photos.adapter = adapter
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
        // TODO set aircraft registration as toolbar title
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
                viewModelHandler.handle(GalleryInteraction.Retry(""))
            }
            .show()
    }
}