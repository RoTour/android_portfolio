package com.novyapp.superplanning.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.databinding.PagerItemBinding

class ViewPagerListAdapter :
    ListAdapter<Pair<String, MutableList<CourseV2>>, ViewPagerListAdapter.WeekRecyclerViewViewHolder>(
        DiffUtilCallbacks()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekRecyclerViewViewHolder {
        return WeekRecyclerViewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WeekRecyclerViewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeekRecyclerViewViewHolder private constructor(
        private val binding: PagerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(coursesForOneWeek: Pair<String, MutableList<CourseV2>>) {
            val oneWeekListAdapter = CourseListAdapter()
            oneWeekListAdapter.submitList(coursesForOneWeek)
            binding.recyclerView.adapter = oneWeekListAdapter
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WeekRecyclerViewViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PagerItemBinding.inflate(inflater, parent, false)
                return WeekRecyclerViewViewHolder(binding)
            }
        }
    }


}

class DiffUtilCallbacks : DiffUtil.ItemCallback<Pair<String, MutableList<CourseV2>>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, MutableList<CourseV2>>,
        newItem: Pair<String, MutableList<CourseV2>>
    ): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, MutableList<CourseV2>>,
        newItem: Pair<String, MutableList<CourseV2>>
    ): Boolean {
        oldItem.second.forEachIndexed { index, courseV2 ->
            if (courseV2 != newItem.second[index]) return false
        }
        return true
    }
}