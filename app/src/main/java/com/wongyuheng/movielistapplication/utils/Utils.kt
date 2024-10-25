package com.wongyuheng.movielistapplication.utils

object Utils {
    fun isValidSearchQuery(query: String): Boolean {
        val regex = "^[a-zA-Z0-9\\s]*$".toRegex()
        return query.isNotEmpty() && regex.matches(query)
    }
    fun preventSpaces(query: String): Boolean {
        // Invalid if it contains spaces or tabs
        return !(query.contains(" ") || query.contains('\t'))
    }
}