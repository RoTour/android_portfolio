package com.novyapp.superplanning.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.data.CourseListViews
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.databinding.CourseListItemBinding
import com.novyapp.superplanning.databinding.CourseListSeparatorBinding
import java.text.DateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

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
            (holder as CourseListItemViewHolder).bind(list[position].value as CourseV2)
        } else {
            (holder as CourseListSeparatorViewHolder).bind(list[position].value as String)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }


    fun submitList(newList:  MutableList<CourseV2>){
        list.clear()
        /**
         * Courses ordered by DAY : "Day" to List<CourseV2>
         */
        val orderedCourses: LinkedHashMap<String, MutableList<CourseV2>> = LinkedHashMap()

        newList.forEach {
            val key = DateFormat.getDateInstance(DateFormat.LONG).format(it.date?.toDate() ?: Date())
            if(!orderedCourses.containsKey(key)) orderedCourses[key] = mutableListOf()
            orderedCourses[key]!!.add(it)
        }
        orderedCourses.forEach { (key, sublist) ->
            list.add(CourseListViews(SEPARATOR_VIEW, key))
            sublist.forEach { course -> list.add(CourseListViews(COURSE_VIEW, course)) }
        }
        notifyDataSetChanged()
    }




    class CourseListItemViewHolder private constructor(
            private val binding: CourseListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CourseV2?) {
            item?.let {
                binding.course = it
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
        fun bind(item: String?){
            item?.let {
                binding.separatorDate = it
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
