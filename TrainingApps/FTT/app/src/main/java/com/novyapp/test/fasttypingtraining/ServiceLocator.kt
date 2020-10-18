package com.novyapp.test.fasttypingtraining

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.novyapp.test.fasttypingtraining.data.source.DefaultWordsRepository
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import com.novyapp.test.fasttypingtraining.data.source.local.WordsDatabase
import com.novyapp.test.fasttypingtraining.data.source.local.WordsLocalDataSource
import com.novyapp.test.fasttypingtraining.data.source.local.getDatabase
import com.novyapp.test.fasttypingtraining.data.source.remote.WordsRemoteDataSource
import kotlinx.coroutines.runBlocking


object ServiceLocator {

    private val lock = Any()
    private var localSource: WordsLocalDataSource? = null
    private var database: WordsDatabase? = null

    @Volatile
    var wordsRepository: WordsRepositoryInterface? = null
        @VisibleForTesting set
    
    fun provideWordsRepository(context: Context) : WordsRepositoryInterface{
        synchronized(this){
            return wordsRepository ?: createWordsRepository(context)
        }
    }

    private fun createWordsRepository(context: Context): DefaultWordsRepository {
        val newRepo = DefaultWordsRepository(getLocalDataSource(context), WordsRemoteDataSource)
        wordsRepository = newRepo
        return newRepo
    }

    private fun getLocalDataSource(context: Context): WordsLocalDataSource {
        if(localSource == null){
            database = getDatabase(context)
            localSource = WordsLocalDataSource(database!!)
        }
        return localSource!!
    }

    @VisibleForTesting
    fun resetRepository(){
        synchronized(lock){
            runBlocking {
                WordsRemoteDataSource.deleteAllWords()
            }
//            database?.apply {
//                clearAllTables()
//                close()
//            }
            database = null
            wordsRepository = null
        }
    }
}