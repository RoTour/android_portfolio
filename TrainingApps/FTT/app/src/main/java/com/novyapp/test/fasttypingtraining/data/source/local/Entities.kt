package com.novyapp.test.fasttypingtraining.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novyapp.test.fasttypingtraining.data.domain.DomainHighScore
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord

@Entity(tableName = "high_score_table")
data class DatabaseHighScore(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0L,
    val wordsPerMinute: Double = 0.0
)

@Entity(tableName = "words_table")
data class DatabaseWords(
    @PrimaryKey(autoGenerate = true)
    val wordId: Long = 0L,
    val word: String = ""
)


fun List<DatabaseHighScore>.asDomainHighScore(): List<DomainHighScore>{
    return map {
        DomainHighScore(
            playerId = it.playerId,
            wordsPerMinute = it.wordsPerMinute
        )
    }
}

fun List<DatabaseHighScore>.asDatabaseHighScore(): List<DatabaseHighScore>{
    return map {
        DatabaseHighScore(
            playerId = it.playerId,
            wordsPerMinute = it.wordsPerMinute
        )
    }
}

fun List<DatabaseWords>.asDomainWords(): List<DomainWord>{
    return map {
        DomainWord(
            wordId = it.wordId,
            word = it.word
        )
    }
}