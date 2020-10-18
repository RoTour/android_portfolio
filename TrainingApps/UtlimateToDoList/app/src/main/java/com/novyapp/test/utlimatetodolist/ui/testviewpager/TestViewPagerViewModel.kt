package com.novyapp.test.utlimatetodolist.ui.testviewpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import java.lang.IllegalArgumentException

class TestViewPagerViewModel(
    val repository: ITasksRepository
) : ViewModel() {
    val allTasks = repository.getObservableTasks()
}

class TestViewPagerViewModelFactory(
    val repository: ITasksRepository
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TestViewPagerViewModel::class.java)){
            return TestViewPagerViewModel(repository) as T
        }
        throw IllegalArgumentException("Bad arg provided in TesViewPagerViewModelFactory")
    }
}