package com.example.githubproject.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.githubproject.R
import com.example.githubproject.data.db.AppDatabase
import com.example.githubproject.data.model.FavoriteProducts
import com.example.githubproject.data.model.Product
import com.example.githubproject.data.model.User
import com.example.githubproject.features.SecondActivity
import com.example.githubproject.ui.main.viewmodel.MainViewModel
import com.example.githubproject.utils.Status
import com.mindorks.framework.mvvm.ui.main.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainAdapter.OnItemClickListener {
    @Inject lateinit var db: AppDatabase
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupObserver()
    }

    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings ->{
                goToCartScreen()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(this@MainActivity, this, arrayListOf())
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
//                    page += 1
//                    mainViewModel.fetchUsers(page)
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
//                    it.data?.let { users -> if (users!=null && users.data != null && !users.data.isEmpty()){renderList(users.data)} }
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

        mainViewModel.products.observe(this, Observer {
            when(!it.data.isNullOrEmpty()){
                true ->{
                    swipeToRefresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> if (users!=null ){renderList(users)} }
                    recyclerView.visibility = View.VISIBLE
                }
                false ->{Log.i("MainActivity", "error")}
            }
        })
    }

    private fun renderList(users: List<Product>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(item: Product?, position: Int) {
//        Toast.makeText(this@MainActivity, item?.ProductName, Toast.LENGTH_LONG).show()
        GlobalScope.launch(Dispatchers.IO) {
            if (db.favoriteProductsDao().getAll().contains(FavoriteProducts(item?.uid!!, Date(), item?.ProductName, item?.ProductImage))){
                db.favoriteProductsDao().delete(FavoriteProducts(item?.uid!!, Date(), item?.ProductName, item?.ProductImage))
            }else {
                db.favoriteProductsDao().insertAll(
                    FavoriteProducts(item?.uid!!, Date(), item?.ProductName, item?.ProductImage)
                )
            }
            runOnUiThread(Runnable { adapter.notifyItemChanged(position) })
        }
    }

    private fun goToCartScreen(){
        val intent = Intent(this@MainActivity, SecondActivity::class.java)
        startActivity(intent)
    }

}