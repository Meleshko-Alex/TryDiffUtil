package com.example.trydiffutil

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trydiffutil.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MyViewModel>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        viewModel.stateFlow
            .flowWithLifecycle(lifecycle)
            .onEach(::renderState)
            .launchIn(lifecycleScope)
    }

    private fun initViews() {
        adapter = ItemAdapter { viewModel.delete(it) }
        with(binding) {
            (rvItemList.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
            rvItemList.adapter = adapter
            rvItemList.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun renderState(state: MyViewModel.State) {
        adapter.submitList(state.items)
        binding.tvTotalCounter.text = "Total Count: ${state.totalCount}"
    }
}