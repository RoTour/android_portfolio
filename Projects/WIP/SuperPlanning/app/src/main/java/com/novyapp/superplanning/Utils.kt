package com.novyapp.superplanning

import com.novyapp.superplanning.data.CourseV2
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


fun todayWeekNumber(): Int {
    return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
}

fun LinkedHashMap<String, MutableList<CourseV2>>.toSortedByWeeksMap(): LinkedHashMap<String, MutableList<CourseV2>> {
    val presorted = this.toSortedMap()
    Timber.i("newWeek: presortedList : $presorted")
    val sortedWeeks = mutableListOf<String>()
    var idx = 0
    presorted.keys.forEach {
        if (it.toInt() > 35 ){ // Week 35 should be start of scholar year
            sortedWeeks.add(idx++, it)
        } else {
            Timber.i("newWeek: index : ${sortedWeeks.size}")
            sortedWeeks.add(sortedWeeks.size, it)
        }
    }
    Timber.i("newWeek: sortedList : $sortedWeeks")
    val sortedMap = LinkedHashMap<String, MutableList<CourseV2>>()
    sortedWeeks.forEach {
        sortedMap[it] = this[it]!!
    }
    Timber.i("newWeek: sortedmap : $sortedMap")
    return sortedMap
}
