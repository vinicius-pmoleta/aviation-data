package com.aviationdata.features.gallery.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.aviationdata.features.gallery.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_gallery_photo.*

class PhotosAdapter : RecyclerView.Adapter<PhotoViewHolder>() {

    private val results = mutableListOf<PhotoResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<PhotoResult>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }
}

class PhotoViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(result: PhotoResult) {
        gallery_photo_image.load(result.url) {
            crossfade(true)
            placeholder(R.drawable.ic_photo_24dp)
        }
        gallery_photo_notes.text = result.notes
        gallery_photo_photographer.text = result.photographer
    }
}