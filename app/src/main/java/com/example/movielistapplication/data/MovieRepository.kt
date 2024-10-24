package com.example.movielistapplication.data
class MovieRepository(private val movieDao: MovieDao) {
    suspend fun addMovie(movie: Movie) {
        movieDao.insert(movie)
    }
    suspend fun getAllMovies(): List<Movie> {
        return movieDao.getAllMovies()
    }
}