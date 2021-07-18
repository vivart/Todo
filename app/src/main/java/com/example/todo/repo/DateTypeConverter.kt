package com.example.todo.repo

import androidx.room.TypeConverter
import java.time.Instant

class DateTypeConverter {

    @TypeConverter
    fun fromInstant(date: Instant?): Long? = date?.toEpochMilli()

    @TypeConverter
    fun toInstant(millisSinceEpoch: Long?): Instant? =
        millisSinceEpoch?.let { Instant.ofEpochMilli(it) }
}