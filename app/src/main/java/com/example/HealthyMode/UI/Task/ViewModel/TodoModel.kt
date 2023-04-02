package com.example.HealthyMode.UI.Task.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HealthyMode.Repository.TodoRepository
import com.example.HealthyMode.TodoDatabase.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoModel(private val Repository: TodoRepository): ViewModel() {
    val allplans:LiveData<List<Todo>> =Repository.planslist
//    fun getTodo():LiveData<List<Todo>>{
//        return Repository.getTodo()
//    }
    fun insertTodo(todo: Todo)
    {
//        as it is suspended we use corotine
        viewModelScope.launch (Dispatchers.IO){
            Repository.insertTodo(todo)
        }
    }
    fun deletetodo(todo: Todo)
    {
//        as it is suspended we use corotine
        viewModelScope.launch (Dispatchers.IO){
            Repository.deleteTodo(todo)
        }
    }
    fun updatetodo(todo: Todo)
    {
//        as it is suspended we use corotine
        viewModelScope.launch (Dispatchers.IO){
            Repository.updateTodo(todo)
        }
    }

}