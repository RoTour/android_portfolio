package com.novyapp.superplanning.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.data.Course
import com.novyapp.superplanning.databinding.CourseListItemBinding
import com.novyapp.superplanning.fromISOtoNice
import timber.log.Timber

class CourseListAdapter : ListAdapter<Course, CourseListAdapter.CourseListViewHolder>(DiffCallbacks()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        return CourseListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CourseListViewHolder private constructor(
            private val binding: CourseListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Course?) {
            item?.let {
                binding.course = it
                binding.formattedDate = it.date.fromISOtoNice()
                Timber.i(binding.formattedDate)
                binding.executePendingBindings()
            }
        }


        companion object {
            fun from(parent: ViewGroup): CourseListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CourseListItemBinding.inflate(inflater, parent, false)
                return CourseListViewHolder(binding)
            }
        }
    }

}

class DiffCallbacks : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }

}