package com.example.HealthyMode.TodoDatabase.Repository

import androidx.lifecycle.LiveData
import com.example.HealthyMode.TodoDatabase.Todo
interface TodoRepository{
    suspend fun insertTodo(todo: Todo)
    suspend fun deleteTodo(todo:Todo)
    suspend fun updateTodo(todo:Todo)
    suspend fun getTodoById(id:Int):Todo?
    fun  getList(): LiveData<List<Todo>>
}