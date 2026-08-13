package com.example.model

data class Movie(
    val id: String,
    val title: String,
    val description: String = "",
    val poster: String = "",
    val backdrop: String = "",
    val year: String = "2024",
    val duration: String = "2h 15m",
    val genre: String = "Action",
    val rating: String = "8.5",
    val quality: String = "4K",
    val language: String = "Bangla & English",
    val cast: String = "",
    val director: String = "",
    val featured: Boolean = false,
    val videoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class Category(
    val id: String,
    val name: String,
    val iconName: String = "Tag"
)

enum class Screen(val route: String) {
    HOME("home"),
    SEARCH("search"),
    WATCHLIST("watchlist"),
    CATEGORIES("categories"),
    PROFILE("profile"),
    ADMIN("admin"),
    DETAILS("details/{movieId}"),
    PLAYER("player/{movieId}")
}
