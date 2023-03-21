package com.example.HealthyMode.TodoDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 3)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao():TodoDao

    companion object{
        @Volatile
        private var INSTANCE:TodoDatabase?=null
        fun getDatabase(context: Context):TodoDatabase{
            if (INSTANCE==null)
            {
                synchronized(this){
                    INSTANCE=Room.databaseBuilder(
                        context,TodoDatabase::class.java,"TODO"
                    ).createFromAsset("Todo.db").build()
                }
            }
            return INSTANCE!!
        }
    }
}