package com.example.data.repository

import android.content.Context
import com.example.data.database.FavoriteWallpaper
import com.example.data.database.WallpaperDao
import com.example.data.model.Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class WallpaperRepository(private val wallpaperDao: WallpaperDao) {

    // Local DB favorites
    val favorites: Flow<List<FavoriteWallpaper>> = wallpaperDao.getAllFavorites()

    fun isFavoriteFlow(id: String): Flow<Boolean> = wallpaperDao.isFavoriteFlow(id)

    suspend fun toggleFavorite(wallpaper: Wallpaper) {
        val isAlreadyFav = wallpaperDao.isFavorite(wallpaper.id)
        if (isAlreadyFav) {
            wallpaperDao.deleteFavoriteById(wallpaper.id)
        } else {
            wallpaperDao.insertFavorite(wallpaper.toFavorite())
        }
    }

    suspend fun addFavorite(wallpaper: Wallpaper) {
        wallpaperDao.insertFavorite(wallpaper.toFavorite())
    }

    suspend fun removeFavorite(id: String) {
        wallpaperDao.deleteFavoriteById(id)
    }

    // Dynamic catalog management for real-time admin simulations
    private val _customWallpapers = MutableStateFlow<List<Wallpaper>>(emptyList())
    val customWallpapers: StateFlow<List<Wallpaper>> = _customWallpapers.asStateFlow()

    fun uploadWallpaper(newWall: Wallpaper) {
        _customWallpapers.value = listOf(newWall) + _customWallpapers.value
    }

    fun deleteUploadedWallpaper(id: String) {
        _customWallpapers.value = _customWallpapers.value.filter { it.id != id }
    }

    // Curated Library (Mix of Pexels/Unsplash optimized items)
    private val curatedWallpapers = listOf(
        // === Cars & Supercars ===
        Wallpaper(
            id = "car_1",
            title = "Cyberpunk Nissan GT-R",
            url = "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?auto=format&fit=crop&q=85&w=1080",
            category = "Supercars",
            likesCount = 1420,
            resolution = "3840 x 2160",
            author = "Kyon M.",
            colorHex = "#00F0FF",
            tags = listOf("cars", "supercars", "neon", "cyberpunk", "gtr", "dark"),
            isLive = false,
            isPremium = true
        ),
        Wallpaper(
            id = "car_2",
            title = "Aesthetic Porsche 911 Neon",
            url = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&q=85&w=1080",
            category = "Cars",
            likesCount = 985,
            resolution = "3840 x 2160",
            author = "Alex S.",
            colorHex = "#8B5CF6",
            tags = listOf("cars", "porsche", "neon", "purple", "aesthetic"),
            isLive = false
        ),
        Wallpaper(
            id = "car_3",
            title = "Audi R8 Emerald Glow",
            url = "https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?auto=format&fit=crop&q=85&w=1080",
            category = "Supercars",
            likesCount = 1102,
            resolution = "3840 x 2160",
            author = "Sven S.",
            colorHex = "#00FF66",
            tags = listOf("supercars", "audi", "green", "neon")
        ),
        Wallpaper(
            id = "bike_1",
            title = "Futuristic Ducati Beast",
            url = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&q=85&w=1080",
            category = "Bikes",
            likesCount = 880,
            resolution = "3840 x 2160",
            author = "MotoArt",
            colorHex = "#FF3366",
            tags = listOf("bikes", "ducati", "red", "neon", "speed")
        ),

        // === Anime & AI Art ===
        Wallpaper(
            id = "anime_1",
            title = "Retro Cyber Tokyo",
            url = "https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&q=85&w=1080",
            category = "Anime",
            likesCount = 2031,
            resolution = "4000 x 6000",
            author = "Jezael M.",
            colorHex = "#8B5CF6",
            tags = listOf("anime", "tokyo", "cyberpunk", "illustration", "neon"),
            isLive = true
        ),
        Wallpaper(
            id = "ai_1",
            title = "Artificial Mech Knight",
            url = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&q=85&w=1080",
            category = "AI Art",
            likesCount = 1530,
            resolution = "4000 x 6000",
            author = "AIGen Studio",
            colorHex = "#00F0FF",
            tags = listOf("ai art", "mech", "robot", "abstract", "purple")
        ),
        Wallpaper(
            id = "anime_2",
            title = "Astronaut Dreamer Sketch",
            url = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&q=85&w=1080",
            category = "Anime",
            likesCount = 1354,
            resolution = "3840 x 2160",
            author = "SpaceVibe",
            colorHex = "#8B5CF6",
            tags = listOf("anime", "space", "galaxy", "digital")
        ),

        // === Dark & AMOLED ===
        Wallpaper(
            id = "dark_1",
            title = "AMOLED Liquid Gold",
            url = "https://images.unsplash.com/photo-1541701494587-cb58502866ab?auto=format&fit=crop&q=85&w=1080",
            category = "AMOLED",
            likesCount = 2450,
            resolution = "3840 x 2160",
            author = "Art Deco",
            colorHex = "#FFCC00",
            tags = listOf("dark", "amoled", "abstract", "gold", "minimal"),
            isLive = true,
            isPremium = true
        ),
        Wallpaper(
            id = "dark_2",
            title = "Minimalist Abyss Grid",
            url = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&q=85&w=1080",
            category = "Dark",
            likesCount = 1890,
            resolution = "4000 x 6000",
            author = "T4LHA",
            colorHex = "#000000",
            tags = listOf("dark", "minimal", "black", "grid", "amoled")
        ),
        Wallpaper(
            id = "dark_3",
            title = "Cyber Interface Lines",
            url = "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?auto=format&fit=crop&q=85&w=1080",
            category = "AMOLED",
            likesCount = 1435,
            resolution = "3840 x 2160",
            author = "HackerVibe",
            colorHex = "#00F0FF",
            tags = listOf("amoled", "cyber", "green", "neon", "matrix")
        ),

        // === Gaming & Neon ===
        Wallpaper(
            id = "gaming_1",
            title = "Futuristic Battlestation",
            url = "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&q=85&w=1080",
            category = "Gaming",
            likesCount = 1762,
            resolution = "3840 x 2160",
            author = "GamerX",
            colorHex = "#FF3366",
            tags = listOf("gaming", "neon", "cyberpunk", "led", "pc"),
            isLive = false
        ),
        Wallpaper(
            id = "neon_1",
            title = "Tokyo Retro Grid Neon",
            url = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&q=85&w=1080",
            category = "Neon",
            likesCount = 1920,
            resolution = "3840 x 2160",
            author = "Saito",
            colorHex = "#00F0FF",
            tags = listOf("neon", "cyberpunk", "grid", "vaporwave")
        ),

        // === Space & Nature ===
        Wallpaper(
            id = "space_1",
            title = "Neon Nebula Collapse",
            url = "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&q=85&w=1080",
            category = "Space",
            likesCount = 2100,
            resolution = "4000 x 6000",
            author = "Hubble",
            colorHex = "#8B5CF6",
            tags = listOf("space", "nebula", "galaxy", "stars", "4k")
        ),
        Wallpaper(
            id = "nature_1",
            title = "Misty Forest Dark Peaks",
            url = "https://images.unsplash.com/photo-1475924156734-496f6cac6ec1?auto=format&fit=crop&q=85&w=1080",
            category = "Nature",
            likesCount = 1205,
            resolution = "3840 x 2160",
            author = "Aaron S.",
            colorHex = "#FFFFFF",
            tags = listOf("nature", "forest", "dark", "misty", "mountains")
        ),

        // === Islamic Aesthetic ===
        Wallpaper(
            id = "islamic_1",
            title = "Sacred Golden Geometry",
            url = "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&q=85&w=1080",
            category = "Islamic",
            likesCount = 1690,
            resolution = "3840 x 2160",
            author = "Sufi M.",
            colorHex = "#FFCC00",
            tags = listOf("islamic", "arabic", "architecture", "mosque", "gold"),
            isLive = false
        ),
        Wallpaper(
            id = "islamic_2",
            title = "Ramadan Glow Minarets",
            url = "https://images.unsplash.com/photo-1542856391-010fb87dcfed?auto=format&fit=crop&q=85&w=1080",
            category = "Islamic",
            likesCount = 1450,
            resolution = "4000 x 6000",
            author = "Ibrahim K.",
            colorHex = "#FFCC00",
            tags = listOf("islamic", "mosque", "night", "glow"),
            isLive = true
        ),

        // === Abstract & Minimal ===
        Wallpaper(
            id = "minimal_1",
            title = "Geometric Dark Poly",
            url = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&q=85&w=1080",
            category = "Minimal",
            likesCount = 1320,
            resolution = "3840 x 2160",
            author = "Simplistic",
            colorHex = "#FFFFFF",
            tags = listOf("minimal", "abstract", "dark", "lines")
        ),
        Wallpaper(
            id = "abstract_1",
            title = "Psychedelic Glass Smoke",
            url = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?auto=format&fit=crop&q=85&w=1080",
            category = "Abstract",
            likesCount = 1968,
            resolution = "3840 x 2160",
            author = "VaporLab",
            colorHex = "#8B5CF6",
            tags = listOf("abstract", "smoke", "glassmorphism", "neon"),
            isPremium = true
        )
    )

    fun getAllWallpapers(): List<Wallpaper> {
        return _customWallpapers.value + curatedWallpapers
    }

    fun getWallpapersByCategory(category: String): List<Wallpaper> {
        val all = getAllWallpapers()
        if (category.equals("Trending", ignoreCase = true)) {
            return all.sortedByDescending { it.likesCount }
        }
        if (category.equals("4K", ignoreCase = true)) {
            return all.filter { it.resolution.contains("3840") }
        }
        if (category.equals("Live Wallpapers", ignoreCase = true)) {
            return all.filter { it.isLive }
        }
        return all.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun searchWallpapers(query: String, selectedColor: String? = null, selectedCategory: String? = null): List<Wallpaper> {
        var list = getAllWallpapers()

        if (query.isNotEmpty()) {
            list = list.filter { wall ->
                wall.title.contains(query, ignoreCase = true) ||
                        wall.tags.any { tag -> tag.contains(query, ignoreCase = true) } ||
                        wall.category.contains(query, ignoreCase = true)
            }
        }

        if (selectedCategory != null) {
            list = list.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        if (selectedColor != null) {
            list = list.filter { it.colorHex.equals(selectedColor, ignoreCase = true) }
        }

        return list
    }

    fun incrementLike(id: String): Wallpaper? {
        // Simple mock incremental mechanics
        return getAllWallpapers().find { it.id == id }?.let { original ->
            val updated = original.copy(likesCount = original.likesCount + 1)
            // If in custom wallpapers, update
            if (_customWallpapers.value.any { it.id == id }) {
                _customWallpapers.value = _customWallpapers.value.map {
                    if (it.id == id) updated else it
                }
            }
            updated
        }
    }
}
