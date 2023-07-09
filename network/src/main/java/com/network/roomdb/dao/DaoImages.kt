package com.network.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.network.models.ImagesModel

@Dao
interface DaoImages {
    @Insert(onConflict = REPLACE)
    suspend fun addImage(question: ImagesModel)

    @Insert(onConflict = REPLACE)
    suspend fun insertImages(questions: List<ImagesModel>): List<Long>

    @Update
    suspend fun updateImage(question: ImagesModel)

    @Delete
    suspend fun deleteImage(question: ImagesModel)

    @Query("SELECT * FROM images")
    fun getAllImages(): LiveData<List<ImagesModel>>



    @Query("SELECT * FROM images where category = :category")
    fun getImagesByCategory(
        category: String,
    ): LiveData<List<ImagesModel>>

    @Query("DELETE FROM images")
    fun deleteAllImages()

    @Query("SELECT (SELECT COUNT(*) FROM images) == 0")
    fun isDbEmpty(): LiveData<Boolean>
}