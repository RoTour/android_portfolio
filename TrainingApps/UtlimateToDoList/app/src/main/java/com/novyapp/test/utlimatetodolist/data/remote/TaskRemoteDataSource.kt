package com.novyapp.test.utlimatetodolist.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.utlimatetodolist.data.IDataSource
import com.novyapp.test.utlimatetodolist.data.Result
import com.novyapp.test.utlimatetodolist.data.local.Task
import java.lang.Error
import java.lang.Exception

object TaskRemoteDataSource: IDataSource {

    private val data = LinkedHashMap<Long, Task>()

    override suspend fun getTask(id: Long): Result<Task> {
        val task = data[id]
        task?.let { return Result.Success(it) }
        return Result.Error(Exception("Task not found"))
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return Result.Success(data.map { it.value })
    }

    override fun getObservableTask(id: Long): LiveData<Result<Task>> {
        val task = data[id]
        task?.let { return MutableLiveData(Result.Success(it)) }
        return MutableLiveData(Result.Error(Exception("Task not found")))
    }

    override fun getObservableTasks(): LiveData<Result<List<Task>>> {
        return MutableLiveData(Result.Success(data.map { it.value }))
    }

    override suspend fun saveTask(task: Task) {
        data[task.id] = task
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        tasks.forEach { data[it.id] = it }
    }

    override suspend fun deleteTasks() {
        data.clear()
    }

    override suspend fun deleteTask(id: Long) {
        data.remove(id)
    }

    override suspend fun updateTask(updatedTask: Task) {
        data[updatedTask.id] = updatedTask
    }

    override suspend fun toggleIsComplete(task: Task) {
        val updatedTask = Task(task.id, task.taskTitle, task.taskText, !task.isCompleted)
        data[task.id] = updatedTask
    }
}