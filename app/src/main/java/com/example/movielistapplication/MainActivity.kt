package com.example.movielistapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielistapplication.data.Movie
import com.example.movielistapplication.ui.theme.MovieListApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieListApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //MainPage()
                    val selectedMovie = Movie()
                    MovieDetailsScreen(movie = selectedMovie) // Correctly passing the movie object
                }
            }
        }
    }
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(), // Fill the available space
        verticalArrangement = Arrangement.Center, // Center vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
    ) {
        // Image at the top
        Image(
            painter = painterResource(id = R.drawable.login_page_image), // Replace with your image resource
            contentDescription = "Logo", // Provide a content description for accessibility
            modifier = Modifier
                .size(300.dp) // Set the size of the image (adjust as needed)
                .padding(bottom = 16.dp) // Add space below the image
        )
        Text(
            text = "Access more with an account",
            modifier = Modifier.padding(bottom = 16.dp) // Add space below the Text
        )
        Button(onClick = { /* TODO: Handle Log in */ }) {
            Text(text = "Log in")
        }
        Spacer(modifier = Modifier.height(8.dp)) // Add space between buttons
        Button(onClick = { /* TODO: Handle Sign up */ }) {
            Text(text = "Sign up")
        }
    }
}
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Access more with an account",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (username == "VVVBB" && password == "@bcd1234") {
                onLoginSuccess()
            } else {
                errorMessage = "Invalid credentials, please try again."
            }
        }) {
            Text(text = "Log in")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* Handle Sign up */ }) {
            Text(text = "Sign up")
        }
    }
}

@Composable
fun MovieListScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val movieList = listOf(
        // Dummy movie data (replace with actual images)
        R.drawable.login_page_image,
        R.drawable.login_page_image,
        R.drawable.login_page_image,
        R.drawable.login_page_image,
        R.drawable.login_page_image,
        R.drawable.login_page_image,
        R.drawable.login_page_image,
        R.drawable.login_page_image
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Movies") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { /* Handle search action */ }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Buttons Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columns
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(movieList.size) { index ->
                MovieButton(imageResId = movieList[index])
            }
        }
    }
}

@Composable
fun MovieButton(imageResId: Int) {
    // Button containing an image of the movie
    Button(
        onClick = { /* Handle button click */ },
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp) // Set width for button
            .height(180.dp) // Set height for button
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Movie Image",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MovieDetailsScreen(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Movie Image
        Image(
            painter = painterResource(R.drawable.login_page_image),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 16.dp)
        )

        // Movie Rating
        Text(
            text = "Rating: ${movie.Rating}",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Movie Title
        Text(
            text = "Title: ${movie.Title}",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Movie Category
        Text(
            text = "Category: ${movie.Genre}",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Movie Header
        Text(
            text = "Header: ${movie.Header}",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Movie Plot Summary
        Text(
            text = "Plot Summary: ${movie.Plot}",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Other Ratings (if any)
        Text(
            text = "Other Ratings",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Horizontal Scrollable List of Buttons
        Text(
            text = "Other Rating Categories",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movie.OtherRatingTypes) { relatedMovie ->
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MovieListApplicationTheme {
        MainPage()
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MovieListApplicationTheme {
        LoginScreen(onLoginSuccess = { /* No action needed for preview */ })
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    MovieListApplicationTheme {
        MovieListScreen()
    }
}
@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    MovieListApplicationTheme {
        val mockMovie = Movie(
        )
        MovieDetailsScreen(mockMovie)
    }
}
