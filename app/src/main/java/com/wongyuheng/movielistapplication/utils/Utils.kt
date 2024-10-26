package com.wongyuheng.movielistapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

object Utils {
    fun isValidSearchQuery(query: String): Boolean {
        val regex = "^[a-zA-Z0-9\\s]*$".toRegex()
        return query.isNotEmpty() && regex.matches(query) && query.trim() != "\n"
    }
    fun preventSpaces(query: String): Boolean {
        // Invalid if it contains spaces or tabs
        return !(query.contains(" ") || query.contains('\t'))
    }

    fun preventNewline(query: String): Boolean {
        // Invalid if it contains spaces or tabs
        return !(query.contains('\n'))
    }
}

object AuthUtils {
    private const val PREF_NAME = "secure_prefs"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize SharedPreferences
    fun init(context: Context) {
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
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
    fun storeCredentials(email: String, password: String) {
        // Retrieve existing credentials
        val existingEmail = sharedPreferences.getString("user_email", null)
        val existingPassword = sharedPreferences.getString("user_password", null)

        // Check if the credentials already exist and are the same
        if (existingEmail != email || existingPassword != hashPassword(password)) {
            val editor = sharedPreferences.edit()
            editor.putString("user_email", email)
            editor.putString("user_password", hashPassword(password))
            editor.apply()
        }
    }

    // Validate offline login
    fun validateOfflineLogin(inputEmail: String, inputPassword: String): Boolean {
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

class TriangleShape(private val triangleHeight: Float, private val triangleBase: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // Bottom left (0, size.height)
            moveTo(0f, size.height) // Start at the bottom left corner
            lineTo(0f, size.height - triangleHeight) // Move to the top left (side A)
            lineTo(triangleBase, size.height) // Move to the bottom right (side B)
            close() // Close the path to form a triangle
        }
        return Outline.Generic(path)
    }
}

@Composable
fun StarRating(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val clampedRating = rating.coerceIn(0f, 10f)
        val fullStars = clampedRating.toInt() / 2
        val hasHalfStar = (clampedRating % 2).toInt() != 0

        // Display full stars
        repeat(fullStars) {
            Image(
                painter = painterResource(id = android.R.drawable.star_big_on), // Filled star
                contentDescription = "Filled Star",
                modifier = androidx.compose.ui.Modifier.size(24.dp)
            )
        }

        // Display half star if needed
        if (hasHalfStar) {
            Image(
                painter = painterResource(id = android.R.drawable.star_on), // Half star
                contentDescription = "Half Star",
                modifier = androidx.compose.ui.Modifier.size(24.dp)
            )
        }

        // Display empty stars
        val emptyStars = (10 - clampedRating.toInt()) / 2
        repeat(emptyStars) {
            Image(
                painter = painterResource(id = android.R.drawable.star_off), // Empty star
                contentDescription = "Empty Star",
                modifier = androidx.compose.ui.Modifier.size(24.dp)
            )
        }
    }
}