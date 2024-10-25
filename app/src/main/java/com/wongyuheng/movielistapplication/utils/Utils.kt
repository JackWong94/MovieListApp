package com.wongyuheng.movielistapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

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

object AuthUtils {

    // Create an instance of EncryptedSharedPreferences
    fun createEncryptedPrefs(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Hash the password
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        return Base64.encodeToString(hashedBytes, Base64.DEFAULT)
    }

    // Store email and hashed password
    fun storeCredentials(sharedPreferences: SharedPreferences, email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_password", hashPassword(password))
        editor.apply()
    }

    // Validate offline login
    fun validateOfflineLogin(sharedPreferences: SharedPreferences, inputEmail: String, inputPassword: String): Boolean {
        val storedEmail = sharedPreferences.getString("user_email", null)
        val storedPassword = sharedPreferences.getString("user_password", null)

        return storedEmail == inputEmail && storedPassword == hashPassword(inputPassword)
    }

    // Check network availability
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val LOGGED_IN_KEY = "logged_in"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Get the login state
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(LOGGED_IN_KEY, false)
    }

    // Set the login state
    fun setLoggedIn(loggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(LOGGED_IN_KEY, loggedIn).apply()
    }

    // Clear the login state
    fun clearLoginState() {
        sharedPreferences.edit().remove(LOGGED_IN_KEY).apply()
    }
}