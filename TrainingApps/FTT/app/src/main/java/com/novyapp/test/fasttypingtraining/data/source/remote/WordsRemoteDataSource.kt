package com.novyapp.test.fasttypingtraining.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.source.WordsDataSourceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WordsRemoteDataSource : WordsDataSourceInterface {

    val services = Network.fttaApi
    private val ioDispatcher = Dispatchers.IO
    private val observableWords = MutableLiveData<Result<List<DomainWord>>>()

    override suspend fun getAllWords(): Result<List<DomainWord>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(services.getWordListAsync().await().asDomainWords())
        } catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun saveWords(vararg words: DomainWord) {
        /**
         * Not allowed to edit the remote storage
         */
    }

    override suspend fun saveWords(results: Result.Success<List<DomainWord>>) {
        /**
         * Not allowed to edit the remote storage
         */
    }

    override suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>> =
        withContext(ioDispatcher){
        return@withContext try {
            val results = getAllWords()
            if(results is Result.Success){
                val toReturn: MutableList<DomainWord> = mutableListOf()
                for(i in 0 until numOfWords) toReturn.add(results.data.random())
                Result.Success(toReturn.toList())
            }
            Result.Error(Exception("Results is Error but not caught"))
        } catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun refreshWords() {
//        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWords() {
        /**
         * Not allowed to edit the remote storage
         */
    }

    override suspend fun deleteOnWord(word: String) {
        /**
         * Not allowed to edit the remote storage
         */
    }

    override fun observeWords(): LiveData<Result<List<DomainWord>>> {
//        TODO("Not yet implemented")
        return observableWords
    }
}