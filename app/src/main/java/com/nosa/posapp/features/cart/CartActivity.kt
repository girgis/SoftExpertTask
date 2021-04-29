package com.nosa.posapp.features.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.utils.Status
import com.nosa.posapp.R
import com.nosa.posapp.data.model.CartModel
import com.nosa.posapp.data.model.Products
import com.nosa.posapp.db.CachedCart
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.cart.success.SuccessCartActivity
import com.nosa.posapp.features.scanning.ScanningViewModel
import com.nosa.posapp.features.scanning.adapters.ProductAdapter
import com.nosa.posapp.features.success.SuccessfulActivity
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cart.*

@AndroidEntryPoint
class CartActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private val scanningViewModel: ScanningViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    var mSession_id: String? = null
    var cachedUser: CachedUser = CachedUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_cart)
        setupUI()
        setupObserver()
        setCartItems()
    }

    private fun setupObserver(){
        scanningViewModel.result.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    Log.d("CartActivity", "--- Status.SUCCESS")
                    loading_ll.visibility = View.GONE
                    startActivity(Intent(this@CartActivity, SuccessCartActivity::class.java))
                }
                Status.LOADING -> {loading_ll.visibility = View.VISIBLE
                    Log.d("CartActivity", "--- Status.LOADING")}
                Status.ERROR -> {loading_ll.visibility = View.GONE
                    Log.d("CartActivity", "--- Status.ERROR")}
            }
        })

        scanningViewModel.cart.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { cart ->
                        if (cart.success) {
                            cart.data?.let {
                                if (!cart.data.session_id.isNullOrEmpty()) {
                                    mSession_id = cart.data.session_id
                                    cart.data.products?.let {
                                        renderList(it)
                                        updateUI(cart)
                                    }
                                }
                            } ?: kotlin.run{renderList(null)
                                updateUI(null)
                                mSession_id = null}
                        }else Toast.makeText(this, cart.message, Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    Log.d("ScanningActivity", "--- LOADING CART")
                }
            }
        })
    }

    private fun setupUI(){
        mRecycleView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(this@CartActivity, this, arrayListOf())
        mRecycleView.addItemDecoration(
            DividerItemDecoration(
                mRecycleView.context,
                (mRecycleView.layoutManager as LinearLayoutManager).orientation
            )
        )
        mRecycleView.adapter = adapter

        buy_btn.setOnClickListener(View.OnClickListener { v ->
            Utils.getDeviceIMEI(this)?.let { terminal ->
                cachedUser?.getUser()?.api_token?.let { token ->
                    mSession_id?.let { session_id ->
                        cachedUser?.getUser()?.lang?.let {
                            scanningViewModel.makeOrder(
                                it,
                                token,
                                terminal,
                                session_id,
                                "0599855959",
                                "3",
                                "",
                                ""
                            )
                        }
                    }
                }
            }
        })
    }

    private fun setCartItems(){
        CachedCart.getInstance().cart?.let { cart ->
            cart.products?.let { productsList ->
                adapter.addData(productsList)
            }
            mSession_id = cart.session_id
        }
    }

    private fun updateUI(cartModel: CartModel?){
        cartModel?.let {cartModel ->
            var productsSum = 0
            cartModel.data.products?.forEach { product -> productsSum += product.quantity}

            buy_btn.text = getString(R.string.buy).plus(" ${productsSum} ").plus(getString(R.string.buy_1))
                .plus(" ${cartModel.data.total} ").plus(getString(R.string.currency))
        } ?: kotlin.run { buy_btn.text = getString(R.string.buy) }
    }

    private fun renderList(products: List<Products>?) {
        if (products != null) {
            adapter.addData(products)
            adapter.notifyDataSetChanged()
        }else{
            adapter.addData(ArrayList<Products>())
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(item: Products?, position: Int) {
    }

    override fun incrementProduct(item: Products?, position: Int) {
        cachedUser.getUser()?.api_token?.let {token ->
            if (mSession_id != null){
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    item?.let { scanningViewModel.incrementProductByID(cachedUser?.getUser()?.lang!!, token, terminal, item?.id.toString(), mSession_id!!) }
                }
            }
        }
    }

    override fun decrementProduct(item: Products?, position: Int) {
        cachedUser.getUser()?.api_token?.let {token ->
            if (mSession_id != null){
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    item?.let {
                        scanningViewModel.decrementProductByID(
                            cachedUser?.getUser()?.lang!!,
                            token,
                            terminal,
                            item?.id.toString(),
                            mSession_id!!
                        )
                    }
                }
            }
        }
    }

    override fun deleteProduct(item: Products?, position: Int) {
        cachedUser.getUser()?.api_token?.let {token ->
            if (mSession_id != null){
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    item?.let {
                        scanningViewModel.deleteProductByID(
                            cachedUser?.getUser()?.lang!!,
                            token,
                            terminal,
                            item?.id.toString(),
                            mSession_id!!
                        )
                    }
                }
            }
        }
    }

}