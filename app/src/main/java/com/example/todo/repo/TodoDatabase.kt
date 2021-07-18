package com.example.todo.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DB_NAME = "stuff.db"

@Database(entities = [TodoEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoStore(): TodoEntity.Store

    companion object {
        fun newInstance(context: Context) =
            Room.databaseBuilder(context, TodoDatabase::class.java, DB_NAME).build()

        fun newTestInstance(context: Context) =
            Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java).build()
    }
}