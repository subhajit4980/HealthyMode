package com.example.HealthyMode.TodoDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * from Todo ORDER BY time ASC,status DESC")
    fun getTodo():LiveData<List<Todo>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)
    @Delete
    suspend fun deleteTodo(todo: Todo)
    @Update
    suspend fun updateTodo(todo: Todo)
    @Query("SELECT * from Todo WHERE id= :id")
    suspend fun getTodoById(id: Int): Todo?

}