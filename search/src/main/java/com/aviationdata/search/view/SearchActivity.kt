package com.aviationdata.search.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aviationdata.core.dependencies.selfBind
import com.aviationdata.core.structure.ViewHandler
import com.aviationdata.core.structure.ViewModelHandler
import com.aviationdata.core.structure.ViewState
import com.aviationdata.core.structure.ViewState.*
import com.aviationdata.core.utility.dismissKeyboard
import com.aviationdata.core.view.EndlessRecyclerViewScrollListener
import com.aviationdata.search.R
import com.aviationdata.core.R as coreR
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search.*
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.on

class SearchActivity : AppCompatActivity(), ViewHandler<SearchState>, KodeinAware {

    override val kodeinContext: KodeinContext<SearchActivity> = kcontext(this)

    override val kodein by selfBind()

    private val viewModelHandler: ViewModelHandler<SearchState> by kodein.on(kodeinContext).instance()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(search_toolbar)

        configureInput()
        configureResults()

        viewModelHandler.liveState().observe(this, Observer { handle(it) })
    }

    private fun configureInput() {
        search_input.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> submitSearch()
            }
            true
        }
        search_input_container.setEndIconOnClickListener {
            viewModelHandler.handle(SearchInteraction.Reset)
        }
    }

    private fun configureResults() {
        search_results.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = SearchAdapter()
            scrollListener =
                object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        viewModelHandler.handle(SearchInteraction.More)
                    }
                }
            addOnScrollListener(scrollListener)
        }
    }

    private fun submitSearch() {
        val query = search_input.text.toString()
        viewModelHandler.handle(SearchInteraction.Search(query))
    }

    override fun handle(state: ViewState<SearchState>) {
        when (state) {
            is Initializing -> initialize()
            is Loading.FromEmpty -> prepareExecution()
            is Loading.FromPrevious -> handlePresentation(state.previous)
            is Success -> handlePresentation(state.value)
            is Failed -> handleError()
        }
    }

    private fun initialize() {
        search_toolbar.title = getString(R.string.search_screen_title)
        search_loading.visibility = View.GONE
        search_results.visibility = View.VISIBLE
        (search_results.adapter as SearchAdapter).clear()
        scrollListener.resetState()

        search_input_bar.setExpanded(true, true)
        search_input.requestFocus()
        search_input.text?.clear()
    }

    private fun prepareExecution() {
        search_loading.visibility = View.VISIBLE
        search_results.visibility = View.GONE

        search_toolbar.title = getString(R.string.search_screen_title_loading)
        dismissKeyboard(this)
        search_input.clearFocus()
        search_input_bar.setExpanded(false, true)
    }

    private fun handlePresentation(value: SearchState) {
        search_toolbar.title = getString(R.string.search_screen_title_with_query, value.query)
        search_loading.visibility = View.GONE
        search_results.visibility = View.VISIBLE
        (search_results.adapter as SearchAdapter).updateResults(value.results)
    }

    private fun handleError() {
        Snackbar.make(search_coordinator, R.string.search_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(coreR.string.default_retry_action) {
                viewModelHandler.handle(SearchInteraction.Retry)
            }
            .show()

        search_loading.visibility = View.GONE
        search_toolbar.title = getString(R.string.search_screen_title)
        search_input_bar.setExpanded(true, true)
        search_input.requestFocus()
    }
}