package com.novyapp.test.fasttypingtraining.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.CompactResult
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.domain.WordsSetup

interface WordsRepositoryInterface {
    var wordList: LiveData<Result<List<DomainWord>>>
    var wordsSetup: LiveData<WordsSetup>
    var stateIsDataLoaded: MutableLiveData<Boolean>

    suspend fun getAllWords(forceRefresh: Boolean = false): Result<List<DomainWord>>
    suspend fun saveWords(vararg words: DomainWord)
    suspend fun refreshWords()
    suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>>
    suspend fun deleteAllWords()
    suspend fun deleteOnWord(word: String)
    fun observeWords(): LiveData<Result<List<DomainWord>>>
    fun checkIsDataLoaded(): Boolean {
        return ((wordList.value is Result.Success)
                &&
                !((wordList.value as Result.Success).data.isNullOrEmpty()))
    }
}