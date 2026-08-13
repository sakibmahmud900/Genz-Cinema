package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val poster: String,
    val backdrop: String,
    val year: String,
    val duration: String,
    val genre: String,
    val rating: String,
    val quality: String,
    val language: String,
    val cast: String,
    val director: String,
    val featured: Boolean,
    val videoUrl: String,
    val createdAt: Long
) {
    fun toMovie() = Movie(
        id = id,
        title = title,
        description = description,
        poster = poster,
        backdrop = backdrop,
        year = year,
        duration = duration,
        genre = genre,
        rating = rating,
        quality = quality,
        language = language,
        cast = cast,
        director = director,
        featured = featured,
        videoUrl = videoUrl,
        createdAt = createdAt
    )

    companion object {
        fun fromMovie(movie: Movie) = MovieEntity(
            id = movie.id,
            title = movie.title,
            description = movie.description,
            poster = movie.poster,
            backdrop = movie.backdrop,
            year = movie.year,
            duration = movie.duration,
            genre = movie.genre,
            rating = movie.rating,
            quality = movie.quality,
            language = movie.language,
            cast = movie.cast,
            director = movie.director,
            featured = movie.featured,
            videoUrl = movie.videoUrl,
            createdAt = movie.createdAt
        )
    }
}

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val movieId: String,
    val addedAt: Long = System.currentTimeMillis()
)
