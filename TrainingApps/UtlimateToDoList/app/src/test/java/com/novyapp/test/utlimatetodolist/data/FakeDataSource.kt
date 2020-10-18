package com.novyapp.test.utlimatetodolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.utlimatetodolist.data.local.Task

class FakeDataSource(
    starterList: List<Task> = emptyList()
) : IDataSource {

    private val data: LinkedHashMap<Long, Task> = LinkedHashMap()

    init {
        starterList.forEach { data[it.id] = it }
    }

    override suspend fun getTask(id: Long): Result<Task> {
        data[id]?.let { return Result.Success(it) }
            ?: run { return Result.Error(Exception("Task Not Found (id:$id)")) }
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return Result.Success(data.map { it.value })
    }

    override fun getObservableTask(id: Long): LiveData<Result<Task>> {
        data[id]?.let { return MutableLiveData(Result.Success(it)) }
            ?: run { return MutableLiveData(Result.Error(Exception("Task Not Found (id:$id)"))) }
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
        val updateTask = Task(task.id, task.taskTitle, task.taskText, !task.isCompleted)
        data[task.id] = updateTask
    }
}