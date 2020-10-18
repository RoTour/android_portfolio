package com.novyapp.dofusitems.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.dofusitems.databinding.ItemGridListBinding
import com.novyapp.dofusitems.domain.DomainDofusMounts

class MainListAdapter : ListAdapter<DomainDofusMounts, MainListAdapter.ViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGridListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemGridListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainDofusMounts?) {
            binding.mount = item
            binding.executePendingBindings()
        }

    }

    companion object DiffCallBack : DiffUtil.ItemCallback<DomainDofusMounts>(){
        override fun areItemsTheSame(
            oldItem: DomainDofusMounts,
            newItem: DomainDofusMounts
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DomainDofusMounts,
            newItem: DomainDofusMounts
        ): Boolean {
            return oldItem == newItem
        }
    }

}

//class MainClickListener(val clickListener: () -> Unit) {
//
//}
