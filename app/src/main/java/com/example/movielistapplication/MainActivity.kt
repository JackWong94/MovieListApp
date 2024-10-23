package com.example.movielistapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    MainPage()
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

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MovieListApplicationTheme {
        MainPage()
    }
}