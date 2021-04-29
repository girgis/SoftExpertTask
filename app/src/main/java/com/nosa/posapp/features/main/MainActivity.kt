package com.nosa.posapp.features.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.githubproject.utils.Status
import com.nosa.posapp.R
import com.nosa.posapp.db.CachedCart
import com.nosa.posapp.db.CachedSysConstants
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.login.LoginActivity
import com.nosa.posapp.features.orderDetails.OrderDetailsActivity
import com.nosa.posapp.features.ordersHistory.HistoryType
import com.nosa.posapp.features.ordersHistory.OrdersHistoryActivity
import com.nosa.posapp.features.profile.ProfileActivity
import com.nosa.posapp.features.scanning.ScanningActivity
import com.nosa.posapp.features.store.StoreActivity
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    val cachedUser: CachedUser = CachedUser(this)
    val cachedSysConstants: CachedSysConstants = CachedSysConstants(this)
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this@MainActivity, it?.lang) }
        setContentView(R.layout.activity_main)

        setupUI()
        setupObserver()
        getSystemConstrains()
    }

    private fun setupUI(){
        market_name_tv.text = cachedUser.getUser()?.branch?.name

        Sale_cv.setOnClickListener(this)
        stock_cv.setOnClickListener(this)
        Sale_history_cv.setOnClickListener(this)
        stock_history_cv.setOnClickListener(this)
        shop.setOnClickListener(this)
        logout_iv.setOnClickListener(this)
        market_name_tv.setOnClickListener(this)
        cachedUser.getUser()?.branch?.logofullpath?.let { logo ->
            Glide.with(this@MainActivity).load(logo).centerCrop().into(shop)
        }

        cachedUser.getUser()?.let {user ->
            inquiry_btn.setOnClickListener { v ->
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    if (isValidData())
                        mainViewModel.searchOrders(
                            user.lang,
                            user.api_token,
                            terminal,
                            user_name_et.text.toString().trim(),
                            "all"
                        )
                }
            }
        }
    }

    private fun setupObserver(){
        mainViewModel.cart.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    if (it.data?.success!!) {
                        if (it.data?.data != null) {
                            if (it.data.data.is_order == 1){
                                if(it.data.data.for_stock == 0) {
                                    startActivity(Intent(this@MainActivity, OrderDetailsActivity::class.java)
                                        .putExtra(OrderDetailsActivity.INTENT_CART, it.data.data).putExtra(OrderDetailsActivity.INTENT_TYPE, HistoryType.SELL))
                                }else{
                                    startActivity(Intent(this@MainActivity, OrderDetailsActivity::class.java)
                                        .putExtra(OrderDetailsActivity.INTENT_CART, it.data.data).putExtra(OrderDetailsActivity.INTENT_TYPE, HistoryType.STOCK))
                                }
                            } else {
                                if(it.data.data.for_stock == 0) {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            ScanningActivity::class.java
                                        ).putExtra(OrderDetailsActivity.INTENT_CART, it.data.data)
                                    )
                                }else{
                                    CachedCart.getInstance().cart = it.data.data
                                    startActivity(Intent(this@MainActivity, StoreActivity::class.java))
                                }
                            }
                        }
                    }else{
                        it.data?.message?.let { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
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

        mainViewModel.system.observe(this, Observer { response ->
            when(response.status){
                Status.SUCCESS -> {response.data?.data?.let {
                    cachedSysConstants.saveSystemConstants(it)
                }}
                Status.ERROR -> {Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()}
                Status.LOADING -> {}
            }
        })
    }

    private fun getSystemConstrains(){
        cachedUser.getUser()?.let { user ->
            Utils.getDeviceIMEI(this)?.let { terminal ->
                mainViewModel.getSystemConstrains(user.lang, user.api_token, terminal)
            }
        }
    }

    private fun isValidData(): Boolean{
        if (user_name_et.text.isNullOrEmpty()){
            Toast.makeText(this@MainActivity, getString(R.string.id_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.Sale_cv ->{
                startActivity(Intent(this@MainActivity, ScanningActivity::class.java))
            }
            R.id.stock_cv -> {
                startActivity(Intent(this@MainActivity, StoreActivity::class.java))
            }
            R.id.Sale_history_cv -> {
                startActivity(Intent(this@MainActivity, OrdersHistoryActivity::class.java)
                    .putExtra(OrdersHistoryActivity.INTENT_HISTORY_TYPE, HistoryType.SELL))
            }
            R.id.stock_history_cv -> {
                startActivity(Intent(this@MainActivity, OrdersHistoryActivity::class.java)
                    .putExtra(OrdersHistoryActivity.INTENT_HISTORY_TYPE, HistoryType.STOCK))
            }
            R.id.shop -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            }
            R.id.market_name_tv -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            }
            R.id.logout_iv -> {
                cachedUser.clearUser()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                finish()
            }
        }
    }

}