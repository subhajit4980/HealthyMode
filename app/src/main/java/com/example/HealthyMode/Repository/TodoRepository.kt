package com.example.HealthyMode.Repository

import androidx.lifecycle.LiveData
import com.example.HealthyMode.TodoDatabase.Todo
import com.example.HealthyMode.TodoDatabase.TodoDao

class TodoRepository(val todoDao: TodoDao){
//    fun getTodo(): LiveData<List<Todo>>
//    {
//        return todoDao.getTodo()
//    }
    val planslist:LiveData<List<Todo>> =todoDao.getTodo()
    suspend fun insertTodo(todo:Todo)
    {
        todoDao.insertTodo(todo)
    }
    suspend fun deleteTodo(todo:Todo)
    {
        todoDao.deleteTodo(todo)
    }
    suspend fun updateTodo(todo:Todo)
    {
        todoDao.updateTodo(todo)
    }
}