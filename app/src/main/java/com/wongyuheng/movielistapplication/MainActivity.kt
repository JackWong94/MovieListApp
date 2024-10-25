package com.wongyuheng.movielistapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.wongyuheng.movielistapplication.data.MovieDatabase
import com.wongyuheng.movielistapplication.data.MovieRepository
import com.wongyuheng.movielistapplication.data.MovieViewModel
import com.wongyuheng.movielistapplication.ui.theme.AppThemeBlue
import com.wongyuheng.movielistapplication.ui.theme.AppThemeGrey
import com.wongyuheng.movielistapplication.ui.theme.AppThemeLightGrey
import com.wongyuheng.movielistapplication.ui.theme.AppThemeWhite
import com.wongyuheng.movielistapplication.ui.theme.MovieListApplicationTheme
import com.wongyuheng.movielistapplication.utils.AuthUtils
import com.wongyuheng.movielistapplication.utils.StarRating
import com.wongyuheng.movielistapplication.utils.TriangleShape
import com.wongyuheng.movielistapplication.utils.UserPreferences
import com.wongyuheng.movielistapplication.utils.Utils
import com.wongyuheng.movielistapplication.utils.Utils.isValidSearchQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_page")
    data object LoginScreen : Screen("login")
    data object SignUpScreen : Screen("signup")
    data object MovieListScreen : Screen("movie_list")
    data object MovieDetailsScreen : Screen("movie_details/{movieId}")
}
class MainActivity : ComponentActivity() {
    private lateinit var movieViewModel: MovieViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the database
        val database = MovieDatabase.getDatabase(application)
        val movieDao = database.movieDao()
        val repository = MovieRepository(movieDao)
        movieViewModel = ViewModelProvider(this, MovieViewModelFactory(repository))[MovieViewModel::class.java]
        FirebaseApp.initializeApp(this)
        // Initialize UserPreferences
        UserPreferences.init(applicationContext)
        setContent {
            MovieListApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Check if user is logged in
                    val startDestination = if (UserPreferences.isLoggedIn()) {
                        Screen.MovieListScreen.route // Navigate to MovieListScreen if logged in
                    } else {
                        Screen.MainScreen.route // Navigate to MainScreen (Login) if not logged in
                    }

                    // Set up the NavHostController
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(Screen.MainScreen.route) {
                            MainPage(
                                onClickLogin = {
                                    navController.navigate(Screen.LoginScreen.route)
                                },
                                onClickSignup = {
                                    navController.navigate(Screen.SignUpScreen.route)
                                }
                            )
                        }
                        composable(Screen.LoginScreen.route) {
                            LoginScreen (navController = navController) {
                                navController.navigate(Screen.MovieListScreen.route)
                            }
                        }
                        composable(Screen.SignUpScreen.route) {
                            SignUpScreen(navController = navController)
                        }
                        composable(Screen.MovieListScreen.route) {
                            MovieListScreen(
                                navController = navController,
                                movieViewModel = movieViewModel,
                                onClickDetail = {
                                    navController.navigate(Screen.MovieDetailsScreen.route)
                                }
                            )
                        }

                        composable(Screen.MovieDetailsScreen.route) {
                            MovieDetailsScreen(navController, movieViewModel)
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage(onClickLogin: () -> Unit, onClickSignup: () -> Unit, modifier: Modifier = Modifier) {
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
            modifier = Modifier.padding(horizontal = 16.dp),
            color = AppThemeBlue,
            fontSize = 30.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center, // Center-aligns the text
            style = androidx.compose.ui.text.TextStyle(
                letterSpacing = 1.sp, // Correctly using sp for letter spacing
                lineHeight = 38.sp // Correctly using sp for line height
            )
        )
        Spacer(modifier = Modifier.height(40.dp)) // Add space
        Button(onClick = { onClickLogin() },
            colors = ButtonDefaults.buttonColors(AppThemeBlue), // Change to desired color
            shape = RoundedCornerShape(12.dp), // Set rounded corners
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            ) {
            Text(text = "Log in")
        }
        Spacer(modifier = Modifier.height(15.dp)) // Add space
        Button(onClick = { onClickSignup() },
            colors = ButtonDefaults.buttonColors(AppThemeBlue), // Change to desired color
            shape = RoundedCornerShape(12.dp), // Set rounded corners
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
        ) {
            Text(text = "Sign up")
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    BackHandler {
        //Override back button
        navController.navigate(Screen.MainScreen.route)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.MainScreen.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome back !",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = AppThemeBlue,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center, // Center-aligns the text
                    style = androidx.compose.ui.text.TextStyle(
                        letterSpacing = 1.sp, // Correctly using sp for letter spacing
                        lineHeight = 38.sp // Correctly using sp for line height
                    )
                )
                Spacer(modifier = Modifier.height(40.dp)) // Add space
                TextField(
                    value = email,
                    onValueChange = { newText  ->
                        if(Utils.preventSpaces(newText )) {
                            email = newText
                        }
                    },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp)) // Add space
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )
                Spacer(modifier = Modifier.height(40.dp)) // Add space
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Button(onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        // Show an error message to the user
                        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Store credentials securely after successful login
                                    val sharedPreferences = AuthUtils.createEncryptedPrefs(context)
                                    AuthUtils.storeCredentials(sharedPreferences, email, password)
                                    UserPreferences.setLoggedIn(true)
                                    onLoginSuccess()
                                } else {
                                    // Attempt offline login if offline
                                    if (!AuthUtils.isNetworkAvailable(context)) {
                                        val sharedPreferences =
                                            AuthUtils.createEncryptedPrefs(context)
                                        if (AuthUtils.validateOfflineLogin(
                                                sharedPreferences,
                                                email,
                                                password
                                            )
                                        ) {
                                            // Allow access to the app
                                            onLoginSuccess()
                                        } else {
                                            // Inform user of invalid credentials
                                            errorMessage =
                                                "Login failed: ${task.exception?.message}"
                                        }
                                    }

                                }
                            }
                    }
                    /* HARDCODED USERNAME AND PASSWORD
                    if (!(username == "VVVBB" && password == "@bcd1234")) {
                        onLoginSuccess()
                    } else {
                        errorMessage = "Invalid credentials, please try again."
                    }*/
                },
                    colors = ButtonDefaults.buttonColors(AppThemeBlue), // Change to desired color
                    shape = RoundedCornerShape(12.dp), // Set rounded corners
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                ) {
                    Text(text = "Log in")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { navController.navigate(Screen.SignUpScreen.route) },
                    colors = ButtonDefaults.buttonColors(AppThemeBlue), // Change to desired color
                    shape = RoundedCornerShape(12.dp), // Set rounded corners
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                ) {
                    Text(text = "Sign up")
                }
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sign Up") },
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
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email,
                    onValueChange = { newText  ->
                                    if(Utils.preventSpaces(newText )) {
                                        email = newText
                                    }
                    },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp)) // Add space
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                )

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(40.dp)) // Add space
                Button(onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        // Show an error message to the user
                        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Store credentials
                                    val sharedPreferences = AuthUtils.createEncryptedPrefs(context)
                                    AuthUtils.storeCredentials(sharedPreferences, email, password)
                                    navController.navigate(Screen.LoginScreen.route)
                                    Toast.makeText(context, "Sign up successful", Toast.LENGTH_LONG).show()
                                } else {
                                    errorMessage = "Sign up failed: ${task.exception?.message}"
                                }
                            }
                    }
                    },
                    colors = ButtonDefaults.buttonColors(AppThemeBlue), // Change to desired color
                    shape = RoundedCornerShape(12.dp), // Set rounded corners
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                ) {
                    Text(text = "Sign Up")
                }
            }
        }
    )
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    navController: NavController,
    onClickDetail: () -> Unit,
    movieViewModel: MovieViewModel
) {
    // State for showing the logout confirmation dialog
    var showDialog by remember { mutableStateOf(false) }
    BackHandler {
        //Override back button to handle logout
        showDialog = true
    }

    // Function to handle logout
    fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        UserPreferences.clearLoginState()
        // After logout, navigate back to the login screen
        navController.navigate(Screen.MainScreen.route)
    }
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
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                    // Logout confirmation dialog
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Logout Confirmation") },
                            text = { Text("Are you sure you want to log out?") },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        // Launch logout in a coroutine scope
                                        // Use CoroutineScope to call the suspend function
                                        CoroutineScope(Dispatchers.Main).launch {
                                            logout() // Call the logout function
                                        }
                                        showDialog = false // Close the dialog
                                    }
                                ) {
                                    Text("Yes")
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog = false // Close the dialog
                                    }
                                ) {
                                    Text("No")
                                }
                            }
                        )
                    }
                }
            )
        },
        content = { paddingValues->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        if(Utils.preventNewline(it)) {
                            searchQuery = it
                        }
                        if (isValidSearchQuery(searchQuery)) {
                            movieViewModel.fetchMovies(searchQuery)
                        } else {
                            Log.e("MovieListApp", "Invalid search query: $it")
                        }
                    },
                    label = { Text("Search Movies") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .border(
                            BorderStroke(2.dp, AppThemeBlue),
                            shape = MaterialTheme.shapes.medium
                        ) // Blue outline
                        .background(Color.Transparent), // Transparent background
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent, // Background color
                            focusedIndicatorColor = Color.Transparent, // Focused indicator color
                            unfocusedIndicatorColor = Color.Transparent // Unfocused indicator color
                        ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (isValidSearchQuery(searchQuery)) {
                                movieViewModel.fetchMovies(searchQuery)
                            } else {
                                Log.e("MovieListApp", "Invalid search query: $searchQuery")
                            }
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
                        MovieButton(imageUrl = movieList[index].Poster, onClickDetail  = {
                                movieViewModel.setMovieId(movieList[index].imdbID)
                                onClickDetail()
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun MovieButton(imageUrl: String, onClickDetail: () -> Unit){
    // Button containing an image of the movie
    Button(
        onClick = { onClickDetail() },
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RectangleShape,
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp) // Set width for button
            .height(220.dp) // Set height for button
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(16.dp), // Adjust corner radius as needed
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Movie Image",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(navController: NavController, movieViewModel: MovieViewModel) {
    BackHandler {
        //Override back button
        navController.navigate(Screen.MovieListScreen.route)
    }
    // Collect the state from the ViewModel
    val movieDetail by movieViewModel.movieDetail.collectAsState()
    val movieID by movieViewModel.movieId.collectAsState()
    // Fetch movie details when the composable is first composed
    LaunchedEffect(movieID) {
        movieViewModel.fetchMovieDetails(movieID ?: "Movie Details") // Example movie ID
        Log.d("JACK", "Movie Detail: $movieDetail")
    }
    Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(Screen.MovieListScreen.route) }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .height(LocalConfiguration.current.screenHeightDp.dp * 5 / 12)
                    ) {
                        // Background Image
                        val defaultPoster =
                            "https://dummyimage.com/600x800/000/fff.png&text=Background+Image" // Use a valid URL for testing
                        val painter =
                            rememberAsyncImagePainter(model = movieDetail?.Poster ?: defaultPoster)

                        Image(
                            painter = painter,
                            contentDescription = "Background Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0.2f), // Set transparency as needed
                            contentScale = ContentScale.Crop // Crop the image to fill
                        )
                        // White Background Box
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(
                                    TriangleShape(
                                        triangleHeight = 500f,
                                        triangleBase = 1100f
                                    )
                                ) // Triangle cutout shape
                                .background(Color.White) // Set the background to white
                        )
                        Image(
                            contentScale = ContentScale.Fit,
                            painter = rememberAsyncImagePainter(
                                model = movieDetail?.Poster ?: defaultPoster
                            ),
                            contentDescription = "Movie Poster",
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .size(300.dp)
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp)) // Add space
                    // Movie Rating
                    Column (modifier = Modifier.padding(15.dp)) {
                        Row {
                            StarRating(rating = movieDetail?.imdbRating?.toFloat() ?: 0f)
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = "${movieDetail?.imdbRating ?: "0"} / 10",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold,
                                color = AppThemeBlue,
                                letterSpacing = 0.3.sp
                                )
                            )
                            Spacer(modifier = Modifier.padding(20.dp))
                            Text(
                                text = "${movieDetail?.imdbVotes ?: "N/A"} Ratings",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Light,
                                color = AppThemeGrey,
                                fontSize = 20.sp,
                                letterSpacing = 0.3.sp
                            )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp)) // Add space
                        // Movie Title
                        Text(
                            text = "${movieDetail?.Title ?: "N/A"}",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AppThemeGrey,
                                letterSpacing = 0.3.sp
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Movie Category
                        Text(
                            text = "Category: ${movieDetail?.Genre ?: "N/A"}",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = AppThemeLightGrey,
                                letterSpacing = 0.3.sp
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp)) // Add space
                        // Movie Plot Summary
                        Text(
                            text = "Plot Summary",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = AppThemeGrey,
                                letterSpacing = 0.3.sp
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Movie Plot Summary
                        Text(
                            text = "${movieDetail?.Plot ?: "N/A"}",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = AppThemeLightGrey,
                                letterSpacing = 0.3.sp
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Other Ratings (if any)
                        Text(
                            text = "Other Ratings",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            movieDetail?.let { detail ->
                                items(detail.Ratings) { rating ->
                                    val (source, value) = rating
                                    // Display each rating in a Card or any other desired layout
                                    Card(
                                        modifier = Modifier.size(
                                            250.dp,
                                            80.dp
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 8.dp
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = AppThemeWhite,
                                        ),
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .wrapContentHeight()
                                                .background(AppThemeWhite)
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = source,
                                                modifier = Modifier
                                                    .align(Alignment.Start)
                                                    .padding(start = 8.dp, top = 8.dp),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 15.sp,
                                                    color = AppThemeGrey,
                                                    letterSpacing = 0.3.sp
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(5.dp)) // Add space
                                            Text(
                                                text = value,
                                                modifier = Modifier
                                                    .align(Alignment.End) // Aligns this text to the bottom end (bottom right)
                                                    .padding(bottom = 8.dp, end = 8.dp),
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontSize = 15.sp,
                                                    color = AppThemeBlue,
                                                    letterSpacing = 0.3.sp
                                                ),
                                                fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MovieListApplicationTheme {
        MainPage(
            onClickLogin = { /* No action needed for preview */ },
            onClickSignup = { /* No action needed for preview */}
        )
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
        val mockViewModel = MovieViewModel(null)
        MovieListScreen(
            mockNavController,
            onClickDetail = { /* No action needed for preview */ },
            mockViewModel
        )
    }
}
@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    MovieListApplicationTheme {
        val mockNavController = rememberNavController()
        val mockViewModel = MovieViewModel(null)
        MovieDetailsScreen(mockNavController, mockViewModel)
    }
}

@Preview
@Composable
fun PreviewStarRating() {
    StarRating(rating = 7.5f) // Preview with a rating of 7.5
}