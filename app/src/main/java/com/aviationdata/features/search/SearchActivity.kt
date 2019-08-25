package com.aviationdata.features.search

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aviationdata.R
import com.aviationdata.core.ViewState
import com.aviationdata.core.ViewState.*
import com.aviationdata.core.dismissKeyboard
import com.aviationdata.features.search.data.SearchState
import kotlinx.android.synthetic.main.activity_search.*

private const val TAG = "SearchActivity"

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
        viewModel.search(this@SearchActivity, query)
    }

    private fun handleStateChange(state: ViewState<SearchState>) {
        when (state) {
            is FirstLaunch -> launch()
            is Loading.FromEmpty -> startExecution()
            is Loading.FromPrevious -> handlePresentation(state.previous)
            is Success -> handlePresentation(state.value)
            is Failed -> handleError(state.reason)
        }
    }

    private fun handleError(reason: Throwable) {
        Log.e(TAG, "Error performing search", reason)

        // TODO show error

        search_toolbar.title = getString(R.string.search_screen_title)
        search_input_container.setExpanded(true, true)
        search_input.requestFocus()
    }

    private fun handlePresentation(value: SearchState) {
        // TODO show value

        search_toolbar.title = getString(R.string.search_screen_title_with_query, value.query)
        (search_results.adapter as SearchAdapter).updateResults(value.results)
    }

    private fun startExecution() {
        // TODO show loading

        search_toolbar.title = getString(R.string.search_screen_title_loading)
        dismissKeyboard(this)
        search_input.clearFocus()
        search_input_container.setExpanded(false, true)
    }

    private fun launch() {
        search_input.requestFocus()
    }
}