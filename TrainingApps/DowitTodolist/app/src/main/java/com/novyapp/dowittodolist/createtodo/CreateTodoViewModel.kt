package com.novyapp.dowittodolist.createtodo

import android.app.Application
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novyapp.dowittodolist.database.Todo
import com.novyapp.dowittodolist.database.TodoDatabaseDao
import com.novyapp.dowittodolist.utils.getLongDateFromDMY
import com.novyapp.dowittodolist.utils.mylogs
import kotlinx.coroutines.*

class CreateTodoViewModel(
    private val database: TodoDatabaseDao,
    private val application: Application
) : ViewModel(){

    private val _eventPickupDateButtonPressed = MutableLiveData<Boolean>()
        val eventPickupDateButtonPressed: LiveData<Boolean>
            get() = _eventPickupDateButtonPressed

    private val _eventCreateTodoButtonPressed = MutableLiveData<Boolean>()
        val eventCreateTodoButtonPressed: LiveData<Boolean>
            get() = _eventCreateTodoButtonPressed

    private val _eventNavigateToMain = MutableLiveData<Boolean>()
        val eventNavigateToMain: LiveData<Boolean>
            get() = _eventNavigateToMain

    private val _eventErrorFieldMissing = MutableLiveData<Boolean>()
    val eventErrorFieldMissing: LiveData<Boolean>
        get() = _eventErrorFieldMissing

    private val _todoTask = MutableLiveData<String>()
        val todoTask : LiveData<String>
            get() = _todoTask

    private val _dueDate = MutableLiveData<Long>()
        val  dueDate : LiveData<Long>
            get() = _dueDate




    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _eventCreateTodoButtonPressed.value = false
        _eventCreateTodoButtonPressed.value = false
        _eventNavigateToMain.value = false
        _eventErrorFieldMissing.value = false

        _dueDate.value = System.currentTimeMillis()
        _todoTask.value = ""
    }


    fun newDateSelected(day: Int, month: Int, year: Int){
        _dueDate.value = getLongDateFromDMY(day, month+1, year)
    }


    // Events

    fun onPickupDateButtonClicked(){
        _eventPickupDateButtonPressed.value = true
        mylogs("PickupClicked")
    }

    fun onPickupDateButtonClickHandled(){
        _eventPickupDateButtonPressed.value = false
    }

    fun onCreateTodoButtonClicked(){
        _eventCreateTodoButtonPressed.value = true
        mylogs("CreateClicked")
    }

    fun onCreateTodoButtonClickHandled(){
        _eventCreateTodoButtonPressed.value = false
    }

    private fun onNavigateToMain() {
        _eventNavigateToMain.value = true
    }

    private fun onNavigateToMainHandled() {
        _eventNavigateToMain.value = false
    }

    private fun errorFieldMissing() {
        _eventErrorFieldMissing.value = true
    }

    private fun errorFieldMissingHandled() {
        _eventErrorFieldMissing.value = false
    }


    // Database manipulations

    fun pushTodoInDatabase() {
        uiScope.launch {
            if(_todoTask.value == null || _todoTask.value == ""){
                errorFieldMissing()
                errorFieldMissingHandled()
            }
            else{
                val newTodo = Todo(task = todoTask.value!!, dueDate = dueDate.value!!)
                insertIntoDatabase(newTodo)
                onNavigateToMain()
                onNavigateToMainHandled()
            }

        }
    }

    private suspend fun insertIntoDatabase(newTodo: Todo) {
        withContext(Dispatchers.IO){
            database.insert(newTodo)
        }
    }

    // Overrides

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun todoTaskUpdated(text: Editable?) {
        _todoTask.value = text.toString()
    }
}