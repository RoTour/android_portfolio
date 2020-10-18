package com.novyapp.dowittodolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todos_table")
data class Todo(

    @PrimaryKey(autoGenerate = true)
    val todoId: Long = 0L,

    @ColumnInfo(name = "task_string")
    var task: String = "",

    @ColumnInfo(name = "due_date_long")
    var dueDate: Long = System.currentTimeMillis(),

    //SQLite does not handle boolean type so we use Int ( 0 = false, else = true)
    @ColumnInfo(name = "done_state_fakebool")
    var doneState: Int = 0




)