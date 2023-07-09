package com.network.roomdb

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    /*@TypeConverter
    fun toString(value: String): String {
        return value
    }

    @TypeConverter
    fun fromString(value: String): String {
        return value
    }*/

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}