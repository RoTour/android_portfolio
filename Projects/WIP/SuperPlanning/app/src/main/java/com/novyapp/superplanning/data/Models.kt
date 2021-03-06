package com.novyapp.superplanning.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

data class Course(
    var subject: String = "",
    var teacher: String = "",
    var date: String = "",
    var classroom: String = ""
) {

}

data class CourseListViews(val viewType: Int, val value: Any)


data class CourseV2 (
    val subject: String? = null,
    val professor: String? = null,
    val classroom: String? = null,
    val date: Timestamp? = null
) {

}

fun DocumentSnapshot.toCourseV2(): CourseV2{
    return CourseV2(
        this.data?.get("subject") as String,
        this.data?.get("professor") as String,
        this.data?.get("classroom") as String,
        this.data?.get("date") as Timestamp
    )
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Loading(val message: String = "Loading"): Result<Nothing>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Loading -> "Loading[$message]"
            is Error -> "Error[e=$exception]"
        }
    }
}

data class Reference<out T>(val pointedValue: T)