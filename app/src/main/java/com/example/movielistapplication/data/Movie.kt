package com.example.movielistapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie (
    @PrimaryKey var imdbID: String = "imdbID",
    var Title: String = "Title",
    var Year: String = "Year",
    var Poster: String = "Poster"
)