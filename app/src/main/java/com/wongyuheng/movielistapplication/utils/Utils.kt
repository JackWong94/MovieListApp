package com.wongyuheng.movielistapplication.utils

object Utils {
    fun isValidSearchQuery(query: String): Boolean {
        val regex = "^[a-zA-Z0-9\\s]*$".toRegex()
        return query.isNotEmpty() && regex.matches(query)
    }
}