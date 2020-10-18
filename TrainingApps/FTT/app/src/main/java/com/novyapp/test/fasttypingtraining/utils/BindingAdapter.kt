package com.novyapp.test.fasttypingtraining.utils

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.novyapp.test.fasttypingtraining.R

@BindingAdapter("timeHint")
fun TextView.timeRemainingHint(long: Long?){
    long?.let {
        when(it){
            0L -> this.setText(R.string.gameNotStarted)
        }

    }
}