package com.novyapp.test.fasttypingtraining.data.source.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordsDatabaseDao {
    @Query("select * from words_table ORDER BY wordId DESC")
    fun getAllWords(): List<DatabaseWords>

    @Query("select * from words_table where wordId=:key")
    fun getOneWord(key: Long): DatabaseWords

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg words: DatabaseWords)

    @Query("SELECT * FROM WORDS_TABLE ORDER BY RANDOM() LIMIT :numOfRows")
    fun getRandomRows(numOfRows: Int): List<DatabaseWords>

    @Query("select * from words_table")
    fun observeTasks(): LiveData<List<DatabaseWords>>


}

@Dao
interface HighScoreDatabaseDao {
    @Query("select * from high_score_table")
    fun getAllHighScores(): LiveData<List<DatabaseHighScore>>
}

@Database(
    entities = [DatabaseWords::class, DatabaseHighScore::class],
    version = 1,
    exportSchema = false
)
abstract class WordsDatabase : RoomDatabase() {
    abstract val wordsDatabaseDao: WordsDatabaseDao
    abstract val highScoreDatabaseDao: HighScoreDatabaseDao
}

private lateinit var INSTANCE: WordsDatabase

fun getDatabase(context: Context, inMemory: Boolean = false): WordsDatabase {
    synchronized(WordsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = if (inMemory) {
                Room.inMemoryDatabaseBuilder(
                    context,
                    WordsDatabase::class.java
                ).build()
            } else {
                Room.databaseBuilder(
                    context,
                    WordsDatabase::class.java,
                    "words_database"
                ).build()
            }
        }
    }
    return INSTANCE
}