package com.tradex.pos.features.scanning

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.utils.Status
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.tradex.pos.R
import com.tradex.pos.data.model.Cart
import com.tradex.pos.data.model.CartModel
import com.tradex.pos.data.model.Products
import com.tradex.pos.db.CachedUser
import com.tradex.pos.extensions.requestPermission
import com.tradex.pos.features.orderDetails.OrderDetailsActivity
import com.tradex.pos.features.scanning.adapters.ProductAdapter
import com.tradex.pos.features.scanning.ui.PaymentMethodsFragment
import com.tradex.pos.features.success.SuccessfulActivity
import com.tradex.pos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_scanning.*
import kotlinx.android.synthetic.main.activity_scanning.search_by_barcode_et
import kotlinx.android.synthetic.main.activity_scanning.search_img
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ScanningActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    companion object{
        const val INTENT_CART = "intent_cart"
    }

    private lateinit var adapter: ProductAdapter

    private val scanningViewModel: ScanningViewModel by viewModels()
    var cachedUser: CachedUser = CachedUser(this)
    var cart: Cart? = null

    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null
    private var lastText: String? = null
    var mSession_id: String? = null
    var scanTime: Long = 0

    //doooooooooooon't forgot to save session_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedUser?.getUser()?.let { Utils.setLocale(this, it?.lang) }
        setContentView(R.layout.activity_scanning)

        setupUI()
        initScanning()
        getData()
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    private fun initScanning(){
        barcodeView = findViewById(R.id.barcode_scanner)
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
            Toast.makeText(this@ScanningActivity, "Scanned: " + result.text, Toast.LENGTH_LONG).show()
            cachedUser.getUser()?.api_token?.let {
                Utils.getDeviceIMEI(this@ScanningActivity)?.let { terminal_id ->
                        cachedUser?.getUser()?.lang?.let { it1 ->
                        scanningViewModel.searchProductsByBarcode(it1, it, terminal_id, result.text)
                     }
                }
            }
            beepManager?.playBeepSoundAndVibrate()
        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private fun getData(){
        if (intent.hasExtra(OrderDetailsActivity.INTENT_CART)) {
            cart = intent.getParcelableExtra(OrderDetailsActivity.INTENT_CART) as Cart
            Log.d("OrderDetailsActivity", "--- cart: ${cart}")
            cart?.session_id?.let { session_id ->
                mSession_id = session_id
                cachedUser.getUser()?.let { user ->
                    Utils.getDeviceIMEI(this@ScanningActivity)?.let { terminal_id ->
                        scanningViewModel.getCart(user.lang, user.api_token, terminal_id, session_id)
                    }
                }
            }
        }
    }

    private fun setupUI(){
        scanning_recycleView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(this@ScanningActivity, this, arrayListOf())
        scanning_recycleView.addItemDecoration(
            DividerItemDecoration(
                scanning_recycleView.context,
                (scanning_recycleView.layoutManager as LinearLayoutManager).orientation
            )
        )
        scanning_recycleView.adapter = adapter

        buy_btn.setOnClickListener(View.OnClickListener {
            if (isValidatePayment())
                addFragmentToActivity(PaymentMethodsFragment.newInstance("",""))
        })

        search_by_barcode_et.setOnKeyListener { v, keyCode, event ->
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
        if (isValidDataBar()) {
            cachedUser.getUser()?.api_token?.let {
                Utils.getDeviceIMEI(this)?.let { terminal_id ->
                    cachedUser?.getUser()?.lang?.let { it1 ->
                        scanningViewModel.searchProductsByBarcode(
                            it1, it, terminal_id, search_by_barcode_et.text.toString())
                    }
                }
            }
        } else {Toast.makeText(this@ScanningActivity, getString(R.string.write_barCode), Toast.LENGTH_SHORT).show()}
    }

    private fun isValidDataBar(): Boolean{
        if (search_by_barcode_et.text.isNullOrEmpty()){
            Toast.makeText(this, getString(R.string.enter_barcode), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addFragmentToActivity(fragment: Fragment?){
        if (fragment == null) return
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
        tr.add(android.R.id.content, fragment, PaymentMethodsFragment.TAG)
        tr.addToBackStack(PaymentMethodsFragment.TAG)
        tr.commitAllowingStateLoss()
    }

    private fun setupObserver() {
        scanningViewModel.cart.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll_scan.visibility = View.GONE
                    it.data?.let { cart ->
                        if (cart.success) {
                            cart.data?.let {
                                if (!cart.data.session_id.isNullOrEmpty()) {
                                    this.cart = cart.data
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
                    loading_ll_scan.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    loading_ll_scan.visibility = View.VISIBLE
                    Log.d("ScanningActivity", "--- LOADING CART")
                }
            }
        })

        scanningViewModel.product.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_ll_scan.visibility = View.GONE
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                    it.data?.let {productModel ->
                        if (productModel.success){
                            scanning_recycleView.visibility = View.VISIBLE
                            barcode_scanning_ll.visibility = View.GONE
                            cachedUser.getUser()?.api_token?.let {
                                Utils.getDeviceIMEI(this@ScanningActivity)?.let { terminal ->
                                    if (!productModel.data.isNullOrEmpty()) {
                                        if (mSession_id != null) {
                                            scanningViewModel.addToCart(
                                                cachedUser?.getUser()?.lang!!,
                                                it,
                                                terminal,
                                                productModel.data[0].id.toString(),
                                                mSession_id!!
                                            )
                                        } else {
                                            scanningViewModel.createCart(
                                                cachedUser?.getUser()?.lang!!,
                                                it,
                                                terminal,
                                                productModel.data[0].id.toString()
                                            )
                                        }
                                    } else Toast.makeText(
                                        this,
                                        "No Items founded!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }else Toast.makeText(this, productModel.message, Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    loading_ll_scan.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Log.d("ScanningActivity", "--- ${it.data.toString()}")
                }
                Status.LOADING -> {
                    loading_ll_scan.visibility = View.VISIBLE
                    Log.d("ScanningActivity", "--- LOADING")
                }
            }
        })
    }

    private fun updateUI(cartModel: CartModel?){
        cartModel?.let {cartModel ->
            var productsSum = 0
            cartModel.data.products?.forEach { product -> productsSum += product.quantity}
            total_products_value_tv.text = productsSum.toString()
            total_price_value_tv.text = cartModel.data.total.toString()

            buy_btn.text = getString(R.string.buy).plus(" ${productsSum} ").plus(getString(R.string.buy_1))
                .plus(" ${cartModel.data.total} ").plus(getString(R.string.currency))
        } ?: kotlin.run { buy_btn.text = getString(R.string.buy)
            total_products_value_tv.text = "0"
            total_price_value_tv.text = "0"}
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

    private fun isValidatePayment():Boolean{
        if (mSession_id.isNullOrEmpty())
            return false
        return true
    }

    fun goToSuccess(){
        mSession_id?.let { startActivity(Intent(this@ScanningActivity, SuccessfulActivity::class.java)
            .putExtra(SuccessfulActivity.INTENT_SESSION, it)) }
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this@ScanningActivity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            requestPermission()
    }

    private fun isValidData(): Boolean{
        if (search_by_barcode_et.text.isNullOrEmpty()){
            Toast.makeText(this@ScanningActivity, getString(R.string.id_required), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onItemClick(item: Products?, position: Int) {
    }

    override fun incrementProduct(item: Products?, position: Int) {
        cachedUser.getUser()?.api_token?.let {token ->
            if (mSession_id != null){
                Utils.getDeviceIMEI(this)?.let { terminal ->
                    item?.let {
                        scanningViewModel.incrementProductByID(
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