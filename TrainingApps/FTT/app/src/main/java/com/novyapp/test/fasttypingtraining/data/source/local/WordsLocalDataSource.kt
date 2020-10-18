package com.novyapp.test.fasttypingtraining.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.domain.asDatabaseWords
import com.novyapp.test.fasttypingtraining.data.source.WordsDataSourceInterface
import com.novyapp.test.fasttypingtraining.data.Result.Success
import com.novyapp.test.fasttypingtraining.data.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.Exception

class WordsLocalDataSource(
    private val database: WordsDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WordsDataSourceInterface{
    override suspend fun getAllWords(): Result<List<DomainWord>> = withContext(ioDispatcher){
        return@withContext try {
            Success(database.wordsDatabaseDao.getAllWords().asDomainWords())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override fun observeWords(): LiveData<Result<List<DomainWord>>> {
        return database.wordsDatabaseDao.observeTasks().map {
            Success(it.asDomainWords())
        }
    }

    override suspend fun saveWords(vararg words: DomainWord) = withContext(ioDispatcher){
        database.wordsDatabaseDao.insertAll(words = *words.asDatabaseWords())
    }

    override suspend fun saveWords(results: Result.Success<List<DomainWord>>) = withContext(ioDispatcher){
        database.wordsDatabaseDao.insertAll(*results.data.asDatabaseWords())
    }

    override suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(database.wordsDatabaseDao.getRandomRows(numOfWords).asDomainWords())
        } catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun refreshWords() {
//        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWords() {
//        TODO("Not yet implemented")
    }

    override suspend fun deleteOnWord(word: String) {
//        TODO("Not yet implemented")
    }
}

