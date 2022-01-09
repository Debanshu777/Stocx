package com.debanshu777.stocx.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.model.Stock

class StockAdapter :
    RecyclerView.Adapter<StockAdapter.StockAdapterViewHolder>() {
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
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.stock_item, parent, false)
        return StockAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockAdapterViewHolder, position: Int) {
        holder.stockSid.text = differ.currentList[position].sid
        holder.stockPrice.text = differ.currentList[position].price.toString()
        holder.stockChange.text = differ.currentList[position].change.toString()
        if (differ.currentList[position].change < 0) {
            holder.changeIcon.setImageResource(R.drawable.ic_loss_icon)
        }
    }

    inner class StockAdapterViewHolder(
        itemView: View,
    ) :
        RecyclerView.ViewHolder(itemView) {
        val stockSid: TextView = itemView.findViewById(R.id.stock_sid)
        val stockPrice: TextView = itemView.findViewById(R.id.stock_price)
        val stockChange: TextView = itemView.findViewById(R.id.stock_change)
        val changeIcon: ImageView = itemView.findViewById(R.id.change_icon)
    }

    override fun getItemCount(): Int =
        differ.currentList.size
}
