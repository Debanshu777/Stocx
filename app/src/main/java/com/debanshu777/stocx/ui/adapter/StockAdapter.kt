package com.debanshu777.stocx.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.databinding.StockItemBinding

class StockAdapter :
    RecyclerView.Adapter<StockAdapter.StockAdapterViewHolder>() {


    inner class StockAdapterViewHolder(val binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root)
     private val differCallback = object : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem.sid == newItem.sid
        }

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapterViewHolder {
        val binding = StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockAdapterViewHolder, position: Int) {
        with(holder) {
            with(differ.currentList[position]) {
                binding.stockSid.text=this.sid
                binding.stockPrice.text=this.price.toString()
                binding.stockChange.text=this.change.toString()
                if(this.change<0){
                    binding.changeIcon.setImageResource(R.drawable.ic_loss_icon)
                }
            }
        }
    }

    override fun getItemCount(): Int =
        differ.currentList.size
}
