package com.novyapp.superplanning

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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

fun findNextIntBiggerThan(number: String, keys: MutableSet<String>): String {
    keys.forEach { key ->
        var value = key
        if(key.toInt() <= 34) value+=52
        if(value.toInt() >= number.toInt()) return key
    }
    Timber.e("ERROR IN findNextIntBiggerThan : provided number is greater than greatest value in provided set")
    return "-1"
}
//fun CharSequence.fromISOtoNice(): String {
//    return "${this.substring(0,3)}/${this.substring(5,6)}/${this.substring(7,8)}"
//}

fun todayWeekNumber(): Int {
    return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
}