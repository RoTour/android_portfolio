package com.novyapp.superplanning.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.data.Course
import com.novyapp.superplanning.databinding.PagerItemBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.WeekRecyclerViewViewHolder>() {

    private var adapterList = linkedMapOf<String, MutableList<Course>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekRecyclerViewViewHolder {
        return WeekRecyclerViewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WeekRecyclerViewViewHolder, position: Int) {
        holder.bind(adapterList.values.toList()[position])
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    fun submitList(data: LinkedHashMap<String, MutableList<Course>>) {
        adapterList.clear()
        adapterList = data
        notifyDataSetChanged()
    }

    class WeekRecyclerViewViewHolder private constructor(
            private val binding: PagerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mutableList: MutableList<Course>) {
            val adapter = CourseListAdapter()
            adapter.submitList(mutableList)
            binding.recyclerView.adapter = adapter
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

