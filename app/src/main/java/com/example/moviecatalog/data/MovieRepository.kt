package com.example.moviecatalog.data

import com.example.moviecatalog.data.models.Configuration
import com.example.moviecatalog.data.models.Movie
import com.example.moviecatalog.service.ApiUtil
import com.example.moviecatalog.service.MovieApiService
import com.example.moviecatalog.service.MoviesListResponse
import retrofit2.Response

/**
 * Created by Iftekhar Ahmed on 28/06/2021.
 */
class MovieRepository {

    companion object {
        const val API_BASE_URL = "https://api.themoviedb.org/3/"
    }

    private val movieService = ApiUtil.createService<MovieApiService>(API_BASE_URL)

    suspend fun getConfiguration(): Response<Configuration> {
        return movieService.getConfiguration()
    }

    suspend fun getLatestMovies(pageNo: String): Response<MoviesListResponse> {
        return movieService.getLatestMovies(pageNo)
    }

    suspend fun getMovieDetails(movieId: String): Response<Movie> {
        return movieService.getMovieDetails(movieId)
    }

    suspend fun searchMovies(query: String, pageNo: String): Response<MoviesListResponse> {
        return movieService.searchMovies(query, pageNo)
    }
}