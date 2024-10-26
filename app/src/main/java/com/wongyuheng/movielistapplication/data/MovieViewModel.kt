package com.wongyuheng.movielistapplication.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") movieId: Any
    ): MovieDetailResponse
}

class MovieViewModel(repository: MovieRepository?) : ViewModel() {
    private val movieService: MovieService
    private val _repository = repository
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
                if (response.response == "True") {
                    _movieList.clear()
                    _movieList.addAll(response.search)
                    Log.d("JACK", "Success")
                    // Save each movie to the local database one by one
                    response.search.forEach { movie ->
                        val movieEntity = Movie(
                            imdbID = movie.imdbID,
                            title = movie.title,
                            year = movie.year,
                            poster = movie.poster
                        )
                        withContext(Dispatchers.IO) {
                            _repository?.addMovie(movieEntity) // Non-blocking add to database
                        }
                    }
                } else {
                    _movieList.clear() // Handle error case
                    Log.d("JACK", "Fail")
                    // Fetch all movies from the local database on error
                    val localMovies = withContext(Dispatchers.IO) {
                        _repository?.getAllMovies() // Fetch from database
                    }
                    _movieList.addAll(localMovies ?: emptyList()) // Safely add local movies
                }
            } catch (e: Exception) {
                _movieList.clear() // Handle error case
                Log.e("JACK", "Error fetching movies: ${e.message ?: "Unknown error"}", e)
                // Fetch all movies from the local database on error
                val localMovies = withContext(Dispatchers.IO) {
                    _repository?.getAllMovies() // Fetch from database
                }
                _movieList.addAll(localMovies ?: emptyList()) // Safely add local movies
            }
        }
    }

    private val _movieId = MutableStateFlow<String?>(null)
    val movieId: StateFlow<String?> = _movieId

    fun setMovieId(id: String) {
        _movieId.value = id
        Log.d("JACK", "Setting Movie ID: ${_movieId.value}")
    }
    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> get() = _movieDetail

    fun fetchMovieDetails(movieId: Any) {
        viewModelScope.launch {
                try {
                    Log.d("JACK", "Fetching movie details...")
                    val response = movieService.getMovieDetails("6fc87060", movieId)
                    Log.d("JACK", "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n${response}")
                    if (response.response == "True") {
                        val movieDetail = MovieDetail(
                            title = response.title,
                            year = response.year,
                            rated = response.rated,
                            released = response.released,
                            runtime = response.runtime,
                            genre = response.genre,
                            director = response.director,
                            writer = response.writer,
                            actors = response.actors,
                            plot = response.plot,
                            language = response.language,
                            country = response.country,
                            awards = response.awards,
                            poster = response.poster,
                            ratings = response.ratings,
                            metascore = response.metascore,
                            imdbRating = response.imdbRating,
                            imdbVotes = response.imdbVotes,
                            imdbID = response.imdbID,
                            type = response.type,
                            dvd = response.dvd,
                            boxOffice = response.boxOffice,
                            production = response.production,
                            website = response.website,
                            response = response.response,
                            error = response.error
                        )
                        _movieDetail.value = movieDetail
                        Log.d("JACK", "Movie details retrieved successfully $movieDetail")
                    } else {
                        _movieDetail.value = null // Handle error
                        Log.d("JACK", "Failed to fetch movie details: ${response.error}")
                    }
                } catch (e: Exception) {
                    _movieDetail.value = null // Handle error case
                    Log.e(
                        "JACK",
                        "Error fetching movie details: ${e.message ?: "Unknown error"}",
                        e
                    )
                }
        }
    }
}