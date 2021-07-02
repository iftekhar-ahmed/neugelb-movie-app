package com.example.moviecatalog.view

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.*
import com.example.moviecatalog.data.models.MinimalMovie
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory()).get(MovieViewModel::class.java)
    }

    private val adapter by lazy {
        MoviesListAdapter(object : MoviesListAdapter.OnClickListener {
            override fun onItemClick(movie: MinimalMovie) {
                startActivity(
                    Intent(this@MainActivity, MovieDetailsActivity::class.java).apply {
                        putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movie.id)
                        putExtra(MovieDetailsActivity.EXTRA_CONFIGURATION, viewModel.configuration)
                    }
                )
            }
        })
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            viewModel.searchMovies(query)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        movies_list.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        movies_list.adapter = adapter
        movies_list.addItemDecoration(
            SpacesItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.grid_item_space),
                true
            )
        )
        movies_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Check if we should call for more pages
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                if (dy > 0
                    && gridLayoutManager.findLastVisibleItemPosition() + 1 >= gridLayoutManager.itemCount
                    && viewModel.isLoading.value == false
                    && viewModel.hasPage
                ) {
                    if (viewModel.searchQuery.isNullOrBlank()) {
                        viewModel.getMovies()
                    } else {
                        viewModel.searchMovies(viewModel.searchQuery)
                    }
                }
            }
        })

        viewModel.isLoading.observe(this, { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.movies.observe(this, { moviesListResponse ->
            if (moviesListResponse.results.isEmpty()) {
                adapter.clearItems()
            } else {
                adapter.addItems(moviesListResponse.results)
            }
            resultsCount.text = getString(R.string.results_count, moviesListResponse.totalResults)
            pagesCount.text = getString(
                R.string.pages_count,
                moviesListResponse.page,
                moviesListResponse.totalPages
            )
        })

        viewModel.error.observe(this, { message ->
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        })

        viewModel.getMovies()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search)).apply {
            (actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    viewModel.searchMovies("") // this resets search results
                    viewModel.getMovies()
                    return true
                }

                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    viewModel.searchMovies("") // this resets the latest movies list
                    return true
                }
            })
        }

        return true
    }
}