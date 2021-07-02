package com.example.moviecatalog.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.MovieViewModel
import com.example.moviecatalog.MovieViewModelFactory
import com.example.moviecatalog.R
import com.example.moviecatalog.data.models.Configuration
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.activity_movie_details.toolbar

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */
class MovieDetailsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, MovieViewModelFactory()).get(MovieViewModel::class.java)
    }

    companion object {
        const val EXTRA_MOVIE_ID = "_movie_id"
        const val EXTRA_CONFIGURATION = "_configuration"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movieID = intent.extras?.getInt(EXTRA_MOVIE_ID) ?: let {
            finish()
            return
        }
        val configuration = intent.extras?.getParcelable<Configuration>(EXTRA_CONFIGURATION) ?: let {
            finish()
            return
        }

        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.movie.observe(this, { movie ->
            Picasso.get().apply {
                load(movie.fullBackdropPath).placeholder(R.drawable.ic_movie).error(R.drawable.ic_movie).into(backdrop)
                load(movie.fullPosterPath).placeholder(R.drawable.ic_movie).error(R.drawable.ic_movie).into(poster)
            }
            if (!movie.tagline.isNullOrBlank()) {
                tagline.text = movie.tagline
                tagline.visibility = View.VISIBLE
            }
            movie_title.text = movie.title
            releaseDate.text = movie.releaseDate
            runtime.text = movie.getRuntime().let { getString(R.string.movie_runtime, it.hour, it.minutes) }
            genres.text = getString(R.string.genres, movie.getGenresCsv())
            overview.text = movie.overview
        })

        viewModel.error.observe(this, { message ->
            Toast.makeText(this@MovieDetailsActivity, message, Toast.LENGTH_LONG).show()
        })

        viewModel.getMovie(movieID.toString(), configuration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}