package com.nosa.posapp.features.orderDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Cart
import com.nosa.posapp.data.model.Products
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.features.orderDetails.adapters.ProductDetailsAdapter
import com.nosa.posapp.features.ordersHistory.HistoryType
import com.nosa.posapp.features.ordersHistory.OrdersHistoryActivity
import com.nosa.posapp.features.ordersHistory.adapter.SellHistoryAdapter
import com.nosa.posapp.features.returnOrders.ReturnOrdersActivity
import com.nosa.posapp.features.scanning.adapters.ProductAdapter
import com.nosa.posapp.features.success.SuccessfulActivity
import com.nosa.posapp.utils.Utils
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_details.buy_btn
import kotlinx.android.synthetic.main.activity_order_details.total_price_value_tv
import kotlinx.android.synthetic.main.activity_order_details.total_products_value_tv
import kotlinx.android.synthetic.main.activity_orders_history.*
import kotlinx.android.synthetic.main.activity_orders_history.back_iv
import kotlinx.android.synthetic.main.activity_orders_history.mRecycleView
import kotlinx.android.synthetic.main.activity_scanning.*

class OrderDetailsActivity : AppCompatActivity(), ProductDetailsAdapter.OnItemClickListener {

    private lateinit var adapter: ProductDetailsAdapter
    var cart: Cart? = null
    val cachedUser:CachedUser = CachedUser(this)
    private var historyType: HistoryType = HistoryType.SELL

    companion object{
        const val INTENT_CART = "intent_cart"
        const val INTENT_TYPE = "intent_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_order_details)

        getData()
        setupUI()
        fillUI()
    }

    private fun getData(){
        cart = intent.getParcelableExtra(INTENT_CART) as Cart
        if (intent.hasExtra(INTENT_TYPE))
            historyType = intent.getSerializableExtra(INTENT_TYPE) as HistoryType
        Log.d("OrderDetailsActivity", "--- cart: ${cart}")
    }

    private fun setupUI(){
        when(historyType){
            HistoryType.SELL -> {details_title_tv.text = getString(R.string.invoice_details_title)}
            HistoryType.STOCK -> {details_title_tv.text = getString(R.string.order_details_title)}
        }

        back_iv.setOnClickListener { v -> finish() }
        buy_btn.setOnClickListener(View.OnClickListener { v ->
            cart?.let { startActivity(Intent(this@OrderDetailsActivity, SuccessfulActivity::class.java)
                .putExtra(SuccessfulActivity.INTENT_SESSION, it.session_id)) }
        })

        return_btn.setOnClickListener { v -> startActivity(Intent(this@OrderDetailsActivity, ReturnOrdersActivity::class.java)
                .putExtra(ReturnOrdersActivity.INTENT_CART, cart)) }

        mRecycleView.layoutManager = GridLayoutManager(this, 1)
        adapter = ProductDetailsAdapter(this, this, arrayListOf())
        mRecycleView.adapter = adapter
    }

    private fun fillUI(){
        cart?.let {cart ->
            cart.products?.let {
                adapter.addData(it)
                adapter.notifyDataSetChanged()

                var productsSum = 0
                cart.products?.forEach { product -> productsSum += product.quantity}
                total_products_value_tv.text = productsSum.toString()
                total_price_value_tv.text = cart.total.toString()
            }
        }
    }

    override fun onItemClick(item: Products?, position: Int) {
    }

    override fun incrementProduct(item: Products?, position: Int) {
    }

    override fun decrementProduct(item: Products?, position: Int) {
    }

    override fun deleteProduct(item: Products?, position: Int) {
    }

}