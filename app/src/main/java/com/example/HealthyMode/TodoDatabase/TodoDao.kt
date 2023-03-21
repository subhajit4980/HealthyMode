package com.example.HealthyMode.TodoDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * from Todo")
    fun getTodo():LiveData<List<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo)
    @Delete
    suspend fun deleteTodo(todo: Todo)
    @Update
    suspend fun updateTodo(todo: Todo)

}