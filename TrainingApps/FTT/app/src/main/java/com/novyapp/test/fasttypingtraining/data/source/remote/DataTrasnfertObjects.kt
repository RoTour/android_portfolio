package com.novyapp.test.fasttypingtraining.data.source.remote

import android.os.Parcelable
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.source.local.DatabaseWords
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetworkHighScore(
    val playerId: Long = 0L,
    val wordsPerMinute: Double = 0.0
) : Parcelable

@Parcelize
data class NetworkWord(
    val word: String = ""
) : Parcelable

@Parcelize
data class NetworkListVersion(
    val version: Long = 0L
) : Parcelable

fun List<NetworkWord>.asDatabaseWords(): Array<DatabaseWords>{
    return map {
        DatabaseWords(
            word = it.word
        )
    }.toTypedArray()
}

fun List<NetworkWord>.asDomainWords(): List<DomainWord>{
    return map {
        DomainWord(
            word = it.word
        )
    }
}

fun Array<out NetworkWord>.asDomainWords(): Array<DomainWord>{
    return map {
        DomainWord(
            word = it.word
        )
    }.toTypedArray()
}