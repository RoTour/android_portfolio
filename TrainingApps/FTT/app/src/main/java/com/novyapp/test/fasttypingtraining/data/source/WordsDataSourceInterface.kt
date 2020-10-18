package com.novyapp.test.fasttypingtraining.data.source

import androidx.lifecycle.LiveData
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord

interface WordsDataSourceInterface {
    suspend fun getAllWords(): Result<List<DomainWord>>
    suspend fun saveWords(vararg words: DomainWord)
    suspend fun saveWords(results: Result.Success<List<DomainWord>>)
    suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>>
    suspend fun refreshWords()
    suspend fun deleteAllWords()
    suspend fun deleteOnWord(word: String)
    fun observeWords(): LiveData<Result<List<DomainWord>>>
}