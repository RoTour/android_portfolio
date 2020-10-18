package com.novyapp.dowittodolist.createtodo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.dowittodolist.database.TodoDatabaseDao
import com.novyapp.dowittodolist.main.MainViewModel
import java.lang.IllegalArgumentException

class CreateTodoViewModelFactory(
    private val database: TodoDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(CreateTodoViewModel::class.java)){
            return CreateTodoViewModel(database, application) as T
        }
        throw IllegalArgumentException("wtf bro args invalid on MainViewModelFactory")
    }
}