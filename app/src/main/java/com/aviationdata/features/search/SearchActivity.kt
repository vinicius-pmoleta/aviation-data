package com.aviationdata.features.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aviationdata.R
import com.aviationdata.features.search.data.SearchState
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.stateLiveData.observe(this, Observer { handleStateChange(it) })

        setSupportActionBar(search_toolbar)

        configureInput()
        configureResults()
    }

    private fun configureInput() {
        search_input.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> viewModel.search(
                    this@SearchActivity,
                    search_input.text.toString()
                )
                else -> {
                }
            }
            true
        }
    }

    private fun configureResults() {
        search_results.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = SearchAdapter()
        }
    }

    private fun handleStateChange(state: SearchState) {
        (search_results.adapter as SearchAdapter).updateResults(state.results)
    }
}