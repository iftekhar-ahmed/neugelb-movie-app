package com.example.moviecatalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.data.MovieRepository
import com.example.moviecatalog.data.models.Configuration
import com.example.moviecatalog.data.models.Movie
import com.example.moviecatalog.service.ApiError
import com.example.moviecatalog.service.MoviesListResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Created by Iftekhar Ahmed on 30/06/2021.
 */
class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _error by lazy { MutableLiveData<String>() }
    val error: LiveData<String>
        get() = _error

    private val _movies by lazy { MutableLiveData<MoviesListResponse>() }
    val movies: LiveData<MoviesListResponse>
        get() = _movies

    private val _movie by lazy { MutableLiveData<Movie>() }
    val movie: LiveData<Movie>
        get() = _movie

    private val _isLoading by lazy { MutableLiveData<Boolean>() }
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val hasPage: Boolean = _movies.value?.let { it.page < it.totalPages } ?: true

    var searchQuery: String? = null
        private set

    var configuration: Configuration? = null
        private set

    private fun fetchConfiguration(callback: () -> Unit) {
        _isLoading.value = false
        // One-time fetching of image base URLs and available poster sizes from /configuration API
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getConfiguration()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    configuration = response.body()
                    callback()
                } else {
                    _error.value = parseError(response.errorBody()?.string())
                }
                _isLoading.value = false
            }
        }
    }

    private fun handleMoviesListResponse(
        response: Response<MoviesListResponse>,
        success: (MoviesListResponse) -> Unit
    ) {
        if (response.isSuccessful) {
            val moviesListResponse = response.body() as MoviesListResponse
            val imageConfig = (configuration as Configuration).images
            moviesListResponse.results.forEach { movie ->
                movie.fullPosterPath = "${imageConfig.baseUrl}${imageConfig.posterSize}${movie.posterPath}"
            }
            success(moviesListResponse)
        } else {
            _error.value = parseError(response.errorBody()?.string())
        }
    }

    fun getMovie(id: String, configuration: Configuration) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getMovieDetails(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val movie = response.body() as Movie
                    val imageConfig = configuration.images
                    movie.fullPosterPath = "${imageConfig.baseUrl}${imageConfig.posterSize}${movie.posterPath}"
                    movie.fullBackdropPath = "${imageConfig.baseUrl}${imageConfig.backdropSize}${movie.backdropPath}"
                    _movie.value = movie
                } else {
                    _error.value = parseError(response.errorBody()?.string())
                }
                _isLoading.value = false
            }
        }
    }

    fun getMovies() {
        _isLoading.value = true
        configuration?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val response = movieRepository.getLatestMovies(_movies.value?.nextPage?.toString() ?: "1")
                withContext(Dispatchers.Main) {
                    handleMoviesListResponse(response) {
                        _movies.value = it
                    }
                    _isLoading.value = false
                }
            }
        } ?: fetchConfiguration { getMovies() }
    }

    fun searchMovies(query: String?) {
        searchQuery = query
        if (query.isNullOrBlank()) {
            _movies.value = MoviesListResponse(0, arrayOf(), 0, 0)
            return
        }
        _isLoading.value = true
        configuration?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val response = movieRepository.searchMovies(query, _movies.value?.nextPage?.toString() ?: "1")
                withContext(Dispatchers.Main) {
                    handleMoviesListResponse(response) {
                        _movies.value = it
                    }
                    _isLoading.value = false
                }
            }
        } ?: fetchConfiguration { searchMovies(query) }
    }

    private fun parseError(json: String?): String = Gson().fromJson(json,ApiError::class.java).status
}