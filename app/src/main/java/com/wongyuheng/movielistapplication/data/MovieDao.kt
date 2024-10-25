package com.wongyuheng.movielistapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE imdbID = :id")
    fun getMovieById(id: String): Movie?
}