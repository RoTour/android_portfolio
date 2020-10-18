package com.novyapp.test.utlimatetodolist.ui.addoredit

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import com.novyapp.test.utlimatetodolist.data.local.Task
import kotlinx.coroutines.launch

class AddOrEditViewModel(
    private val repository: ITasksRepository
) : ViewModel() {


    fun saveNewTask(title: Editable, description: Editable) {
        viewModelScope.launch {
            repository.saveTask(Task(taskTitle =  title.toString(), taskText = description.toString()))
        }
    }

    fun saveNewTask(newTask: Task) {
        viewModelScope.launch {
            repository.saveTask(newTask)
        }
    }


    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask)
        }
    }

    fun deleteThisTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

}

class AddOrEditViewModelFactory(
    private val repository: ITasksRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(AddOrEditViewModel::class.java)){
            return AddOrEditViewModel(repository) as T
        }
        throw IllegalArgumentException("Bad Arguments provided in AddOrEditViewModelFactory")
    }
}