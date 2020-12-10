package com.novyapp.superplanning

import com.novyapp.superplanning.data.CourseV2
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

fun String.fromISOtoNice(): String {
    return "${this.substring(0, 4)}/${this.substring(5, 7)}/${this.substring(8, 10)}"
}

fun Calendar.toISOString(): String {
    val inputString = "${this.get(Calendar.YEAR)}/${this.get(Calendar.MONTH)+1}/${this.get(Calendar.DAY_OF_MONTH)} ${this.get(Calendar.HOUR_OF_DAY)}:${this.get(Calendar.MINUTE)}"
    val date: Date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(inputString) ?: Date()

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    return sdf.format(date)
}

fun String.fromISOtoCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.set(
            this.substring(0,4).toInt(), // Year
            this.substring(5,7).toInt() -1, // Month
            this.substring(8,10).toInt(), // Day
            this.substring(11,13).toInt(), // Hour
            this.substring(14,16).toInt() // Min
    )
    return cal
}

fun Int.nextWeekNumber(): Int{
    if(this <= 0 || this > 52) throw IllegalArgumentException("WeekNumber should be in 0 < WeekNumber <= 52")
    return if(this!=52)  this+1 else 1
}

fun Int.previousWeekNumber(): Int{
    if(this <= 0 || this > 52) throw IllegalArgumentException("WeekNumber should be in 0 < WeekNumber <= 52")
    return if(this!=1)  this-1 else 52
}

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


//fun numberOfWeeksInYear(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Int {
//    val cal = Calendar.getInstance()
//    cal.set(year, 11,31)
//    Timber.i("newWeek: ${DateFormat.getDateInstance(DateFormat.LONG).format(Date(cal.timeInMillis))}")
//    Timber.i("newWeek: ${cal.get(Calendar.WEEK_OF_YEAR)}")
//    return 52
//}
