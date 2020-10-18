package com.novyapp.test.utlimatetodolist.data

import androidx.lifecycle.LiveData
import com.novyapp.test.utlimatetodolist.data.local.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultTasksRepository(
    private val localDataSource: IDataSource,
    private val remoteDataSource: IDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ITasksRepository {
    override suspend fun getTask(id: Long): Result<Task> {
        return localDataSource.getTask(id)
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return localDataSource.getTasks()
    }

    override fun getObservableTask(id: Long): LiveData<Result<Task>> {
        return localDataSource.getObservableTask(id)
    }

    override fun getObservableTasks(): LiveData<Result<List<Task>>> {
        return localDataSource.getObservableTasks()
    }

    override suspend fun saveTask(task: Task) {
        localDataSource.saveTask(task)
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        localDataSource.saveTasks(*tasks)
    }

    override suspend fun deleteTask(id: Long) {
        localDataSource.deleteTask(id)
    }

    override suspend fun deleteTasks() = withContext(ioDispatcher){
        localDataSource.deleteTasks()
    }

    override suspend fun refreshFromRemote() {
        val result = remoteDataSource.getTasks()
        if(result is Result.Success){
            localDataSource.deleteTasks()
            localDataSource.saveTasks(*result.data.toTypedArray())
        } else if(result is Result.Error) {
            throw result.exception
        }
    }

    override suspend fun toggleIsComplete(task: Task) {
        localDataSource.toggleIsComplete(task)
    }

    override suspend fun updateTask(updatedTask: Task) {
        localDataSource.updateTask(updatedTask)
    }
}