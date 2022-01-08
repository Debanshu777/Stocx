package com.debanshu777.stocx.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.local.StockDatabase
import com.debanshu777.stocx.dataSource.model.Stock
import com.debanshu777.stocx.dataSource.network.ConnectionLiveData
import com.debanshu777.stocx.dataSource.polling.StockPoller
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.databinding.ActivityMainBinding
import com.debanshu777.stocx.ui.adapter.StockAdapter
import com.debanshu777.stocx.utils.Constants.Companion.DELAY_TIME_BETWEEN_CALL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var stockAdapter: StockAdapter
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var stockPoller: StockPoller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionLiveData = ConnectionLiveData(this)
        val stockRepository = StockRepository(StockDatabase(this))
        val viewModelProviderFactory = MainActivityViewModelProviderFactory(stockRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[MainActivityViewModel::class.java]

        stockPoller = StockPoller(viewModel, stockRepository, Dispatchers.Main)
        connectionLiveData.observe(this, {
            viewModel.isNetworkAvailable.value = it
        })
        viewModel.isNetworkAvailable.observe(this, {
            if (it) {
                binding.networkUpdate.visibility = View.GONE
            } else {
                binding.networkUpdate.visibility = View.VISIBLE
            }
        })
        viewModel.stockData.observe(this, {
            // binding.dummy.text=it.data.toString()
        })
        viewModel.responseFlow.observe(this, {
            // binding.dummy2.text=it?.toString()
            it?.let {
                setupRecyclerView(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.getDataIcon -> {
                when (viewModel.getDataActiveState.value) {
                    "INACTIVE" -> {
                        item.setIcon(R.drawable.ic_pause_icon)
                        viewModel.getDataActiveState.value = "ACTIVE"
                        val data = stockPoller.poll(DELAY_TIME_BETWEEN_CALL)
                        CoroutineScope(Dispatchers.Main).launch {
                            data.collect {
                                viewModel.setStockData(it)
                            }
                        }
                    }
                    "ACTIVE" -> {
                        item.setIcon(R.drawable.ic_play_icon)
                        viewModel.getDataActiveState.value = "INACTIVE"
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView(stocks: List<Stock>) {
        stockAdapter = StockAdapter(stocks)
        binding.stockList.apply {
            adapter = stockAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
