package com.nosa.posapp.features.returnOrders

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubproject.utils.Status
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Cart
import com.nosa.posapp.data.model.Product
import com.nosa.posapp.data.model.Products
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.extensions.requestPermission
import com.nosa.posapp.features.cart.success.SuccessCartActivity
import com.nosa.posapp.features.orderDetails.OrderDetailsActivity
import com.nosa.posapp.features.orderDetails.adapters.ProductDetailsAdapter
import com.nosa.posapp.features.ordersHistory.HistoryType
import com.nosa.posapp.features.returnOrders.adapters.ProductsReturnedAdapter
import com.nosa.posapp.features.returnOrders.viewModel.ReturnOrderViewModel
import com.nosa.posapp.features.scanning.ScanningViewModel
import com.nosa.posapp.features.success.SuccessfulActivity
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_return_orders.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ReturnOrdersActivity : AppCompatActivity(), ProductDetailsAdapter.OnItemClickListener,
    ProductsReturnedAdapter.OnItemClickListener {

    companion object{
        const val INTENT_CART = "intent_cart"
        const val HISTORY_TYPE = "history_type"
    }

    private lateinit var adapter: ProductDetailsAdapter
    private lateinit var adapter_returned: ProductsReturnedAdapter
    var cart: Cart? = null
    val cachedUser: CachedUser = CachedUser(this)
    var scanTime: Long = 0
    var is_item_added = false
    val returnedList = arrayListOf<Product>()
    var historyType: HistoryType = HistoryType.SELL

    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null
    private val scanningViewModel: ScanningViewModel by viewModels()
    private val returnOrderViewModel: ReturnOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_orders)

        getData()
        setupUI()
        initScanning()
        fillUI()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        barcodeView!!.resume()

        checkPermissions()
    }

    override fun onPause() {
        super.onPause()
        barcodeView!!.pause()
    }

    fun pause(view: View?) {
        barcodeView!!.pause()
    }

    fun resume(view: View?) {
        barcodeView!!.resume()
    }

    fun triggerScan(view: View?) {
        barcodeView!!.decodeSingle(callback)
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this@ReturnOrdersActivity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            requestPermission()
    }

    private fun getData(){
        cart = intent.getParcelableExtra(OrderDetailsActivity.INTENT_CART) as Cart
        historyType = intent.getSerializableExtra(ReturnOrdersActivity.HISTORY_TYPE) as HistoryType
    }

    private fun setupUI(){
        mRecycleView_products.layoutManager = GridLayoutManager(this, 1)
        adapter = ProductDetailsAdapter(this, this, arrayListOf())
        mRecycleView_products.adapter = adapter

        mRecycleView_returned.layoutManager = GridLayoutManager(this, 1)
        adapter_returned = ProductsReturnedAdapter(this, this, arrayListOf())
        mRecycleView_returned.adapter = adapter_returned

        return_btn.setOnClickListener(View.OnClickListener { v ->
            if (!returnedList.isNullOrEmpty()){
                cachedUser?.getUser()?.let { user ->
                    Utils.getDeviceIMEI(this)?.let { terminal ->
                        cart?.session_id?.let {
                            returnOrderViewModel.returnOrderRequest(user.lang, user.api_token, terminal, it, returnedList)
                        }
                    }
                }
            }else Toast.makeText(this, getString(R.string.specify_returened_items), Toast.LENGTH_SHORT).show()
        })

        search_img.setOnClickListener(View.OnClickListener { v ->
            if (!search_by_barcode_et.text.isNullOrEmpty()){
                Utils.getDeviceIMEI(this@ReturnOrdersActivity)?.let { terminal_id ->
                    cachedUser.getUser()?.let {
                        scanningViewModel.searchProductsByBarcode(it.lang, it.api_token, terminal_id, search_by_barcode_et.text.toString())
                    }
                }
            }
        })
    }

    private fun setupObserver(){
        returnOrderViewModel.result.observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.SUCCESS -> {
                    loading_ll_2.visibility = View.GONE
                    if (it?.data?.success!!){
                        when(historyType){
                            HistoryType.SELL -> {
                                startActivity(
                                    Intent(this@ReturnOrdersActivity, SuccessfulActivity::class.java)
                                        .putExtra(SuccessfulActivity.INTENT_SESSION, cart?.session_id))
                            }
                            HistoryType.STOCK -> {
                                startActivity(Intent(this@ReturnOrdersActivity, SuccessCartActivity::class.java)
                                    .putExtra(SuccessfulActivity.INTENT_SESSION, cart?.session_id))
                            }
                        }
                    }
                }
                Status.LOADING -> {
                    loading_ll_2.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_ll_2.visibility = View.GONE
                }
            }
        })

        scanningViewModel.product.observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.SUCCESS -> {
                    loading_ll_2.visibility = View.GONE
                    it.data?.let { product ->
                        product.data?.let { list ->
                            if (list.size > 0) {
                                list?.get(0)?.let { product ->
                                    cart?.products?.forEach { products: Products ->
                                        if (products.product?.id == product.id) {
                                            if (!returnedList.contains(product)){
                                                list.get(0).order_product_id = products.id
                                                list.get(0).quantity = 1
                                                returnedList.addAll(list)
                                                adapter_returned.addData(returnedList)
                                                adapter_returned.notifyDataSetChanged()
                                                is_item_added = true
                                            }
                                        }
                                    }
                                    if (!is_item_added) {
                                        is_item_added = false
                                        Toast.makeText(this@ReturnOrdersActivity, getString(R.string.list_does_not_contains), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
                Status.LOADING -> {loading_ll_2.visibility = View.VISIBLE}
                Status.ERROR -> {
                    loading_ll_2.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun fillUI(){
        cart?.let {cart ->
            cart.products?.let {
                adapter.addData(it)
                adapter.notifyDataSetChanged()

                var productsSum = 0
//                cart.products?.forEach { product -> productsSum += product.quantity}
//                total_products_value_tv.text = productsSum.toString()
//                total_price_value_tv.text = cart.total.toString()
            }
        }
    }

    private fun initScanning(){
        barcodeView = findViewById(R.id.barcode_scanner_1)
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView?.getBarcodeView()?.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView?.initializeFromIntent(intent)
        barcodeView?.decodeContinuous(callback)
        beepManager = BeepManager(this)
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if(result.getText() == null || (System.currentTimeMillis() - scanTime) <= 2000) {
                // Prevent duplicate scans
                return;
            }
            scanTime = System.currentTimeMillis()
//            Toast.makeText(this@ReturnOrdersActivity, "Scanned: " + result.text, Toast.LENGTH_LONG).show()
            Utils.getDeviceIMEI(this@ReturnOrdersActivity)?.let { terminal_id ->
                cachedUser.getUser()?.let {
                    scanningViewModel.searchProductsByBarcode(it.lang, it.api_token, terminal_id, result.text)
                }
            }
            beepManager?.playBeepSoundAndVibrate()
        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onItemClick(item: Products?, position: Int) {
    }

    override fun incrementProduct(item: Products?, position: Int) {
    }

    override fun decrementProduct(item: Products?, position: Int) {
    }

    override fun deleteProduct(item: Products?, position: Int) {
    }

    override fun onReturnedItemClick(item: Product?, position: Int) {
    }

    override fun incrementReturnedProduct(item: Product?, position: Int) {
        if (!cart?.products.isNullOrEmpty()){
            cart?.products?.forEach { product: Products ->
                Log.d("ids", "--- ${product.product?.id} : ${item?.id}")
                if (product.product?.id == item?.id){
                    Log.d("quantity", "--- ${product.quantity} : ${item?.quantity}")
                    if (item?.quantity!! < product.quantity) {
                        item.quantity++
                        adapter_returned.notifyDataSetChanged()
                    }else Toast.makeText(this, getString(R.string.valid_returned_quantity), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun decrementReturnedProduct(item: Product?, position: Int) {
        if (!returnedList.isNullOrEmpty()){
            returnedList.forEach { product: Product ->
                if (product.id == item?.id){
                    if (product.quantity > 1) {
                        product.quantity--
                        adapter_returned.notifyDataSetChanged()
                    }
//                    else Toast.makeText(this@ReturnOrdersActivity, getString(R.string.change_password))
                }
            }
        }
    }

    override fun deleteReturnedProduct(item: Product?, position: Int) {
        if (!returnedList.isNullOrEmpty()){
            returnedList.forEach { product: Product ->
                if (product.id == item?.id){
                    returnedList.remove(item)

                    adapter_returned.addData(returnedList)

                    adapter_returned.notifyDataSetChanged()
                }
            }
        }
    }

}