package com.novyapp.dowittodolist.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.dowittodolist.R
import com.novyapp.dowittodolist.database.Todo
import com.novyapp.dowittodolist.databinding.TodolistItemBinding
import kotlinx.android.synthetic.main.todolist_item.view.*

class TodosAdapter (
    private val clickListener: TodoClickListener
) : ListAdapter<Todo, TodosAdapter.TodoViewHolder>(TodoDiffCallbacks()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class TodoViewHolder private constructor(
        val binding: TodolistItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(
            clickListener: TodoClickListener,
            item: Todo?) {
            binding.todo = item
            binding.clickListener = clickListener
            binding.viewHolder = this
            binding.hasPendingBindings()
        }

        fun handleRemoveAnimation(context: Context?, onEnd: () -> Unit) {
            context?.let {
                val view = binding.viewHolder?.itemView
                val animation = AnimationUtils.loadAnimation(it, R.anim.slide_out_left_todo)
                animation.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        onEnd()
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        view?.isClickable = false
                        view?.todo_icon?.setImageResource(R.drawable.ic_checkbox_checked)
                    }
                })
                view?.animation = animation
                view?.startAnimation(animation)
            }
        }

        companion object{
            fun from(parent: ViewGroup): TodoViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TodolistItemBinding.inflate(layoutInflater, parent, false)
                return TodoViewHolder(binding)
            }
        }

    }
}

class TodoDiffCallbacks: DiffUtil.ItemCallback<Todo>(){
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.todoId == newItem.todoId
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }

}

class TodoClickListener(private val clickListener: (id: Long, viewHolder: TodosAdapter.TodoViewHolder) -> Unit){
    fun onClick(id: Long, viewHolder: TodosAdapter.TodoViewHolder) = clickListener(id,viewHolder)
}