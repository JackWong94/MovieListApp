package com.example.movielistapplication.data

data class Movie (
    val imdbID: String = "imdbID", // Unique identifier for the movie
    val OtherRatingTypes: Int = 0,
    val Plot: String = "Plot",
    val Header: String  = "Header",
    val Genre: String = "Genre",
    val Title: String = "Title",
    val Rating: Int = 0,
    val Poster: String = "Poster"
)