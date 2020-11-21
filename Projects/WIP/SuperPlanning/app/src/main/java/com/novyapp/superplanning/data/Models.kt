package com.novyapp.superplanning.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class Course(
    var subject: String = "",
    var teacher: String = "",
    var date: String = "",
    var classroom: String = ""
) {

}

data class CourseListViews(val viewType: Int, val course: Course)


data class CourseV2 (
    val date: Timestamp? = null,
    val professor: String? = null,
    val subject: String? = null,
    val classroom: String? = null
) {

}

fun DocumentSnapshot.toCourseV2(): CourseV2{
    return CourseV2(
        this.data?.get("datetime") as Timestamp,
        this.data?.get("professor") as String,
        this.data?.get("subject") as String,
        this.data?.get("classroom") as String
    )
}