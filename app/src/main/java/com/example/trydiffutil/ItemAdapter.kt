package com.example.trydiffutil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trydiffutil.databinding.ItemBinding

class ItemAdapter(private val onDeletePressed: (item: Item) -> Unit) :
    ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemCallBack()), View.OnClickListener {

    override fun onClick(v: View) {
        if (v.id == R.id.imageDelete) {
            onDeletePressed(v.tag as Item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)
        binding.imageDelete.setOnClickListener(this)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) = with(binding) {
            tvItemName.text = item.title
            imageDelete.tag = item
            imageDelete.visibility = if (item.isDeleting) View.GONE else View.VISIBLE
            progressBar.visibility = if (item.isDeleting) View.VISIBLE else View.GONE
        }
    }

    private class ItemCallBack : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem == newItem
    }

}
