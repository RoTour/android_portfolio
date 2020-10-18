package com.novyapp.dofusitems.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.novyapp.dofusitems.domain.DomainDofusMounts
import com.novyapp.dofusitems.ui.main.MainListAdapter

@BindingAdapter("listData")
fun bindListData(recyclerView: RecyclerView, listData: List<DomainDofusMounts>?){
    val adapter: MainListAdapter = recyclerView.adapter as MainListAdapter
    adapter.submitList(listData)

}


@BindingAdapter("mountImage")
fun bindMountImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .into(imgView)
    }
}