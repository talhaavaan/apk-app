package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_wallpapers")
data class FavoriteWallpaper(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val category: String,
    val likesCount: Int,
    val resolution: String,
    val author: String,
    val colorHex: String,
    val addedAt: Long = System.currentTimeMillis()
)
