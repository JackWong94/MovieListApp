package com.wongyuheng.movielistapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @SerializedName("imdbID") var imdbID: String = "",
    @SerializedName("Title") var title: String = "",
    @SerializedName("Year") var year: String = "",
    @SerializedName("Poster") var poster: String = ""
)
data class MovieResponse(
    @SerializedName("Search") val search: List<Movie>,
    @SerializedName("totalResults") val totalResults: String,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)