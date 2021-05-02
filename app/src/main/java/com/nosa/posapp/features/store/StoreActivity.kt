package com.nosa.posapp.features.store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.utils.Status
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Category
import com.nosa.posapp.data.model.Product
import com.nosa.posapp.db.CachedCart
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.cart.CartActivity
import com.nosa.posapp.features.scanning.ScanningViewModel
import com.nosa.posapp.features.store.adapters.CategoryAdapter
import com.nosa.posapp.features.store.products.StoreProductsActivity
import com.nosa.posapp.features.store.ui.ProductClickedViewModel
import com.nosa.posapp.features.store.viewmodel.StoreViewModel
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.activity_store.back_iv
import kotlinx.android.synthetic.main.activity_store.loading_ll


@AndroidEntryPoint
class StoreActivity : AppCompatActivity(), CategoryAdapter.OnItemClickListener {

    private val storeViewModel: StoreViewModel by viewModels()
    private val scanningViewModel: ScanningViewModel by viewModels()
    private val viewModel: ProductClickedViewModel by viewModels()

    var cachedUser: CachedUser = CachedUser(this)
    private lateinit var adapter: CategoryAdapter
    private var page: Int = 1
    private var total: Int = -1
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_store)

        setupUI()
        setupObserver()
        getCategories()
    }

    override fun onResume() {
        super.onResume()
        setCartQuantity()
    }

    override fun onDestroy() {
        CachedCart.getInstance().clearCart()
        super.onDestroy()
    }

    private fun setCartQuantity(){
        CachedCart.getInstance().cart?.let{cart ->
            if (cart?.products?.size!! > 9){
                badge_count_tv.text = "+9"
            }else badge_count_tv.text = cart?.products?.size.toString()
        } ?: run {badge_count_tv.text = "0"}
    }

    private fun getCategories(){
        Utils.getDeviceIMEI(this)?.let { terminal ->
            cachedUser.getUser()?.let { token ->
                storeViewModel.getCategories(
                    token.lang,
                    token.api_token,
                    terminal,
                    page.toString()
                )
            }
        }
    }

    private fun setupUI(){
        mRecycleView.layoutManager = GridLayoutManager(this, 3)
        adapter = CategoryAdapter(this@StoreActivity, this, arrayListOf())
        mRecycleView.adapter = adapter

        mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (page < total) {
                        page += 1
                        getCategories()
                    }
                }
            }
        })

        back_iv.setOnClickListener(View.OnClickListener { v -> finish() })
        store_scan_btn_ll.setOnClickListener(View.OnClickListener { startActivity(Intent(this@StoreActivity, ScanProductActivity::class.java)) })
        store_scan_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(this@StoreActivity, ScanProductActivity::class.java)) })
        cart_group.setOnClickListener { v -> startActivity(Intent(this@StoreActivity, CartActivity::class.java)) }
        cart_img.setOnClickListener { v -> startActivity(Intent(this@StoreActivity, CartActivity::class.java)) }
        search_by_barcode_s_et.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                makeRequest()
                true
            }
            false
        }

        search_img.setOnClickListener(View.OnClickListener { v ->
            makeRequest()
        })
    }

    private fun makeRequest(){
        if (isValidData()) {
            cachedUser.getUser()?.api_token?.let {
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    cachedUser?.getUser()?.lang?.let { it1 ->
                        scanningViewModel.searchProductsByBarcode(
                            it1, it, terminal, search_by_barcode_s_et.text.toString().trim())
                    }
                }
            }
        }
    }

    private fun setupObserver(){
        viewModel.cart.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    if (it.data?.data != null) {
                        CachedCart.getInstance().cart = it.data.data
                        setCartQuantity()
                    }
                }
                Status.ERROR -> {
                    loading_ll.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    loading_ll.visibility = View.VISIBLE
                    Log.d("ScanningActivity", "--- LOADING CART")
                }
            }
        })

        scanningViewModel.product.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                    it.data?.let {productModel ->
                        if (productModel.success){
                            it.data.data?.let {if (!it.isNullOrEmpty()){
                                this.product = it.get(0)
                                addProductToCart() }}
                        }else Toast.makeText(this, productModel.message, Toast.LENGTH_LONG).show()
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
        storeViewModel.result.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    Log.d("StoreActivity", "--- size ${it.data?.data?.data?.size}")
                    it.data?.data?.last_page?.let { total = it }
                    it.data?.data?.data?.let { it1 ->
                        if (page > 1) adapter.addToData(it1)
                        else adapter.addData(it1)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    loading_ll.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("StoreActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    loading_ll.visibility = View.VISIBLE
                    Log.d("StoreActivity", "--- LOADING CART")
                }
            }
        })
    }

    private fun addProductToCart(){
        if (product != null) {
            cachedUser?.getUser()?.let { user ->
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    CachedCart.getInstance().cart?.let {
                        it.session_id?.let { it1 ->
                            viewModel.addToCart(user.lang, user.api_token, terminal, product?.id.toString(), it1)
                        }
                    } ?: viewModel.createCart(
                        user.lang,
                        user.api_token,
                        terminal,
                        product?.id.toString()
                    )
                }
            }
        }
    }

    private fun isValidData(): Boolean{
        if (search_by_barcode_s_et.text.isNullOrEmpty()){
            Toast.makeText(this@StoreActivity, getString(R.string.id_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onItemClick(item: Category?, position: Int) {
        startActivity(Intent(this@StoreActivity, StoreProductsActivity::class.java).putExtra("item", item))
    }

}