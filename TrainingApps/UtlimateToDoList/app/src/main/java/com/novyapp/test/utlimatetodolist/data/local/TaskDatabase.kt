package com.novyapp.test.utlimatetodolist.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.selects.select


@Dao
interface TasksDatabaseDao {
    @Query("select * from tasks_table")
    suspend fun getAllTasks(): List<Task>

    @Query("select * from tasks_table where id=:id")
    suspend fun getTask(id:Long): Task

    @Query("select * from tasks_table where id=:id")
    fun observeTask(id: Long): LiveData<Task>

    @Query("select * from tasks_table")
    fun observeTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tasks: Task)

    @Update
    suspend fun updateTask(updatedTask: Task)

    @Query("delete from tasks_table")
    suspend fun clear()

    @Query("DELETE FROM tasks_table WHERE id=:id")
    suspend fun deleteTask(id: Long)

    @Query("UPDATE tasks_table SET is_done_bool=:newValue WHERE id=:id")
    suspend fun toggleIsComplete(id: Long, newValue: Boolean)
}

@Database(entities = [Task::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {
    abstract val tasksDatabaseDao: TasksDatabaseDao
}

private lateinit var INSTANCE: TasksDatabase

fun getDatabase(context: Context): TasksDatabase {
    synchronized(TasksDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context,
                TasksDatabase::class.java,
                "tasks_database"
            ).build()
        }
    }
    return INSTANCE
}