package com.novyapp.test.utlimatetodolist.providers

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.novyapp.test.utlimatetodolist.data.DefaultTasksRepository
import com.novyapp.test.utlimatetodolist.data.IDataSource
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import com.novyapp.test.utlimatetodolist.data.local.TaskLocalDataSource
import com.novyapp.test.utlimatetodolist.data.local.getDatabase
import com.novyapp.test.utlimatetodolist.data.remote.TaskRemoteDataSource

object ServiceLocator {

    private var database: IDataSource? = null

    @Volatile
    var repository: ITasksRepository? = null
        @VisibleForTesting set


    fun provideRepository(context: Context): ITasksRepository {
        return repository
            ?: buildRepository(
                context
            )
    }

    private fun buildRepository(context: Context): ITasksRepository {
        return DefaultTasksRepository(
            database
                ?: buildDatabase(
                    context
                ),
            TaskRemoteDataSource
        )
    }

    private fun buildDatabase(context: Context): IDataSource {
        return TaskLocalDataSource(getDatabase(context).tasksDatabaseDao)
    }
}