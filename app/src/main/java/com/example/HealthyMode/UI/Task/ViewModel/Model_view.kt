//package com.example.HealthyMode.UI.Task.ViewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.cachedIn
//import com.example.HealthyMode.Paging.MainPagingSource
//import com.example.HealthyMode.Repository.TodoRepository
//import com.example.HealthyMode.TodoDatabase.Todo
//import com.example.HealthyMode.TodoDatabase.TodoDao
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class Model_view(
//    private val dao: TodoDao,private val Repository: TodoRepository): ViewModel() {
//    val allplans:LiveData<List<Todo>> =Repository.planslist
//    fun insertTodo(todo: Todo)
//    {
//        viewModelScope.launch (Dispatchers.IO){
//            Repository.insertTodo(todo)
//        }
//    }
//    val data = Pager(
//        PagingConfig(
//            pageSize = 20,
//            enablePlaceholders = false,
//            initialLoadSize = 20
//        ),
//    ) {
//        MainPagingSource(dao)
//    }.flow.cachedIn(viewModelScope)
//
//}
//
//class MainViewModelFactory(
//    private val dao: TodoDao,private val Repository: TodoRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(Model_view::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return Model_view(dao,Repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
