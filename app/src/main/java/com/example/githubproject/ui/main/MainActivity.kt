package com.example.githubproject.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.R
import com.example.githubproject.data.model.User
import com.example.githubproject.ui.main.viewmodel.MainViewModel
import com.example.githubproject.utils.Status
import com.mindorks.framework.mvvm.ui.main.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page += 1
                    mainViewModel.fetchUsers(page)
                }
            }
        })

        swipeToRefresh.setOnRefreshListener {
            page = 1
            mainViewModel.fetchUsers(page)
        }
    }

    private fun setupObserver() {
        mainViewModel.users.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    swipeToRefresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> if (users!=null && users.data != null && !users.data.isEmpty()){renderList(users.data)} }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    swipeToRefresh.isRefreshing = false
                    if (page == 1) {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }else{
                        progressBar.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    swipeToRefresh.isRefreshing = false
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users, page)
        adapter.notifyDataSetChanged()
    }

}