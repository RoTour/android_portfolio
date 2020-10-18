package com.novyapp.dowittodolist.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.novyapp.dowittodolist.R
import com.novyapp.dowittodolist.database.Todo
import kotlinx.android.synthetic.main.todolist_item.view.*
import org.w3c.dom.Text

@BindingAdapter("todoText")
fun TextView.setTodoTaskText(todo: Todo?){
    todo?.let {
        todo_task_text.text = it.task
    }
}

@BindingAdapter("dueDateText")
fun TextView.setDueDateText(todo: Todo?){
    todo?.let {
        todo_duedate_text.text = longToFormattedDate(todo.dueDate)
    }
}

@BindingAdapter("doneIcon")
fun ImageView.setDoneIcon(todo: Todo?){
    todo?.let {
        todo_icon.setImageResource(when(it.doneState){
            0 -> R.drawable.ic_checkbox_unchecked
            else -> R.drawable.ic_checkbox_checked
        })
    }
}