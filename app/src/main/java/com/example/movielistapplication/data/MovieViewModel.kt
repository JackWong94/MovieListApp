package com.example.movielistapplication.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") movieId: Any
    ): MovieDetailResponse
}

// Create a data class for the API response
data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String,
    val Error: String? // Add this to capture any error messages
)
data class MovieDetailResponse(
    val Title: String,
    val Year: String,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Writer: String,
    val Actors: String,
    val Plot: String,
    val Language: String,
    val Country: String,
    val Awards: String,
    val Poster: String,
    val Ratings: List<Rating>,
    val Metascore: String,
    val imdbRating: String,
    val imdbVotes: String,
    val imdbID: String,
    val Type: String,
    val DVD: String,
    val BoxOffice: String,
    val Production: String,
    val Website: String,
    val Response: String,
    val Error: String? // Optional field for error messages
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
                if (response.Response == "True") {
                    val movieDetail = MovieDetail(
                        Title = response.Title,
                        Year = response.Year,
                        Rated = response.Rated,
                        Released = response.Released,
                        Runtime = response.Runtime,
                        Genre = response.Genre,
                        Director = response.Director,
                        Writer = response.Writer,
                        Actors = response.Actors,
                        Plot = response.Plot,
                        Language = response.Language,
                        Country = response.Country,
                        Awards = response.Awards,
                        Poster = response.Poster,
                        Ratings = response.Ratings,
                        Metascore = response.Metascore,
                        imdbRating = response.imdbRating,
                        imdbVotes = response.imdbVotes,
                        imdbID = response.imdbID,
                        Type = response.Type,
                        DVD = response.DVD,
                        BoxOffice = response.BoxOffice,
                        Production = response.Production,
                        Website = response.Website,
                        Response = response.Response,
                        Error = response.Error
                    )
                    _movieDetail.value = movieDetail
                    Log.d("JACK", "Movie details retrieved successfully $movieDetail")
                } else {
                    _movieDetail.value = null // Handle error
                    Log.d("JACK", "Failed to fetch movie details: ${response.Error}")
                }
            } catch (e: Exception) {
                _movieDetail.value = null // Handle error case
                Log.e("JACK", "Error fetching movie details: ${e.message ?: "Unknown error"}", e)
            }
        }
    }
}