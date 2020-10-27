package com.novyapp.superplanning

import java.text.SimpleDateFormat
import java.util.*

fun String.fromISOtoNice(): String {
    return "${this.substring(0, 4)}/${this.substring(5, 7)}/${this.substring(8, 10)}"
}

fun Calendar.toISOString(): String {
    val inputString = "${this.get(Calendar.YEAR)}/${this.get(Calendar.MONTH)+1}/${this.get(Calendar.DAY_OF_MONTH)} ${this.get(Calendar.HOUR_OF_DAY)}:${this.get(Calendar.MINUTE)}"
    val date: Date = SimpleDateFormat("yyyy/MM/dd HH:mm").parse(inputString) ?: Date()

    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
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
//fun CharSequence.fromISOtoNice(): String {
//    return "${this.substring(0,3)}/${this.substring(5,6)}/${this.substring(7,8)}"
//}