package com.novyapp.test.fasttypingtraining.data.repository
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.content.res.Resources
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Transformations
//import com.novyapp.test.fasttypingtraining.R
//import com.novyapp.test.fasttypingtraining.REPOSITORY_SHARED_PREFS
//import com.novyapp.test.fasttypingtraining.data.source.local.WordsDatabase
//import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
//import com.novyapp.test.fasttypingtraining.data.domain.asDatabaseWords
//import com.novyapp.test.fasttypingtraining.data.source.local.asDomainWords
//import com.novyapp.test.fasttypingtraining.data.source.remote.Network
//import com.novyapp.test.fasttypingtraining.data.source.remote.NetworkListVersion
//import com.novyapp.test.fasttypingtraining.data.source.remote.asDatabaseWords
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import timber.log.Timber
//import java.io.InputStream
//
//private const val WORD_LIST_VERSION = "wordListVersion"
//private const val NEED_TO_UPDATE = "needToUpdate"
//
//class WordsRepository(
//    private val wordsDatabase: WordsDatabase,
//    val context: Context
//) {
//
//    companion object {
//        var repositoryStateInfo = MutableLiveData<String>()
//    }
//
//    val words: LiveData<List<DomainWord>> =
//        Transformations.map(wordsDatabase.wordsDatabaseDao.observeTasks()) {
//            it.asDomainWords()
//        }
//
//    lateinit var version: NetworkListVersion
//
//    private val sharedPref =
//        context.getSharedPreferences(REPOSITORY_SHARED_PREFS, Context.MODE_PRIVATE)
//
//    suspend fun refreshWords() {
//        withContext(Dispatchers.IO) {
//            try {
//                val editor = sharedPref.edit()
//                // Use the local .txt file to get the words. OUTDATED
//                if(isWordListTxtChanged()){
//                    readWordListOnLocal()
//                    setNeedToUpdateToFalse(editor)
//                }
//
//                // Connect to the network to get the words
//                if (isUpdateFromNetworkNeed()) {
////                    Timber.i("An update is needed from Network")
//                    updateFromNetwork(editor)
//                }
//
//            } catch (e: Exception) {
//                Timber.e(e)
//            }
//        }
//    }
//
//    private suspend fun updateFromNetwork(editor: SharedPreferences.Editor) {
//        try {
//            repositoryStateInfo.postValue(context.getString(R.string.downloadingContent))
//            val results = Network.fttaApi.getWordListAsync().await()
//            repositoryStateInfo.postValue(context.getString(R.string.processingData))
//            wordsDatabase.wordsDatabaseDao.insertAll(*results.asDatabaseWords())
//            editor.putLong(WORD_LIST_VERSION, version.version).apply()
//            repositoryStateInfo.postValue(context.getString(R.string.finished))
//        } catch (e: Exception) {
//            Timber.e(e)
//        }
//    }
//
//    private suspend fun isUpdateFromNetworkNeed(): Boolean {
//        return try {
//            version = Network.fttaApi.getWordListVersionAsync().await()
//            version.version > sharedPref.getLong(WORD_LIST_VERSION, 0)
//        } catch (e: Exception) {
//            Timber.e(e)
//            false
//        }
//    }
//
//    private fun setNeedToUpdateToFalse(editor: SharedPreferences.Editor) {
//        editor.putString(NEED_TO_UPDATE, "false").apply()
//    }
//
//    private fun isWordListTxtChanged(): Boolean {
//        return (sharedPref.getString(NEED_TO_UPDATE, "true") == "true")
//    }
//
//
//    private fun readWordListOnLocal() {
//        try {
//            val inputStream: InputStream = R.raw.usa.openFile()
//            val wordsFromFile: MutableList<DomainWord> = mutableListOf()
//
//            inputStream.bufferedReader().useLines { line ->
//                line.forEach {
//                    wordsFromFile.add(DomainWord(word = it))
//                }
//            }
//            Timber.i("File read")
//            wordsDatabase.wordsDatabaseDao.insertAll(*wordsFromFile.asDatabaseWords())
//            Timber.i("Insert into database successful")
//        } catch (e: Exception) {
//            Timber.e(e)
//        }
//    }
//
//    private fun Int.openFile(): InputStream {
//        val res: Resources = context.resources
//        return res.openRawResource(this)
//    }
//
//
//}