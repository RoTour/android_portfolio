package com.novyapp.dowittodolist.main

import android.app.Application
import android.util.Log
import android.view.View
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.ViewModel
import com.novyapp.dowittodolist.database.Todo
import com.novyapp.dowittodolist.database.TodoDatabaseDao
import com.novyapp.dowittodolist.utils.generateRandomDate
import kotlinx.coroutines.*
import java.util.*

class MainViewModel(
    private val database: TodoDatabaseDao,
    private val application: Application
) : ViewModel(){

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val todos = database.getNotDoneTodos()

    private var lastDeletedTodo: Todo = Todo()


    fun onButton1Clicked(){
        addAutoGeneratedTodo()
        logDatabase()
    }

    private fun logDatabase() {
        Log.i("MainViewModel","//////////////////////////////////////////")
        todos.value?.forEach {
            Log.i("MainViewModel","id: ${it.todoId}, dueDate: ${it.dueDate}")
        }
    }

    fun onButton2Clicked(){
        uiScope.launch {
            clearDatabase()
        }
    }

    fun restoreDeletedTodo(){
        toggleTodoState(lastDeletedTodo.todoId)
    }

    fun onRemoveAnimationFinished(id: Long, view: View){
        uiScope.launch {
            toggleTodoState(id)
            view.isClickable = true
        }
    }

    private fun toggleTodoState(id: Long) {
        uiScope.launch {
            val todo = getTodoByIdFromDatabase(id)
            todo?.let {
                if(it.doneState == 0){
                    it.doneState = 1
                    lastDeletedTodo = it
                    Log.i("MainViewModel","Toggling $id to true")
                }
                else{
                    it.doneState = 0
                    Log.i("MainViewModel","Toggling $id to false")
                }
                updateInDatabase(it)
            }
        }
    }

    private fun addAutoGeneratedTodo() {
        val date: Long = generateRandomDate()
        val newTodo = Todo(task = "auto generated Todo", dueDate = date)
        uiScope.launch {
            insertInDatabase(newTodo)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /*
    *
    * Database Coroutines
    *
     */

    private suspend fun getTodoByIdFromDatabase(id: Long) : Todo? {
        return withContext(Dispatchers.IO){
            database.getTodoById(id)
        }
    }

    private suspend fun clearDatabase() {
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    private suspend fun insertInDatabase(todo: Todo){
        withContext(Dispatchers.IO){
            database.insert(todo)
        }
    }

    private suspend fun updateInDatabase(todo: Todo){
        withContext(Dispatchers.IO){
            database.update(todo)
        }
    }
}
