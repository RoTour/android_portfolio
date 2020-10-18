package com.novyapp.test.utlimatetodolist.ui.stats

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import com.novyapp.test.utlimatetodolist.data.Result
import com.novyapp.test.utlimatetodolist.providers.ResourcesProvider
import java.lang.IllegalArgumentException

class StatsViewModel(
    private val repository: ITasksRepository,
    private val resProvider: ResourcesProvider
) : ViewModel() {
    private val numberOfCompletedTasks = repository.getObservableTasks()
    val statsNumberOfCompletedTasks = Transformations.map(numberOfCompletedTasks) {
        var result = 0
        var resString = ""
        if(it is Result.Success){
            it.data.forEach {task ->
                if (task.isCompleted) result++
            }
            resString = resProvider.getString(R.string.total_completed_tasks, result)
        } else if (it is Result.Error){
            resString = resProvider.getString(R.string.errorLoadingStats)
        }
        return@map resString
    }
}

class StatsViewModelFactory(
    private val repository: ITasksRepository,
    private val resProvider: ResourcesProvider
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatsViewModel::class.java)){
            return StatsViewModel(repository, resProvider) as T
        }
        throw IllegalArgumentException("Error while building the StatsViewModel")
    }
}