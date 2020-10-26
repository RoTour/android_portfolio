package com.novyapp.superplanning.data

data class Course(
    var subject: String = "",
    var teacher: String = "",
    var date: String = "",
    var classroom: String = ""
)

data class CourseListViews(val viewType: Int, val course: Course)