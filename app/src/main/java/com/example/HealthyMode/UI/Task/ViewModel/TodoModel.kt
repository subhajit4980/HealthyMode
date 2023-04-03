package com.example.HealthyMode.UI.Task.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HealthyMode.TodoDatabase.Repository.TodoRepository
import com.example.HealthyMode.TodoDatabase.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoModel @Inject constructor(private val Repository: TodoRepository) : ViewModel() {
    val allplans: LiveData<List<Todo>> = Repository.getList()
    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    fun insertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.insertTodo(todo)
        }
    }

    fun onTaskSwiped(task: Todo) = viewModelScope.launch {
        Repository.deleteTodo(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }
    fun onTaskCheckedChanged(task: Todo, isChecked: Boolean) = viewModelScope.launch {
        Repository.updateTodo(task.copy(status = isChecked))
    }
    fun onUndoDeleteClick(task: Todo) = viewModelScope.launch {
        Repository.insertTodo(task)
    }

    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: Todo) : TasksEvent()
    }
}
