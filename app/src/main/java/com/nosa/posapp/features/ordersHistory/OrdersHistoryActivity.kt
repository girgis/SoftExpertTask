package com.nosa.posapp.features.ordersHistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.utils.Status
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Cart
import com.nosa.posapp.db.CachedCart
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.main.MainViewModel
import com.nosa.posapp.features.orderDetails.OrderDetailsActivity
import com.nosa.posapp.features.ordersHistory.adapter.SellHistoryAdapter
import com.nosa.posapp.features.ordersHistory.viewmodel.SellHistoryViewModel
import com.nosa.posapp.features.scanning.ScanningActivity
import com.nosa.posapp.features.store.StoreActivity
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_orders_history.*
import kotlinx.android.synthetic.main.activity_orders_history.back_iv
import kotlinx.android.synthetic.main.activity_orders_history.loading_ll
import kotlinx.android.synthetic.main.activity_orders_history.mRecycleView

@AndroidEntryPoint
class OrdersHistoryActivity : AppCompatActivity(), SellHistoryAdapter.OnItemClickListener {

    private val viewModel: SellHistoryViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    var cachedUser: CachedUser = CachedUser(this)
    private lateinit var adapter: SellHistoryAdapter
    var historyType: HistoryType = HistoryType.SELL
    var page: Int = 1
    private var total: Int = -1

    companion object{
        const val INTENT_HISTORY_TYPE: String = "intent_history_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_orders_history)

        getData()
        setupUI()
        setupObserver()
        getSellHistory()
    }

    private fun getData(){
        historyType = intent.getSerializableExtra(INTENT_HISTORY_TYPE) as HistoryType
    }

    private fun setupUI(){
        back_iv.setOnClickListener { v -> finish() }

        if (historyType == HistoryType.SELL)
            title_tv.text = getString(R.string.sales_history)
        else
            title_tv.text = getString(R.string.orders_history)

        mRecycleView.layoutManager = GridLayoutManager(this, 1)
        adapter = SellHistoryAdapter(this, this, arrayListOf())
        mRecycleView.adapter = adapter

        mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (page < total) {
                        page += 1
                        getSellHistory()
                    }
                }
            }
        })

        search_icon_iv.setOnClickListener(View.OnClickListener { v ->
            if (isValidData()){
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    cachedUser.getUser()?.api_token?.let { token ->
                        cachedUser.getUser()?.lang?.let { lang ->
                            if (historyType == HistoryType.STOCK) {
                                mainViewModel.searchOrders(
                                    lang,
                                    token,
                                    terminal,
                                    search_et.text.toString().trim(),
                                    "stock"
                                )
                            } else
                                mainViewModel.searchOrders(
                                    lang,
                                    token,
                                    terminal,
                                    search_et.text.toString().trim(),
                                    "sell"
                                )
                        }
                    }
                }
            }
        })
    }

    private fun setupObserver(){
        mainViewModel.cart.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    if (it.data?.success!!) {
                        if (historyType == HistoryType.STOCK) {
                            if (it.data.data.is_order == 1) {
                                startActivity(
                                    Intent(
                                        this@OrdersHistoryActivity,
                                        OrderDetailsActivity::class.java
                                    )
                                        .putExtra(OrderDetailsActivity.INTENT_CART, it.data.data)
                                        .putExtra(
                                            OrderDetailsActivity.INTENT_TYPE, HistoryType.STOCK
                                        )
                                )
                            } else {
                                CachedCart.getInstance().cart = it.data.data
                                startActivity(
                                    Intent(
                                        this@OrdersHistoryActivity,
                                        StoreActivity::class.java
                                    )
                                )
                            }
                        }else{
                            if (it.data.data.is_order == 1) {
                                startActivity(Intent(this@OrdersHistoryActivity, OrderDetailsActivity::class.java)
                                    .putExtra(OrderDetailsActivity.INTENT_CART, it.data.data).putExtra(OrderDetailsActivity.INTENT_TYPE, HistoryType.SELL))
                            } else {
                                startActivity(
                                    Intent(
                                        this@OrdersHistoryActivity,
                                        ScanningActivity::class.java
                                    ).putExtra(OrderDetailsActivity.INTENT_CART, it.data.data)
                                )
                            }
                        }
                    }else{
                        it.data.message?.let { message -> Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
                    }
                }
                Status.ERROR -> {
                    loading_ll.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    loading_ll.visibility = View.VISIBLE
                }
            }
        })


        viewModel.result.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    if (it.data != null && it.data?.success) {
                        it.data?.data?.data?.let { it1 ->
                            if (page > 1)
                                adapter.addToData(it1)
                            else
                                adapter.addData(it1)
                            adapter.notifyDataSetChanged()
                        }
                        it.data.data.total?.let { total = it }
                    }else Toast.makeText(this@OrdersHistoryActivity, it.data?.message.toString(), Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    loading_ll.visibility = View.GONE
                    Log.d("OrdersHistory", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    loading_ll.visibility = View.VISIBLE
                    Log.d("OrdersHistory", "--- LOADING CART")
                }
            }
        })
    }

    private fun isValidData(): Boolean{
        if (search_et.text.isNullOrEmpty()){
            Toast.makeText(this@OrdersHistoryActivity, getString(R.string.id_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getSellHistory(){
        Utils.getDeviceIMEI(this)?.let { terminal ->
            cachedUser.getUser()?.let { user ->
                when (historyType) {
                    HistoryType.SELL -> viewModel.getSellHistory(
                        user.lang,
                        user.api_token,
                        terminal,
                        page.toString()
                    )
                    HistoryType.STOCK -> viewModel.getStokeHistory(
                        user.lang,
                        user.api_token,
                        terminal,
                        page.toString()
                    )
                }
            }
        }
    }

    override fun onItemClick(item: Cart?, position: Int) {
        if (historyType == HistoryType.SELL) {
            if (item?.is_order == 1)
                startActivity(Intent(this@OrdersHistoryActivity, OrderDetailsActivity::class.java)
                    .putExtra(OrderDetailsActivity.INTENT_CART, item).putExtra(OrderDetailsActivity.INTENT_TYPE, historyType))
            else
                startActivity(Intent(this@OrdersHistoryActivity, ScanningActivity::class.java).putExtra(OrderDetailsActivity.INTENT_CART, item))
        }else {
            if (item?.is_order == 1)
                startActivity(Intent(this@OrdersHistoryActivity, OrderDetailsActivity::class.java)
                    .putExtra(OrderDetailsActivity.INTENT_CART, item).putExtra(OrderDetailsActivity.INTENT_TYPE, historyType))
            else {
                CachedCart.getInstance().cart = item
                startActivity(Intent(this@OrdersHistoryActivity, StoreActivity::class.java))
            }
        }
    }

}