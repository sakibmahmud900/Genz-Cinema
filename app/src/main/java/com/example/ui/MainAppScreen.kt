package com.example.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ads.AdManager
import com.example.ads.BannerAdView
import com.example.auth.AuthManager
import com.example.data.AppDatabase
import com.example.data.DefaultMovies
import com.example.data.MovieEntity
import com.example.data.WatchlistEntity
import com.example.model.Movie
import com.example.model.Screen
import com.example.ui.ads.RewardedAdDialog
import com.example.ui.auth.AuthDialog
import com.example.ui.player.VideoPlayerScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MovieDetailsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WatchlistScreen
import kotlinx.coroutines.launch

@Composable
fun MainAppScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    val database = remember { AppDatabase.getInstance(context) }
    val movieDao = remember { database.movieDao() }

    val moviesEntities by movieDao.getAllMovies().collectAsState(initial = emptyList())
    val watchlistEntities by movieDao.getWatchlist().collectAsState(initial = emptyList())

    val movies = remember(moviesEntities) {
        if (moviesEntities.isEmpty()) {
            DefaultMovies.sampleMovies
        } else {
            moviesEntities.map { it.toMovie() }
        }
    }

    val watchlistMovieIds = remember(watchlistEntities) {
        watchlistEntities.map { it.movieId }
    }

    // Populate initial sample movies into database if empty
    LaunchedEffect(moviesEntities) {
        if (moviesEntities.isEmpty()) {
            movieDao.insertAllMovies(DefaultMovies.sampleMovies.map { MovieEntity.fromMovie(it) })
        }
    }

    val currentUser by AuthManager.currentUser.collectAsState()
    val isAdmin by AuthManager.isAdmin.collectAsState()

    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf("") }

    var showAuthDialog by remember { mutableStateOf(false) }
    var pendingMovieToPlay by remember { mutableStateOf<Movie?>(null) }
    var showRewardedAdDialog by remember { mutableStateOf(false) }

    fun handleWatchVideo(movie: Movie) {
        if (AdManager.isDailyRewardedAdWatchedToday(context)) {
            selectedMovie = movie
            currentScreen = Screen.PLAYER
        } else {
            pendingMovieToPlay = movie
            showRewardedAdDialog = true
        }
    }

    fun navigateToScreenWithAd(target: Screen) {
        activity?.let { act ->
            AdManager.showInterstitialAd(act) {
                currentScreen = target
            }
        } ?: run {
            currentScreen = target
        }
    }

    Scaffold(
        bottomBar = {
            if (currentScreen != Screen.PLAYER && currentScreen != Screen.ADMIN) {
                Column {
                    BannerAdView()
                    NavigationBar(
                        containerColor = Color(0xFF0A0A0A),
                        contentColor = Color.White
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == Screen.HOME,
                            onClick = { navigateToScreenWithAd(Screen.HOME) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE50914),
                                selectedTextColor = Color(0xFFE50914),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.SEARCH,
                            onClick = { navigateToScreenWithAd(Screen.SEARCH) },
                            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                            label = { Text("Search") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE50914),
                                selectedTextColor = Color(0xFFE50914),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.WATCHLIST,
                            onClick = { navigateToScreenWithAd(Screen.WATCHLIST) },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (watchlistMovieIds.isNotEmpty()) {
                                            Badge(containerColor = Color(0xFFE50914)) {
                                                Text(watchlistMovieIds.size.toString(), color = Color.White)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Favorite, contentDescription = "Watchlist")
                                }
                            },
                            label = { Text("Watchlist") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE50914),
                                selectedTextColor = Color(0xFFE50914),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.CATEGORIES,
                            onClick = { navigateToScreenWithAd(Screen.CATEGORIES) },
                            icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
                            label = { Text("Categories") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE50914),
                                selectedTextColor = Color(0xFFE50914),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.PROFILE,
                            onClick = { currentScreen = Screen.PROFILE },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE50914),
                                selectedTextColor = Color(0xFFE50914),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0A0A0A))
        ) {
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        movies = movies,
                        onMovieClick = { movie ->
                            selectedMovie = movie
                            navigateToScreenWithAd(Screen.DETAILS)
                        },
                        onWatchClick = { movie -> handleWatchVideo(movie) },
                        onSeeAllClick = { currentScreen = Screen.SEARCH }
                    )
                }

                Screen.SEARCH -> {
                    SearchScreen(
                        movies = movies,
                        initialCategory = selectedCategoryFilter,
                        onMovieClick = { movie ->
                            selectedMovie = movie
                            navigateToScreenWithAd(Screen.DETAILS)
                        }
                    )
                }

                Screen.DETAILS -> {
                    selectedMovie?.let { movie ->
                        MovieDetailsScreen(
                            movie = movie,
                            allMovies = movies,
                            isInWatchlist = watchlistMovieIds.contains(movie.id),
                            onBackClick = { currentScreen = Screen.HOME },
                            onWatchClick = { handleWatchVideo(it) },
                            onToggleWatchlist = { m ->
                                scope.launch {
                                    if (watchlistMovieIds.contains(m.id)) {
                                        movieDao.removeFromWatchlist(m.id)
                                    } else {
                                        movieDao.addToWatchlist(WatchlistEntity(movieId = m.id))
                                    }
                                }
                            },
                            onMovieClick = { m ->
                                selectedMovie = m
                            }
                        )
                    }
                }

                Screen.PLAYER -> {
                    selectedMovie?.let { movie ->
                        VideoPlayerScreen(
                            movie = movie,
                            onBackClick = { currentScreen = Screen.DETAILS }
                        )
                    }
                }

                Screen.WATCHLIST -> {
                    WatchlistScreen(
                        movies = movies,
                        watchlistMovieIds = watchlistMovieIds,
                        onMovieClick = { movie ->
                            selectedMovie = movie
                            navigateToScreenWithAd(Screen.DETAILS)
                        }
                    )
                }

                Screen.CATEGORIES -> {
                    CategoriesScreen(
                        categories = DefaultMovies.defaultCategories,
                        onCategoryClick = { cat ->
                            selectedCategoryFilter = cat.name
                            currentScreen = Screen.SEARCH
                        }
                    )
                }

                Screen.PROFILE -> {
                    ProfileScreen(
                        user = currentUser,
                        isAdmin = isAdmin,
                        watchlistCount = watchlistMovieIds.size,
                        onOpenAuth = { showAuthDialog = true },
                        onSignOut = { AuthManager.signOut() },
                        onOpenAdmin = { currentScreen = Screen.ADMIN },
                        onOpenWatchlist = { currentScreen = Screen.WATCHLIST }
                    )
                }

                Screen.ADMIN -> {
                    AdminScreen(
                        movies = movies,
                        onAddMovie = { m ->
                            scope.launch {
                                movieDao.insertMovie(MovieEntity.fromMovie(m))
                                // Save to Firestore
                                try {
                                    val firestoreMap = hashMapOf(
                                        "title" to m.title,
                                        "description" to m.description,
                                        "poster" to m.poster,
                                        "backdrop" to m.backdrop,
                                        "year" to m.year,
                                        "duration" to m.duration,
                                        "genre" to m.genre,
                                        "rating" to m.rating,
                                        "quality" to m.quality,
                                        "language" to m.language,
                                        "cast" to m.cast,
                                        "director" to m.director,
                                        "featured" to m.featured,
                                        "videoUrl" to m.videoUrl
                                    )
                                    com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                        .collection("movies")
                                        .document(m.id)
                                        .set(firestoreMap)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                // Send instant push notification with custom cinema bell icon
                                com.example.notification.NotificationHelper.sendMovieNotification(
                                    context = context,
                                    movieTitle = m.title,
                                    genre = m.genre
                                )
                            }
                        },
                        onUpdateMovie = { m ->
                            scope.launch {
                                movieDao.insertMovie(MovieEntity.fromMovie(m))
                            }
                        },
                        onDeleteMovie = { id ->
                            scope.launch {
                                movieDao.deleteMovie(id)
                            }
                        },
                        onBackClick = { currentScreen = Screen.PROFILE }
                    )
                }
            }

            // Auth Dialog Overlay
            if (showAuthDialog) {
                AuthDialog(
                    onDismiss = { showAuthDialog = false },
                    onSuccess = { showAuthDialog = false }
                )
            }

            // Daily First-Time Rewarded Ad Dialog
            if (showRewardedAdDialog && pendingMovieToPlay != null) {
                RewardedAdDialog(
                    movie = pendingMovieToPlay!!,
                    onDismiss = {
                        showRewardedAdDialog = false
                        pendingMovieToPlay = null
                    },
                    onUnlockedAndPlay = {
                        selectedMovie = pendingMovieToPlay
                        showRewardedAdDialog = false
                        currentScreen = Screen.PLAYER
                        pendingMovieToPlay = null
                    }
                )
            }
        }
    }
}
