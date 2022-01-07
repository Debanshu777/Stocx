package com.debanshu777.stocx.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.local.StockDatabase
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.network.ConnectionLiveData
import com.debanshu777.stocx.dataSource.network.RetrofitInstance
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.databinding.ActivityMainBinding
import com.debanshu777.stocx.ui.adapter.StockAdapter
import com.debanshu777.stocx.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

open class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainActivityViewModel
    lateinit var stockAdapter:StockAdapter
    private lateinit var connectionLiveData: ConnectionLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionLiveData = ConnectionLiveData(this)
        val stockRepository=StockRepository(StockDatabase(this))
        val viewModelProviderFactory=MainActivityViewModelProviderFactory(application,connectionLiveData,stockRepository)
        viewModel=ViewModelProvider(this,viewModelProviderFactory)[MainActivityViewModel::class.java]
        connectionLiveData.observe(this,{
            viewModel.isNetworkAvailable.value = it
        })
        viewModel.isNetworkAvailable.observe(this,{
            if(it){
                binding.networkUpdate.visibility= View.GONE
            }else{
                binding.networkUpdate.visibility=View.VISIBLE
            }
        })
        viewModel.stockData.observe(this,{
            //binding.dummy.text=it.data.toString()

        })
        viewModel.responseFlow.observe(this,{
            //binding.dummy2.text=it?.toString()
            it?.let {
                setupRecyclerView(it)
            }

        })
    }
    private fun setupRecyclerView(stocks:List<Stock> ) {
        stockAdapter= StockAdapter(stocks)
        binding.stockList.apply {
            adapter=stockAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}