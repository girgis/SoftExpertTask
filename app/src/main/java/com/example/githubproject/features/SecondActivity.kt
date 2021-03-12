package com.example.githubproject.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.R
import com.example.githubproject.data.db.AppDatabase
import com.example.githubproject.data.model.FavoriteProducts
import com.example.githubproject.data.model.Product
import com.example.githubproject.features.viewmodel.CartViewModel
import com.example.githubproject.ui.main.viewmodel.MainViewModel
import com.example.githubproject.utils.Status
import com.mindorks.framework.mvvm.ui.main.adapter.MainAdapter
import com.mindorks.framework.mvvm.ui.main.adapter.SecondAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SecondActivity : AppCompatActivity(), SecondAdapter.OnItemClickListener {

    @Inject
    lateinit var db: AppDatabase
    private lateinit var adapter: SecondAdapter
    private val mainViewModel : CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SecondAdapter(this@SecondActivity, this, arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        mainViewModel.products.observe(this, Observer {
            when(it.data != null){
                true ->{
//                    swipeToRefresh.isRefreshing = false
//                    progressBar.visibility = View.GONE
                    it.data?.let { users -> if (users!=null ){renderList(users)} }
                    recyclerView.visibility = View.VISIBLE
                }
                false ->{
                    Log.i("MainActivity", "error")}
            }
        })
        mainViewModel.fetchProducts()
    }

    private fun renderList(users: List<FavoriteProducts>) {
        Log.d("SecondActivity", "--- size: ".plus(users.size))
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(item: FavoriteProducts?, position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            db.favoriteProductsDao().delete(item!!)
            mainViewModel.fetchProducts()
        }
    }
}