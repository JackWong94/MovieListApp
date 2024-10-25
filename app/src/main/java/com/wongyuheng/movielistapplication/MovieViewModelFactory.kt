package com.wongyuheng.movielistapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wongyuheng.movielistapplication.data.MovieRepository
import com.wongyuheng.movielistapplication.data.MovieViewModel

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}