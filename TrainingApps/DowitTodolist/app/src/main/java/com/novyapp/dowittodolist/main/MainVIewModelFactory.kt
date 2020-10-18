package com.novyapp.dowittodolist.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.novyapp.dowittodolist.database.TodoDatabaseDao
import java.lang.IllegalArgumentException

class MainVIewModelFactory(
    private val database: TodoDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(database, application) as T
        }
        throw IllegalArgumentException("wtf bro args invalid on MainViewModelFactory")
    }
}