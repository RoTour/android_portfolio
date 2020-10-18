package com.novyapp.test.utlimatetodolist.ui.displaylist

import androidx.lifecycle.*
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import com.novyapp.test.utlimatetodolist.data.Result
import com.novyapp.test.utlimatetodolist.data.local.Task
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException

enum class TaskFilter {
    ALL, DONE, TODO
}

class ListDisplayViewModel(
    private val repository: ITasksRepository
) : ViewModel() {

    val allTasks: LiveData<Result<List<Task>>> = repository.getObservableTasks()
    private val _eventUpdateUIIsNeeded = MutableLiveData<Task?>()
    val eventUpdateUIIsNeeded: LiveData<Task?>
        get() = _eventUpdateUIIsNeeded
    var currentFilter = TaskFilter.ALL
    val forceRefresh = MutableLiveData<Boolean>(false)
    val displayedTasks: LiveData<List<Task>> = forceRefresh.switchMap { refreshIsNeeded ->
        if (refreshIsNeeded) {
            viewModelScope.launch {
                repository.refreshFromRemote()
            }
        }

        repository.getObservableTasks().switchMap { filter(it) }
    }

    private fun filter(data: Result<List<Task>>): LiveData<List<Task>> {
        val result = MutableLiveData<List<Task>>()
        if (data is Result.Success) {
            result.value = applyFilter(data.data, currentFilter)
        } else {
            result.value = emptyList()
        }
        return result
    }

    private fun applyFilter(tasks: List<Task>, currentFilter: TaskFilter): List<Task> {
        val result = ArrayList<Task>()
        for (task in tasks) {
            when (currentFilter) {
                TaskFilter.TODO -> {
                    if (!task.isCompleted) result.add(task)
                }
                TaskFilter.DONE -> {
                    if (task.isCompleted) result.add(task)
                }
                else -> {
                    result.add(task)
                }
            }
        }
        return result
    }


    init {
        _eventUpdateUIIsNeeded.value = null
    }

    fun resetData() {
        viewModelScope.launch { repository.deleteTasks() }
    }


    fun changeState(task: Task, completed: Boolean) = viewModelScope.launch {
        Timber.i("VM UPDATE TO: $completed (${!task.isCompleted})")
        repository.toggleIsComplete(task)
//        _eventUpdateUIIsNeeded.value = task
    }

    fun toggleFilter(): TaskFilter{
        currentFilter = when (currentFilter) {
            TaskFilter.TODO -> TaskFilter.DONE
            TaskFilter.DONE -> TaskFilter.ALL
            else -> TaskFilter.TODO
        }
        forceRefresh.value = false
        return currentFilter
    }


}

class ListDisplayViewModelFactory(
    private val repository: ITasksRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(ListDisplayViewModel::class.java)) {
            return ListDisplayViewModel(repository) as T
        }
        throw IllegalArgumentException("Bad Argument in ListDisplayViewModelFactory. Hint: check in the associated fragment")
    }
}