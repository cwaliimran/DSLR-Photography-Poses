package com.network.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.network.models.WallpaperModel

@Dao
interface DaoWallpapers {
    @Insert(onConflict = REPLACE)
    suspend fun addWallpaper(question: WallpaperModel)

    @Insert(onConflict = REPLACE)
    suspend fun insertWallpapers(questions: List<WallpaperModel>): List<Long>

    @Update
    suspend fun updateWallpaper(question: WallpaperModel)

    @Delete
    suspend fun deleteWallpaper(question: WallpaperModel)

    @Query("SELECT * FROM wallpapers")
    fun getAllWallpapers(): LiveData<List<WallpaperModel>>



    @Query("SELECT * FROM wallpapers where category = :category")
    fun getWallpapersByCategory(
        category: String,
    ): LiveData<List<WallpaperModel>>

    @Query("DELETE FROM wallpapers")
    fun deleteAllWallpapers()

    @Query("SELECT (SELECT COUNT(*) FROM wallpapers) == 0")
    fun isDbEmpty(): LiveData<Boolean>
}