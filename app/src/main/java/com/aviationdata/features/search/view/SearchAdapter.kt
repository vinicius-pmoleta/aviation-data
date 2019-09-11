package com.aviationdata.features.search.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aviationdata.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_search_result.*

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    private val results = mutableListOf<SearchResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(results[position])
    }

    fun updateResults(newResults: List<SearchResult>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }

    fun clear() {
        results.clear()
        notifyDataSetChanged()
    }
}

class SearchViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(result: SearchResult) {
        search_result_identification.text = result.identification
        search_result_model.text = result.model
        search_result_operation.text = result.operation
    }
}