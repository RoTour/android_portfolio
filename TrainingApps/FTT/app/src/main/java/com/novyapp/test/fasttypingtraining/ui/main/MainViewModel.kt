package com.novyapp.test.fasttypingtraining.ui.main

import androidx.lifecycle.*
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import timber.log.Timber

class MainViewModel(
    private val wordsRepository: WordsRepositoryInterface
) : ViewModel() {

    val wordList = wordsRepository.wordList
    val wordSetup = wordsRepository.wordsSetup

    fun checkIsDataLoaded(): Boolean {
        return ((wordList.value is Result.Success) &&
                !((wordList.value as Result.Success).data.isNullOrEmpty()))
                &&
                (!(wordSetup.value?.current?.value?.word.isNullOrBlank()) &&
                 !(wordSetup.value?.next?.value?.word.isNullOrBlank()))
    }

}


class MainViewModelFactory(
    private val repo: WordsRepositoryInterface
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(repo) as T
        throw IllegalArgumentException("Bad argument in the MainViewModelFactory class")
    }
}