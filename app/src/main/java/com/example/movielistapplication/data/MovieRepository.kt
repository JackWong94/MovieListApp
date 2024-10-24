package com.example.movielistapplication.data
class MovieRepository(private val movieDao: MovieDao) {
    fun addMovie(movie: Movie) {
        movieDao.insert(movie)
    }
    fun getAllMovies(): List<Movie> {
        return movieDao.getAllMovies()
    }
}