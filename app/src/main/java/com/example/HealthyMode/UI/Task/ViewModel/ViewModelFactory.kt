package com.example.HealthyMode.UI.Task.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.HealthyMode.Repository.TodoRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val Repository: TodoRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TodoModel::class.java))
        {
            return TodoModel(Repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}