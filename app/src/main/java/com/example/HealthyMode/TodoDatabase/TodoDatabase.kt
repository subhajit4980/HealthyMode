package com.example.HealthyMode.TodoDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 3)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao():TodoDao
}