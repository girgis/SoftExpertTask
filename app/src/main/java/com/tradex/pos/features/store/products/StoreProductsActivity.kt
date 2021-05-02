package com.tradex.pos.features.store.products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.utils.Status
import com.tradex.pos.R
import com.tradex.pos.data.model.Category
import com.tradex.pos.data.model.Product
import com.tradex.pos.db.CachedCart
import com.tradex.pos.db.CachedUser
import com.tradex.pos.features.scanning.ui.PaymentMethodsFragment
import com.tradex.pos.features.store.adapters.CategoryProductAdapter
import com.tradex.pos.features.store.products.viewmodel.StoreProductsViewModel
import com.tradex.pos.features.store.ui.ProductClickedFragment
import com.tradex.pos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_store.back_iv
import kotlinx.android.synthetic.main.activity_store.mRecycleView
import kotlinx.android.synthetic.main.activity_strore_products.*

@AndroidEntryPoint
class StoreProductsActivity : AppCompatActivity(), CategoryProductAdapter.OnItemClickListener {

    private var category: Category? = null
    private val storeProductsViewModel: StoreProductsViewModel by viewModels()
    var cachedUser: CachedUser = CachedUser(this)
    private var page: Int = 1

    private lateinit var adapter: CategoryProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_strore_products)

        getData()
        setupUI()
        setupObserver()
        getProducts()
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(android.R.id.content)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            if (it) {
                super.onBackPressed()
            }else{
            }
            return
        }
        super.onBackPressed()
    }

    private fun getData(){
        category = intent.getParcelableExtra<Category>("item")
    }

    private fun getProducts(){
        Utils.getDeviceIMEI(this)?.let { terminal ->
            cachedUser.getUser()?.let { token ->
                category?.id?.let {
                    storeProductsViewModel.getCategories(
                        token.lang,
                        token.api_token,
                        terminal,
                        it,
                        page
                    )
                }
            }
        }
    }

    private fun setupUI(){
        mRecycleView.layoutManager = GridLayoutManager(this, 2)
        adapter = CategoryProductAdapter(this@StoreProductsActivity, this, arrayListOf())
        mRecycleView.adapter = adapter

        mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page += 1
                    getProducts()
                }
            }
        })

        category?.let { title_tv.text = category?.name }
        CachedCart.getInstance().cart?.let { cart ->
            cart.products?.let { list ->  updateBadge(list.size)}
        }

        back_iv.setOnClickListener(View.OnClickListener { v -> finish() })
    }

    private fun setupObserver(){
        storeProductsViewModel.result.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("StoreActivity", "--- size ${it.data?.data?.data?.size}")
                    it.data?.data?.data?.let { it1 ->
                        if (page > 1){
                            adapter.addToData(it1)
                        }else adapter.addData(it1)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("StoreActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    Log.d("StoreActivity", "--- LOADING CART")
                }
            }
        })
    }

    override fun onItemClick(item: Product?, position: Int) {
        item?.let { addFragmentToActivity(ProductClickedFragment.newInstance(it)) }
    }

    private fun addFragmentToActivity(fragment: Fragment?){
        if (fragment == null) return
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
        tr.add(android.R.id.content, fragment, ProductClickedFragment.TAG)
        tr.addToBackStack(PaymentMethodsFragment.TAG)
        tr.commitAllowingStateLoss()
    }

    fun updateBadge(number: Int){
        if (number > 9){
            padge_count_tv.text = "+9"
        }else padge_count_tv.text = number.toString()
    }

}