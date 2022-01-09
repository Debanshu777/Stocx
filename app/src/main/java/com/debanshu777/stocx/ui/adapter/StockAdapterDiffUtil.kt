package com.debanshu777.stocx.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.debanshu777.stocx.dataSource.model.Stock

class StockAdapterDiffUtil(
    private val oldStockList:List<Stock>,
    private val newStockList:List<Stock>

): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldStockList.size

    override fun getNewListSize(): Int = newStockList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldStockList[oldItemPosition].sid==newStockList[newItemPosition].sid

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldStockList[oldItemPosition] == newStockList[newItemPosition]
}