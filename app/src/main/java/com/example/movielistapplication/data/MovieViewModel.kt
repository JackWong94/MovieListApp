package com.example.movielistapplication.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Define a Retrofit service
interface MovieService {
    @GET("/")
    suspend fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") search: String,
        @Query("type") type: String
    ): MovieResponse
}

// Create a data class for the API response
data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String,
    val Error: String? // Add this to capture any error messages
)

class MovieViewModel : ViewModel() {
    private val movieService: MovieService

    // State to hold the list of movies
    private val _movieList = mutableStateListOf<Movie>() // Assuming Movie is your data class
    val movieList: List<Movie> get() = _movieList

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieService = retrofit.create(MovieService::class.java)
    }

    fun fetchMovies(searchQuery: String) {
        viewModelScope.launch {
            try {
                Log.d("JACK", "Retrieving....")
                val response = movieService.getMovies("6fc87060", searchQuery, "movie")
                if (response.Response == "True") {
                    _movieList.clear()
                    _movieList.addAll(response.Search ?: emptyList())
                    Log.d("JACK", "Success")
                } else {
                    _movieList.clear() // Handle error case
                    Log.d("JACK", "Fail")
                }
            } catch (e: Exception) {
                _movieList.clear() // Handle error case
                Log.e("JACK", "Error fetching movies: ${e.message ?: "Unknown error"}", e)
            }
        }
    }
}