package com.aviationdata.features.gallery.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aviationdata.features.gallery.R
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(gallery_toolbar)

        configurePhotoGallery()
    }

    private fun configurePhotoGallery() {
        val adapter = PhotosAdapter()
        gallery_photos.adapter = adapter
    }
}