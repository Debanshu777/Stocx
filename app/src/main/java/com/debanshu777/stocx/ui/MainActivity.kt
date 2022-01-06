package com.debanshu777.stocx.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.debanshu777.stocx.R
import com.debanshu777.stocx.dataSource.local.StockDatabase
import com.debanshu777.stocx.dataSource.repository.StockRepository
import com.debanshu777.stocx.databinding.ActivityMainBinding
import com.debanshu777.stocx.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}