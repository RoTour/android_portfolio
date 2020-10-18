package com.novyapp.test.utlimatetodolist.ui.displaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.databinding.TaskListItemBinding
import timber.log.Timber

class TaskListAdapter(
    private val clickListener: TaskListListener,
    private val viewModel: ListDisplayViewModel
) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskListCallbacks()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position), viewModel)
    }

    class TaskViewHolder private constructor(
        private val binding: TaskListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: TaskListListener,
            item: Task?,
            viewModel: ListDisplayViewModel
        ) {
            binding.task = item
            binding.listener = clickListener
            binding.viewHolder = this
            binding.viewModel = viewModel
//            binding.hasPendingBindings()
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskListItemBinding.inflate(layoutInflater, parent ,false)
                return TaskViewHolder(binding)
            }
        }
    }

}

class TaskListCallbacks : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        Timber.i("update itemsSame:(${oldItem.taskTitle}, ${newItem.taskTitle}) result: ${oldItem.id == newItem.id} \nd")
        return oldItem.id == newItem.id
//        return false
    }


    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        Timber.i("update contentsSame:(${oldItem.taskTitle}, ${newItem.taskTitle}) result: ${oldItem == newItem}")
        Timber.i("update isCompleted: (${oldItem.isCompleted}, ${newItem.isCompleted})\nd")
        return oldItem == newItem
//        return false
    }
}

class TaskListListener(private val clickListener: ((task: Task, viewHolder: TaskListAdapter.TaskViewHolder) -> Unit)) {
    fun onClick(task: Task, viewHolder: TaskListAdapter.TaskViewHolder) =
        clickListener(task, viewHolder)
}