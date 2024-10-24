package com.example.movielistapplication.data

data class MovieDetail(
    val Title: String = "Title",
    val Year: String = "Year",
    val Rated: String = "Rated",
    val Released: String = "Released", // Added Released field
    val Runtime: String = "Runtime", // Added Runtime field
    val Genre: String = "Genre",
    val Director: String = "Director", // Added Director field
    val Writer: String = "Writer", // Added Writer field
    val Actors: String = "Actors", // Added Actors field
    val Plot: String = "Plot",
    val Language: String = "Language", // Added Language field
    val Country: String = "Country", // Added Country field
    val Awards: String = "Awards", // Added Awards field
    val Poster: String? = "Poster", // Changed to String? to allow null
    val Ratings: List<Rating> = emptyList(), // Changed to a list with a default empty list
    val Metascore: String = "Metascore", // Added Metascore field
    val imdbRating: String = "imdbRating", // Added imdbRating field
    val imdbVotes: String = "imdbVotes", // Added imdbVotes field
    val imdbID: String = "imdbID", // Added imdbID field
    val Type: String = "Type", // Added Type field
    val DVD: String = "DVD", // Added DVD field
    val BoxOffice: String = "BoxOffice", // Added BoxOffice field
    val Production: String = "Production", // Added Production field
    val Website: String = "Website", // Added Website field
    val Response: String = "Response", // Added Response field
    val Error: String? = null, // Optional field for error messages
    val OtherRatingTypes: Int = 0
)

data class Rating(
    val Source: String = "",
    val Value: String = ""
)
