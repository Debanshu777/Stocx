package com.debanshu777.stocx.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.local.StockDatabase
import com.debanshu777.stocx.dataSource.network.ConnectionLiveData
import com.debanshu777.stocx.dataSource.network.RetrofitInstance
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.databinding.ActivityMainBinding
import com.debanshu777.stocx.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainActivityViewModel
    protected lateinit var connectionLiveData: ConnectionLiveData
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
            binding.dummy3.text=it.toString()
        })
        viewModel.stockData.observe(this,{
            binding.dummy.text=it.data.toString()
        })
        viewModel.responseFlow.observe(this,{
            binding.dummy2.text=it?.toString()
        })

    }
}