package com.example.movielistapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.movielistapplication.data.Movie
import com.example.movielistapplication.data.MovieViewModel
import com.example.movielistapplication.ui.theme.MovieListApplicationTheme

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_page")
    data object LoginScreen : Screen("login")
    data object MovieListScreen : Screen("movie_list")
    data object MovieDetailsScreen : Screen("movie_details/{movieId}")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieListApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Set up the NavHostController
                    val navController = rememberNavController()
                    // NavHost: This is where we define the different routes
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MainScreen.route // Start with the Login screen
                    ) {
                        composable(Screen.MainScreen.route) {
                            MainPage(
                                onClickLogin = {
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                            )
                        }
                        composable(Screen.LoginScreen.route) {
                            LoginScreen (navController = navController) {
                                navController.navigate(Screen.MovieListScreen.route)
                            }
                        }

                        composable(Screen.MovieListScreen.route) {
                            MovieListScreen(
                                navController = navController,
                                onClickDetail = {
                                    navController.navigate(Screen.MovieDetailsScreen.route)
                                }
                            )
                        }

                        composable(Screen.MovieDetailsScreen.route) {
                            val movie = Movie()
                            MovieDetailsScreen(navController, movie)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainPage(onClickLogin: () -> Unit, modifier: Modifier = Modifier) {
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
        Button(onClick = { onClickLogin() }) {
            Text(text = "Log in")
        }
        Spacer(modifier = Modifier.height(8.dp)) // Add space between buttons
        Button(onClick = { /* TODO: Handle Sign up */ }) {
            Text(text = "Sign up")
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { PaddingValues->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues),
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
                    if (!(username == "VVVBB" && password == "@bcd1234")) {
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
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavController, onClickDetail:() -> Unit) {
    val movieViewModel = remember { MovieViewModel() }
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        movieViewModel.fetchMovies("Movie") // You can set an initial query
    }
    // Observe the movie list from the ViewModel
    val movieList = movieViewModel.movieList

    // Log all movies retrieved
    if (movieList.isNotEmpty()) {
        Log.d("JACK", "Retrieved movies list: ${movieList.joinToString { it.Title }}")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { PaddingValues->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues)
            ) {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        movieViewModel.fetchMovies(it)
                    },
                    label = { Text("Search Movies") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            movieViewModel.fetchMovies(searchQuery)
                        }
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
                        MovieButton(imageUrl = movieList[index].Poster, onClickDetail)
                    }
                }
            }
        }
    )
}

@Composable
fun MovieButton(imageUrl: String, onClickedDetail: () -> Unit) {
    // Button containing an image of the movie
    Button(
        onClick = { onClickedDetail() },
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp) // Set width for button
            .height(180.dp) // Set height for button
    ) {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = "Movie Image",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(navController: NavController, movie: Movie) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { PaddingValues->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues)
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
                    items(movie.OtherRatingTypes) {
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MovieListApplicationTheme {
        MainPage(onClickLogin = { /* No action needed for preview */ })
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MovieListApplicationTheme {
        val mockNavController = rememberNavController()
        LoginScreen(mockNavController) { /* No action needed for preview */ }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    MovieListApplicationTheme {
        val mockNavController = rememberNavController()
        MovieListScreen(mockNavController, onClickDetail = { /* No action needed for preview */ })
    }
}
@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    MovieListApplicationTheme {
        val mockNavController = rememberNavController()
        val mockMovie = Movie()
        MovieDetailsScreen(mockNavController, mockMovie)
    }
}
