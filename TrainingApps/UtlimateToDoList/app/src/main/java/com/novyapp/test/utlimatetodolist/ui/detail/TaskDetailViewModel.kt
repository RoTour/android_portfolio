package com.novyapp.test.utlimatetodolist.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TaskDetailViewModel(
    private val repository: ITasksRepository
) : ViewModel() {
    fun deleteThisTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }
}

class TaskDetailViewModelFactory(
    private val repository: ITasksRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(TaskDetailViewModel::class.java)){
            return TaskDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Bad arg provided in TaskDetailViewModelFactory")
    }
}