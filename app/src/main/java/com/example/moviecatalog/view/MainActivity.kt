package com.example.moviecatalog.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
                    && !viewModel.isLoading
                    && viewModel.hasPage
                ) {
                    viewModel.getMovies()
                }
            }
        })

        viewModel.movies.observe(this, { moviesListResponse ->
            adapter.addItems(moviesListResponse.results)
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
}