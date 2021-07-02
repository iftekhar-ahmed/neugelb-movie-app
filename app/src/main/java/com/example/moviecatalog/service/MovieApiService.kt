package com.example.moviecatalog.service

import com.example.moviecatalog.BuildConfig
import com.example.moviecatalog.data.models.Configuration
import com.example.moviecatalog.data.models.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Iftekhar Ahmed on 28/06/2021.
 */
interface MovieApiService {

    @GET("configuration?api_key=${BuildConfig.API_KEY}")
    suspend fun getConfiguration(): Response<Configuration>

    @GET("discover/movie?api_key=${BuildConfig.API_KEY}&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false")
    suspend fun getLatestMovies(@Query("page") pageNo: String): Response<MoviesListResponse>

    @GET("movie/{movie_id}?api_key=${BuildConfig.API_KEY}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): Response<Movie>

    @GET("search/movie?api_key=${BuildConfig.API_KEY}")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") pageNo: String): Response<MoviesListResponse>
}