package com.novyapp.test.utlimatetodolist.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.novyapp.test.utlimatetodolist.data.IDataSource
import com.novyapp.test.utlimatetodolist.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskLocalDataSource(
    private val taskDao: TasksDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IDataSource{
    override suspend fun getTask(id: Long): Result<Task> = withContext(ioDispatcher){
        return@withContext try {
            val result = taskDao.getTask(id)
            Result.Success(result)
        } catch (e:Exception){
            Result.Error(e)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            val results = taskDao.getAllTasks()
            Result.Success(results)
        } catch (e: Exception){
            Result.Error(e)
        }
    }

    override fun getObservableTask(id: Long): LiveData<Result<Task>> {
        return taskDao.observeTask(id).map {
            Result.Success(it)
        }
    }

    override fun getObservableTasks(): LiveData<Result<List<Task>>> {
        return taskDao.observeTasks().map {
            Result.Success(it)
        }
    }

    override suspend fun saveTask(task: Task) {
        taskDao.insertAll(task)
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        taskDao.insertAll(*tasks)
    }

    override suspend fun deleteTasks() {
        taskDao.clear()
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }

    override suspend fun updateTask(updatedTask: Task) {
        taskDao.updateTask(updatedTask)
    }

    override suspend fun toggleIsComplete(task: Task) {
        taskDao.toggleIsComplete(task.id, !task.isCompleted)
    }
}