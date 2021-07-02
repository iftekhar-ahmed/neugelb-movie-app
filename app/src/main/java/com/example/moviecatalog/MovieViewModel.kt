package com.example.moviecatalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.data.MovieRepository
import com.example.moviecatalog.data.models.Configuration
import com.example.moviecatalog.service.ApiError
import com.example.moviecatalog.service.MoviesListResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private var configuration: Configuration? = null

    var isLoading = false
        private set

    val hasPage: Boolean = _movies.value?.let { it.page < it.totalPages } ?: true

    private fun fetchConfiguration() {
        isLoading = false
        // One-time fetching of image base URLs and available poster sizes from /configuration API
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getConfiguration()
            if (response.isSuccessful) {
                configuration = response.body()
                getMovies()
            } else {
                _error.value =
                    Gson().fromJson(
                        response.errorBody()?.string(),
                        ApiError::class.java
                    ).status
            }
            isLoading = false
        }
    }

    fun getMovies() {
        isLoading = true
        configuration?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val response = movieRepository.getLatestMovies(_movies.value?.nextPage?.toString() ?: "1")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val moviesListResponse = response.body() as MoviesListResponse
                        val imageConfig = (configuration as Configuration).images
                        moviesListResponse.results.forEach { movie ->
                            movie.fullPosterPath = "${imageConfig.baseUrl}${imageConfig.posterSize}${movie.posterPath}"
                        }
                        _movies.value = moviesListResponse
                    } else {
                        _error.value =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ApiError::class.java
                            ).status
                    }
                    isLoading = false
                }
            }
        } ?: fetchConfiguration()
    }
}