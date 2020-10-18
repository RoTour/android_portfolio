package com.novyapp.test.fasttypingtraining.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import java.lang.IllegalArgumentException

class LoadingViewModel(
    private val repository: WordsRepositoryInterface
) : ViewModel() {

    val wordList = repository.wordList
    val wordSetup = repository.wordsSetup

    fun checkIsDataLoaded(): Boolean {
        return ((wordList.value is Result.Success) &&
                !((wordList.value as Result.Success).data.isNullOrEmpty()))
                &&
                (!(wordSetup.value?.current?.value?.word.isNullOrBlank()) &&
                        !(wordSetup.value?.next?.value?.word.isNullOrBlank()))
    }
}

class LoadingViewModelFactory(
    private val repository: WordsRepositoryInterface
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoadingViewModel::class.java))
            return LoadingViewModel(repository) as T
        throw IllegalArgumentException("Bad argument in the LoadingViewModelFactory")
    }
}