package com.example.data.model

import com.example.data.database.FavoriteWallpaper

data class Wallpaper(
    val id: String,
    val title: String,
    val url: String,
    val category: String,
    val likesCount: Int,
    val resolution: String,
    val author: String,
    val colorHex: String,
    val tags: List<String>,
    val isLive: Boolean = false,
    val isPremium: Boolean = false
) {
    fun toFavorite(): FavoriteWallpaper {
        return FavoriteWallpaper(
            id = id,
            title = title,
            url = url,
            category = category,
            likesCount = likesCount,
            resolution = resolution,
            author = author,
            colorHex = colorHex
        )
    }

    companion object {
        fun fromFavorite(fav: FavoriteWallpaper): Wallpaper {
            return Wallpaper(
                id = fav.id,
                title = fav.title,
                url = fav.url,
                category = fav.category,
                likesCount = fav.likesCount,
                resolution = fav.resolution,
                author = fav.author,
                colorHex = fav.colorHex,
                tags = listOf(fav.category, fav.title),
                isLive = false,
                isPremium = false
            )
        }
    }
}
