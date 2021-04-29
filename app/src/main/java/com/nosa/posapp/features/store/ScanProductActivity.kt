package com.nosa.posapp.features.store

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.githubproject.utils.Status
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Product
import com.nosa.posapp.db.CachedCart
import com.nosa.posapp.db.CachedUser
import com.nosa.posapp.extensions.requestPermission
import com.nosa.posapp.features.scanning.ScanningViewModel
import com.nosa.posapp.features.store.ui.ProductClickedViewModel
import com.nosa.posapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_scan_product.*
import kotlinx.android.synthetic.main.activity_scanning.*
import java.util.*

@AndroidEntryPoint
class ScanProductActivity : AppCompatActivity() {

    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null
    private var lastText: String? = null
    private var product: Product? = null

    private val viewModel: ProductClickedViewModel by viewModels()
    private val scanningViewModel: ScanningViewModel by viewModels()

    var cachedUser: CachedUser = CachedUser(this)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_scan_product)

        initScanning()
        setupUI()
        setupObserver()
    }

    private fun initScanning(){
        barcodeView = findViewById(R.id.barcode_scanner_2)
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView?.getBarcodeView()?.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView?.initializeFromIntent(intent)
        barcodeView?.decodeContinuous(callback)
        beepManager = BeepManager(this)
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            lastText = result.text
            Toast.makeText(this@ScanProductActivity, "Scanned: " + result.text, Toast.LENGTH_LONG).show()
            cachedUser.getUser()?.api_token?.let {
                Utils.getDeviceIMEI(this@ScanProductActivity)?.let {terminal ->
                    scanningViewModel.searchProductsByBarcode(cachedUser?.getUser()?.lang!!, it, terminal, result.text)
                }
            }

            beepManager?.playBeepSoundAndVibrate()
        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private fun setupUI(){
        scan_product_details_cl.visibility = View.GONE
        close_details_iv.setOnClickListener(View.OnClickListener { scan_product_details_cl.visibility = View.GONE })
        addToCartBlue_btn.setOnClickListener { v -> addProductToCart() }
    }

    private fun setupObserver() {
        scanningViewModel.product.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                    it.data?.let {productModel ->
                        if (productModel.success){
                            scan_product_details_cl.visibility = View.VISIBLE
                            if (it.data.data != null && it.data.data.size > 0)
                                setProductDetails(it.data.data.get(0))
                        }else Toast.makeText(this, productModel.message, Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    loading_ll.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    loading_ll.visibility = View.VISIBLE
                    Log.d("ScanningActivity", "--- LOADING")
                }
            }
        })
        viewModel.cart.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll.visibility = View.GONE
                    if (it.data?.data != null) {
                        CachedCart.getInstance().cart = it.data.data
                        finish()
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
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this@ScanProductActivity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            requestPermission()
    }

    private fun setProductDetails(product: Product){
        this.product = product
        product_description_tv.text = product.name
        cart_quantity_tv.text = getString(R.string.remaining).plus(" ${product.remaining_quantity}")
    }

    private fun addProductToCart(){
        if (product != null) {
            cachedUser?.getUser()?.let { user ->
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    CachedCart.getInstance().cart?.let {
                        it.session_id?.let { it1 ->
                            viewModel.addToCart(
                                user.lang,
                                user.api_token,
                                terminal,
                                product?.id.toString(),
                                it1
                            )
                        }
                    } ?: viewModel.createCart(user.lang, user.api_token, terminal, product?.id.toString())
                }
            }
        }
    }


}