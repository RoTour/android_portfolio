package com.novyapp.test.utlimatetodolist.ui

import android.graphics.Paint
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.test.utlimatetodolist.data.Result
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.ui.displaylist.TaskListAdapter
import timber.log.Timber

@BindingAdapter("checkedState")
fun CheckBox.setChecked(task: Task?) {
    task?.let {
        this.isChecked = it.isCompleted
        Timber.i("Modified task: title:${it.taskTitle} state: ${it.isCompleted}")
    }
}

@BindingAdapter("taskText")
fun TextView.setTaskText(task: Task?) {
    task?.let {
        this.text = it.taskTitle
    }
}

@BindingAdapter("setCheckedText")
fun setCheckedText(textView: TextView, checked: Boolean?) {
    checked?.let {
        if (checked) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

@BindingAdapter("listItem")
fun setListItem(recyclerView: RecyclerView, items: LiveData<List<Task>>?) {
    Timber.i("update: Setting List (BindingAdapter)")
    items?.let {
        (recyclerView.adapter as TaskListAdapter).submitList(items.value)
    }
}