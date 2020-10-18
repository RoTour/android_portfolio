package com.novyapp.dowittodolist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDatabaseDao {

    @Query("SELECT * FROM todos_table ORDER BY due_date_long ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos_table WHERE done_state_fakebool=0 ORDER BY due_date_long ASC")
    fun getNotDoneTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos_table WHERE todoId = :id")
    fun getTodoById(id: Long) : Todo?

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Query("DELETE FROM todos_table")
    fun clear()

    @Query("UPDATE todos_table SET done_state_fakebool = :state WHERE todoId = :id")
    fun updateState(id: Long, state: Int)
}