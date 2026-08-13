package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Movie
import java.util.UUID

@Composable
fun AdminScreen(
    movies: List<Movie>,
    onAddMovie: (Movie) -> Unit,
    onUpdateMovie: (Movie) -> Unit,
    onDeleteMovie: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var editingMovieId by remember { mutableStateOf<String?>(null) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var poster by remember { mutableStateOf("") }
    var backdrop by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("2024") }
    var duration by remember { mutableStateOf("2h 15m") }
    var genre by remember { mutableStateOf("Action") }
    var rating by remember { mutableStateOf("8.5") }
    var quality by remember { mutableStateOf("4K") }
    var language by remember { mutableStateOf("Bangla & English") }
    var cast by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var featured by remember { mutableStateOf(false) }

    fun populateForEdit(movie: Movie) {
        editingMovieId = movie.id
        title = movie.title
        description = movie.description
        poster = movie.poster
        backdrop = movie.backdrop
        videoUrl = movie.videoUrl
        year = movie.year
        duration = movie.duration
        genre = movie.genre
        rating = movie.rating
        quality = movie.quality
        language = movie.language
        cast = movie.cast
        director = movie.director
        featured = movie.featured
    }

    fun resetForm() {
        editingMovieId = null
        title = ""
        description = ""
        poster = ""
        backdrop = ""
        videoUrl = ""
        year = "2024"
        duration = "2h 15m"
        genre = "Action"
        rating = "8.5"
        quality = "4K"
        language = "Bangla & English"
        cast = ""
        director = ""
        featured = false
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFF6B35),
        unfocusedBorderColor = Color(0xFF2A2A2A),
        focusedContainerColor = Color(0xFF1A1A1A),
        unfocusedContainerColor = Color(0xFF1A1A1A),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "🔐 Admin Movie Control Panel",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dashboard Stats Summary
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = movies.size.toString(), color = Color(0xFFE50914), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(text = "Total Movies", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = movies.count { it.featured }.toString(), color = Color(0xFFFF6B35), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(text = "Featured", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Form Title
            item {
                Text(
                    text = if (editingMovieId != null) "✏️ Edit Movie" else "➕ Add New Movie",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Input Fields
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Movie Title *") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = videoUrl,
                        onValueChange = { videoUrl = it },
                        label = { Text("Video URL (Vimeo/YouTube/Streamable/MP4) *") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = poster,
                        onValueChange = { poster = it },
                        label = { Text("Poster Image URL") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = backdrop,
                        onValueChange = { backdrop = it },
                        label = { Text("Backdrop Image URL") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = { Text("Year") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Duration") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = genre,
                            onValueChange = { genre = it },
                            label = { Text("Genre") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = rating,
                            onValueChange = { rating = it },
                            label = { Text("Rating") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = quality,
                            onValueChange = { quality = it },
                            label = { Text("Quality (4K/HD)") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = language,
                            onValueChange = { language = it },
                            label = { Text("Language") },
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                    }
                    OutlinedTextField(
                        value = director,
                        onValueChange = { director = it },
                        label = { Text("Director") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = cast,
                        onValueChange = { cast = it },
                        label = { Text("Cast") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = featured,
                            onCheckedChange = { featured = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFE50914))
                        )
                        Text(text = "Feature on Hero Carousel", color = Color.White, fontSize = 14.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (title.isBlank() || videoUrl.isBlank()) return@Button
                                val newOrUpdatedMovie = Movie(
                                    id = editingMovieId ?: UUID.randomUUID().toString(),
                                    title = title.trim(),
                                    description = description.trim(),
                                    poster = poster.trim(),
                                    backdrop = backdrop.trim(),
                                    year = year.trim(),
                                    duration = duration.trim(),
                                    genre = genre.trim(),
                                    rating = rating.trim(),
                                    quality = quality.trim(),
                                    language = language.trim(),
                                    cast = cast.trim(),
                                    director = director.trim(),
                                    featured = featured,
                                    videoUrl = videoUrl.trim()
                                )

                                if (editingMovieId != null) {
                                    onUpdateMovie(newOrUpdatedMovie)
                                } else {
                                    onAddMovie(newOrUpdatedMovie)
                                }
                                resetForm()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (editingMovieId != null) "💾 Update Movie" else "➕ Save Movie",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (editingMovieId != null) {
                            Button(
                                onClick = { resetForm() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A2A)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }

            // Existing Movies List Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "📋 Manage Existing Movies",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Existing Movies Items
            items(movies) { movie ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = movie.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "${movie.genre} • ${movie.year} • ${movie.quality}", color = Color.Gray, fontSize = 12.sp)
                        }
                        IconButton(onClick = { populateForEdit(movie) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFFFF6B35))
                        }
                        IconButton(onClick = { onDeleteMovie(movie.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE50914))
                        }
                    }
                }
            }
        }
    }
}
