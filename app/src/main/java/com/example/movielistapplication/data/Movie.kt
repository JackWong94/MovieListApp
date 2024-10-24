package com.example.movielistapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie (
    @PrimaryKey val imdbID: String = "imdbID",
    val Title: String = "Title",
    val Year: String = "Year",
    val Poster: String = "Poster"
)