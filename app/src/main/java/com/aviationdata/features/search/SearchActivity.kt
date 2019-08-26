package com.aviationdata.features.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aviationdata.R
import com.aviationdata.core.ViewHandler
import com.aviationdata.core.ViewModelHandler
import com.aviationdata.core.ViewState
import com.aviationdata.core.ViewState.*
import com.aviationdata.core.dismissKeyboard
import com.aviationdata.features.search.data.SearchInteraction
import com.aviationdata.features.search.data.SearchState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), ViewHandler<SearchState> {

    private lateinit var viewModelHandler: ViewModelHandler<SearchState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        configureInput()
        configureResults()

        viewModelHandler = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModelHandler.state().observe(this, Observer { handle(it) })
    }

    private fun configureInput() {
        search_input.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> submitSearch()
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

    private fun submitSearch() {
        val query = search_input.text.toString()
        viewModelHandler.handle(SearchInteraction(this@SearchActivity, query))
    }

    override fun handle(state: ViewState<SearchState>) {
        when (state) {
            is FirstLaunch -> initialize()
            is Loading.FromEmpty -> prepareExecution()
            is Loading.FromPrevious -> handlePresentation(state.previous)
            is Success -> handlePresentation(state.value)
            is Failed -> handleError(state.reason)
        }
    }

    private fun initialize() {
        search_input.requestFocus()
    }

    private fun prepareExecution() {
        search_loading.visibility = View.VISIBLE
        search_results.visibility = View.GONE

        search_toolbar.title = getString(R.string.search_screen_title_loading)
        dismissKeyboard(this)
        search_input.clearFocus()
        search_input_container.setExpanded(false, true)
    }

    private fun handlePresentation(value: SearchState) {
        search_toolbar.title = getString(R.string.search_screen_title_with_query, value.query)
        search_loading.visibility = View.GONE
        search_results.visibility = View.VISIBLE
        (search_results.adapter as SearchAdapter).updateResults(value.results)
    }

    private fun handleError(reason: Throwable) {
        Snackbar.make(search_coordinator, R.string.search_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.default_retry_action) { submitSearch() }
            .show()

        search_loading.visibility = View.GONE
        search_toolbar.title = getString(R.string.search_screen_title)
        search_input_container.setExpanded(true, true)
        search_input.requestFocus()
    }
}