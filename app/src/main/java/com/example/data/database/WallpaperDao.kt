package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    @Query("SELECT * FROM favorite_wallpapers ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteWallpaper>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpapers WHERE id = :id)")
    fun isFavoriteFlow(id: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wallpapers WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(fav: FavoriteWallpaper)

    @Query("DELETE FROM favorite_wallpapers WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)
}
