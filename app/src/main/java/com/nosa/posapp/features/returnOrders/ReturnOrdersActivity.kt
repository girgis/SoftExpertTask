package com.nosa.posapp.features.returnOrders

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Cart
import com.nosa.posapp.data.model.Products
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.extensions.requestPermission
import com.nosa.posapp.features.orderDetails.OrderDetailsActivity
import com.nosa.posapp.features.orderDetails.adapters.ProductDetailsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_return_orders.*
import java.util.*

@AndroidEntryPoint
class ReturnOrdersActivity : AppCompatActivity(), ProductDetailsAdapter.OnItemClickListener {

    companion object{
        const val INTENT_CART = "intent_cart"
    }

    private lateinit var adapter: ProductDetailsAdapter
    var cart: Cart? = null
    val cachedUser: CachedUser = CachedUser(this)

    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_orders)

        getData()
        setupUI()
        initScanning()
        fillUI()
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
    }

    private fun setupUI(){
        mRecycleView_products.layoutManager = GridLayoutManager(this, 1)
        adapter = ProductDetailsAdapter(this, this, arrayListOf())
        mRecycleView_products.adapter = adapter

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
            if(result.getText() == null) {
                // Prevent duplicate scans
                return;
            }
            Toast.makeText(this@ReturnOrdersActivity, "Scanned: " + result.text, Toast.LENGTH_LONG).show()
            cachedUser.getUser()?.api_token?.let {
//                scanningViewModel.searchProductsByBarcode(it, result.text)
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

}