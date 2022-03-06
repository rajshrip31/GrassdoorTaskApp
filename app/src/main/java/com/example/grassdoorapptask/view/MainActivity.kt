package com.example.grassdoorapptask.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grassdoorapptask.adapter.MovieListAdapter
import com.example.grassdoorapptask.core.BaseActivity
import com.example.grassdoorapptask.databinding.ActivityMainBinding
import com.example.grassdoorapptask.viewmodel.MainActivityViewModel

class MainActivity : BaseActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var adapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        registerObserver()
        setUI()
    }

    private fun registerObserver() {
        mainActivityViewModel =  ViewModelProviders.of(this)[MainActivityViewModel::class.java]

        mainActivityViewModel.loadingLiveData.observe(this) {
            if (it) {
                dataBinding.llProgressBar.visibility = View.VISIBLE
            } else {
                dataBinding.llProgressBar.visibility = View.GONE
            }
        }

        mainActivityViewModel.errorLiveData.observe(this) {
            if (it != null) {
                printLog("ProMobi errorLiveData :$it")
                showLongMessage(it)
            }
        }

        mainActivityViewModel.moviewListLiveData.observe(this) {
            printLog("Response DATA :$it")
            adapter = MovieListAdapter(this, it)
            dataBinding.recyclerView.adapter = adapter

        }

    }

    private fun setUI() {
        dataBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieListAdapter(this, arrayListOf())
        dataBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                dataBinding.recyclerView.context,
                (dataBinding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )

        mainActivityViewModel.getMovieData("20")

    }
}