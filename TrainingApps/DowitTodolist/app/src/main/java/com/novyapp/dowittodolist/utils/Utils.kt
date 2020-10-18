package com.novyapp.dowittodolist.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

fun generateRandomDate(): Long{
    val day = Random.nextInt(1,31)
    val month = Random.nextInt(1,13)
    val year = Random.nextInt(2020,2022)
    val strDate = "$year/$month/$day"
    return convertDateToLong(strDate)
//    return System.currentTimeMillis() + (Random.nextInt(1,730)*8640000)
}

@SuppressLint("SimpleDateFormat")
fun longToFormattedDate(time: Long): String {
    val date = Date(time)
    val format: DateFormat =
        if (Build.VERSION.SDK_INT < 24) {
            SimpleDateFormat("dd MMM YYYY")
        } else {
            getDateInstance()
        }

    return format.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("yyyy/MM/dd")
    return df.parse(date)!!.time
}

fun getLongDateFromDMY(day: Int, month: Int, year: Int): Long{
    val strDate = "$year/$month/$day"
    return convertDateToLong(strDate)
}

fun mylogs(log: String){
    Log.i("MyLogs", log)
}