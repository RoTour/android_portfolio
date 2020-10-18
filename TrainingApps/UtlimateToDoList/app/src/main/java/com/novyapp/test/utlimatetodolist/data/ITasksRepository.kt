package com.novyapp.test.utlimatetodolist.data

import androidx.lifecycle.LiveData
import com.novyapp.test.utlimatetodolist.data.local.Task

interface ITasksRepository {
    suspend fun getTask(id: Long): Result<Task>
    suspend fun getTasks(): Result<List<Task>>
    fun getObservableTask(id: Long): LiveData<Result<Task>>
    fun getObservableTasks(): LiveData<Result<List<Task>>>
    suspend fun saveTask(task: Task)
    suspend fun saveTasks(vararg tasks: Task)
    suspend fun deleteTasks()
    suspend fun deleteTask(id: Long)
    suspend fun refreshFromRemote()
    suspend fun updateTask(updatedTask: Task)
    suspend fun toggleIsComplete(task: Task)
}