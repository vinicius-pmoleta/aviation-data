package com.aviationdata.features.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aviationdata.common.core.extension.selfBind
import com.aviationdata.common.core.extension.updateTitle
import com.aviationdata.common.core.structure.ViewHandler
import com.aviationdata.common.core.structure.ViewModelHandler
import com.aviationdata.common.core.structure.ViewState
import com.aviationdata.common.core.structure.ViewState.*
import com.aviationdata.common.core.utility.dismissKeyboard
import com.aviationdata.common.core.view.EndlessRecyclerViewScrollListener
import com.aviationdata.features.search.R
import com.aviationdata.features.search.searchComponent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*
import org.kodein.di.generic.instance
import com.aviationdata.common.core.R as coreR

class SearchFragment : Fragment(), ViewHandler<SearchState> {

    private val viewModelHandler: ViewModelHandler<SearchState> by selfBind(searchComponent).instance()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = SearchAdapter {
                findNavController().navigate(
                    SearchFragmentDirections.actionOpenGalleryWith(it.identification)
                )
            }
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
        updateTitle(getString(R.string.search_screen_title))
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

        updateTitle(getString(R.string.search_screen_title_loading))
        dismissKeyboard(this)
        search_input.clearFocus()
        search_input_bar.setExpanded(false, true)
    }

    private fun handlePresentation(value: SearchState) {
        updateTitle(getString(R.string.search_screen_title_with_query, value.query))
        search_loading.visibility = View.GONE
        search_results.visibility = View.VISIBLE
        (search_results.adapter as SearchAdapter).updateResults(value.results)
    }

    private fun handleError() {
        Snackbar.make(search_root, R.string.search_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(coreR.string.default_retry_action) {
                viewModelHandler.handle(SearchInteraction.Retry)
            }
            .show()

        search_loading.visibility = View.GONE
        updateTitle(getString(R.string.search_screen_title))
        search_input_bar.setExpanded(true, true)
        search_input.requestFocus()
    }
}