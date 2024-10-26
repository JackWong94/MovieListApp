package com.wongyuheng.movielistapplication.data
class MovieRepository(private val movieDao: MovieDao) {
    fun addMovie(movie: Movie) {
        // Check if the movie already exists
        val existingMovie = getMovieById(movie.imdbID) // Assuming this method fetches a movie by its ID
        if (existingMovie == null) {
            // Add movie only if it doesn't exist
            movieDao.insert(movie) // Adjust to your actual database insert method
        }
    }
    fun getAllMovies(): List<Movie> {
        return movieDao.getAllMovies()
    }
    fun getMovieById(imdbID: String): Movie? {
        return movieDao.getMovieById(imdbID) // Fetch the movie from the database by ID
    }
}