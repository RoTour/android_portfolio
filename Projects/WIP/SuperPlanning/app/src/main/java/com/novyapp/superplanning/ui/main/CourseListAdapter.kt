package com.novyapp.superplanning.ui.main

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.data.Course
import com.novyapp.superplanning.data.CourseListViews
import com.novyapp.superplanning.databinding.CourseListItemBinding
import com.novyapp.superplanning.databinding.CourseListSeparatorBinding
import com.novyapp.superplanning.fromISOtoNice
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.ArrayList

class CourseListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val COURSE_VIEW = 1
        const val SEPARATOR_VIEW = 2
    }

    private val list: ArrayList<CourseListViews> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            COURSE_VIEW -> CourseListItemViewHolder.from(parent)
            else -> CourseListSeparatorViewHolder.from(parent)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == COURSE_VIEW){
            (holder as CourseListItemViewHolder).bind(list[position].course)
        } else {
            (holder as CourseListSeparatorViewHolder).bind(list[position].course)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    fun submitList(newList:  ArrayList<CourseListViews>){
        list.clear()
        val orderedCourses: LinkedHashMap<String, MutableList<Course>> = LinkedHashMap()

        newList.forEach {
            val key = it.course.date.substring(0,10)
            if(!orderedCourses.containsKey(key)) orderedCourses[key] = mutableListOf()
            orderedCourses[key]!!.add(it.course)
        }
        orderedCourses.forEach { (key, sublist) ->
            list.add(CourseListViews(SEPARATOR_VIEW, Course(date = key)))
            sublist.forEach { course -> list.add(CourseListViews(COURSE_VIEW, course)) }
        }

        notifyDataSetChanged()
    }

    class CourseListItemViewHolder private constructor(
            private val binding: CourseListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Course?) {
            item?.let {
                binding.course = it
                binding.formattedDate = it.date.fromISOtoNice()
                binding.executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): CourseListItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CourseListItemBinding.inflate(inflater, parent, false)
                return CourseListItemViewHolder(binding)
            }
        }
    }

    class CourseListSeparatorViewHolder private constructor(
            private val binding: CourseListSeparatorBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private val simpleDateFormat = SimpleDateFormat.getDateInstance()
        fun bind(item: Course?){
            item?.let {
//                binding.separatorDate = simpleDateFormat.format(item.date)
                binding.separatorDate = it.date.fromISOtoNice()
            }
        }

        companion object {
            fun from(parent: ViewGroup): CourseListSeparatorViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CourseListSeparatorBinding.inflate(inflater, parent, false)
                return CourseListSeparatorViewHolder(binding)
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