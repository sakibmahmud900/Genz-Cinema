package com.example.data

import com.example.model.Category
import com.example.model.Movie

object DefaultMovies {

    val defaultCategories = listOf(
        Category("action", "Action"),
        Category("bengali", "Bengali Special"),
        Category("adventure", "Adventure"),
        Category("scifi", "Sci-Fi"),
        Category("horror", "Horror"),
        Category("drama", "Drama"),
        Category("comedy", "Comedy"),
        Category("thriller", "Thriller"),
        Category("romance", "Romance"),
        Category("animation", "Animation")
    )

    val sampleMovies = listOf(
        Movie(
            id = "demo1",
            title = "The Last Voyage",
            description = "A thrilling adventure across the unknown seas as a brave captain discovers mystical uncharted islands.",
            poster = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=500&auto=format&fit=crop&q=80",
            backdrop = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
            year = "2024",
            duration = "2h 18m",
            genre = "Adventure",
            rating = "8.7",
            quality = "4K",
            language = "Bangla & English",
            cast = "John Doe, Jane Smith",
            director = "James Cameron",
            featured = true,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        Movie(
            id = "demo2",
            title = "Neon Dreams (সাইবার সিটি)",
            description = "In a futuristic cyberpunk metropolis, a renegade hacker unveils a conspiracy that threatens humanity.",
            poster = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=500&auto=format&fit=crop&q=80",
            backdrop = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
            year = "2024",
            duration = "1h 56m",
            genre = "Sci-Fi",
            rating = "8.4",
            quality = "4K",
            language = "Bangla Dubbed",
            cast = "Keanu Reeves, Ana de Armas",
            director = "Denis Villeneuve",
            featured = true,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        ),
        Movie(
            id = "demo3",
            title = "Whispers in the Dark (অন্ধকারের শব্দ)",
            description = "An intense supernatural thriller where silence is the only key to survival in a haunted mountain lodge.",
            poster = "https://images.unsplash.com/photo-1509281373149-e957c6296406?w=500&auto=format&fit=crop&q=80",
            backdrop = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
            year = "2023",
            duration = "1h 42m",
            genre = "Horror",
            rating = "7.9",
            quality = "HD",
            language = "Bangla & English",
            cast = "Ethan Hawke, Toni Collette",
            director = "Jordan Peele",
            featured = false,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        ),
        Movie(
            id = "demo4",
            title = "Bengali Cinema Chronicles (বাংলা সিনেমা স্পেশাল)",
            description = "A celebratory documentary exploring the classic, modern, and indie golden eras of Bengali cinema.",
            poster = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500&auto=format&fit=crop&q=80",
            backdrop = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200&auto=format&fit=crop&q=80",
            year = "2024",
            duration = "2h 05m",
            genre = "Bengali Special",
            rating = "9.1",
            quality = "4K",
            language = "Bengali",
            cast = "Satyajit Ray, Soumitra Chatterjee, Prosenjit",
            director = "Anjan Dutt",
            featured = true,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        ),
        Movie(
            id = "demo5",
            title = "Velocity Rivals (গতির লড়াই)",
            description = "High-octane underground street racing action with custom supercars and relentless police pursuits.",
            poster = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=500&auto=format&fit=crop&q=80",
            backdrop = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=1200&auto=format&fit=crop&q=80",
            year = "2023",
            duration = "2h 10m",
            genre = "Action",
            rating = "8.1",
            quality = "HD",
            language = "Bangla Dubbed",
            cast = "Paul Walker, Vin Diesel",
            director = "Justin Lin",
            featured = false,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
        ),
        Movie(
            id = "demo6",
            title = "Celestial Odyssey",
            description = "An interstellar journey beyond the edge of our galaxy to discover the origins of space and time.",
            poster = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&auto=format&fit=crop&q=80",
            backdrop = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1200&auto=format&fit=crop&q=80",
            year = "2024",
            duration = "2h 30m",
            genre = "Sci-Fi",
            rating = "8.9",
            quality = "4K",
            language = "English",
            cast = "Matthew McConaughey, Jessica Chastain",
            director = "Christopher Nolan",
            featured = false,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        )
    )
}
