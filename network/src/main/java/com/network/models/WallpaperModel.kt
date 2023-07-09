package com.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.network.roomdb.Converters
import com.network.utils.AppConstants
import java.io.Serializable
import java.util.Date
@Entity(tableName = AppConstants.TBL_WALLPAPERS)
@TypeConverters(Converters::class)
data class WallpaperModel(
    @PrimaryKey
    var id: String = "",
    var imageUrl: String? = "",
    var category: String? = "", //we are using collection name as category so we can filter data if available in room database against selected collection(category)
    var createdAt: Date? = Date(),
    var active: Boolean? = true
) : Serializable
