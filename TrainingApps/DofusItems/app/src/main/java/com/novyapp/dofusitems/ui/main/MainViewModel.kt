package com.novyapp.dofusitems.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import com.novyapp.dofusitems.database.getDatabase
import com.novyapp.dofusitems.repository.DofusItemsRepository
import kotlinx.coroutines.*


class MainViewModel(
    application: Application
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = getDatabase(application)
    private val repository = DofusItemsRepository(database)

//    private val _mounts = MutableLiveData<List<DomainDofusMounts>>()
//    val mounts: LiveData<List<DomainDofusMounts>>
//        get() = _mounts

    val dofusMounts = repository.dofusMounts
    init {
        uiScope.launch {
            repository.refreshDofusItems()
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}