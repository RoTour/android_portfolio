package com.novyapp.test.utlimatetodolist.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "tasks_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id:  Long = UUID.randomUUID().mostSignificantBits.toLong(),

    @ColumnInfo(name = "task_title")
    var taskTitle: String = "Task Title",

    @ColumnInfo(name = "task_text")
    var taskText: String = "",

    @ColumnInfo(name = "is_done_bool")
    var isCompleted: Boolean = false
) : Parcelable