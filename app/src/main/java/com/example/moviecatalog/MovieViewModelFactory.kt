package com.example.moviecatalog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.data.MovieRepository

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */
class MovieViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(MovieRepository()) as T
    }
}