package com.debanshu777.stocx.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.model.Stock

class StockAdapter(
    private val stock: List<Stock>,
) :
    RecyclerView.Adapter<StockAdapter.StockAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.stock_item, parent, false)
        return StockAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockAdapterViewHolder, position: Int) {
        //holder.cellYearText.text = nameOfYear[position]
        holder.stockSid.text = stock[position].sid
        holder.stockPrice.text = stock[position].price.toString()
        holder.stockChange.text = stock[position].change.toString()
        if(stock[position].change<0){
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
        stock.size
}