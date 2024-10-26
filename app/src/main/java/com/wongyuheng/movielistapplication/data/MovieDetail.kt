package com.wongyuheng.movielistapplication.data

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val title: String = "Title",
    val year: String = "Year",
    val rated: String = "Rated",
    val released: String = "Released", // Added Released field
    val runtime: String = "Runtime", // Added Runtime field
    val genre: String = "Genre",
    val director: String = "Director", // Added Director field
    val writer: String = "Writer", // Added Writer field
    val actors: String = "Actors", // Added Actors field
    val plot: String = "Plot",
    val language: String = "Language", // Added Language field
    val country: String = "Country", // Added Country field
    val awards: String = "Awards", // Added Awards field
    val poster: String? = "Poster", // Changed to String? to allow null
    val ratings: List<Rating> = emptyList(), // Changed to a list with a default empty list
    val metascore: String = "Metascore", // Added Metascore field
    val imdbRating: String = "imdbRating", // Added imdbRating field
    val imdbVotes: String = "imdbVotes", // Added imdbVotes field
    val imdbID: String = "imdbID", // Added imdbID field
    val type: String = "Type", // Added Type field
    val dvd: String = "DVD", // Added DVD field
    val boxOffice: String = "BoxOffice", // Added BoxOffice field
    val production: String = "Production", // Added Production field
    val website: String = "Website", // Added Website field
    val response: String = "Response", // Added Response field
    val error: String? = null, // Optional field for error messages
    val otherRatingTypes: Int = 0
)
data class MovieDetailResponse(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Rated") val rated: String,
    @SerializedName("Released") val released: String,
    @SerializedName("Runtime") val runtime: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("Director") val director: String,
    @SerializedName("Writer") val writer: String,
    @SerializedName("Actors") val actors: String,
    @SerializedName("Plot") val plot: String,
    @SerializedName("Language") val language: String,
    @SerializedName("Country") val country: String,
    @SerializedName("Awards") val awards: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Ratings") val ratings: List<Rating>,
    @SerializedName("Metascore") val metascore: String,
    @SerializedName("imdbRating") val imdbRating: String,
    @SerializedName("imdbVotes") val imdbVotes: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("DVD") val dvd: String,
    @SerializedName("BoxOffice") val boxOffice: String,
    @SerializedName("Production") val production: String,
    @SerializedName("Website") val website: String,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String? // Optional field for error messages
)
data class Rating(
    @SerializedName("Source") val source: String, // JSON key "Source"
    @SerializedName("Value") val value: String     // JSON key "Value"
)
