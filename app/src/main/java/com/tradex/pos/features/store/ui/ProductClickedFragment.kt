package com.tradex.pos.features.store.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.githubproject.utils.Status
import com.tradex.pos.R
import com.tradex.pos.data.model.Product
import com.tradex.pos.db.CachedCart
import com.tradex.pos.features.store.products.IOnBackPressed
import com.tradex.pos.features.store.products.StoreProductsActivity
import com.tradex.pos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_clicked_fragment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ProductClickedFragment : Fragment(), View.OnClickListener, IOnBackPressed {

    private var mMode: Int = -1
    private var product: Product? = null
    private lateinit var viewModel: ProductClickedViewModel
    private var activity: StoreProductsActivity? = null

    companion object {
        const val TAG = "product_clicked_fragment"
        fun newInstance(param1: Product) = ProductClickedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PARAM1, param1)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is StoreProductsActivity)
            activity = context as StoreProductsActivity
    }

    override fun onDetach() {
        super.onDetach()
        if (activity != null)
            activity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_clicked_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductClickedViewModel::class.java)
        setupObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable<Product>(ARG_PARAM1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inquire_btn.setOnClickListener(this)
        addToCart_btn.setOnClickListener(this)
        go_btn.setOnClickListener(this)
        success_btn.setOnClickListener(this)
        setImageDrawable(0)
    }

    private fun setupObserver(){
        this!!.getActivity()?.let {
            viewModel.cart.observe(it, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data?.data != null) {
                            CachedCart.getInstance().cart = it.data.data
                            it.data?.data?.products?.size?.let { it1 -> activity?.updateBadge(it1) }
                            setViewsVisibility(3)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(getActivity(), it.message, Toast.LENGTH_LONG).show()
                        Log.d("ScanningActivity", "--- ${it.data.toString()}")
                    }
                    Status.LOADING -> {
                        Log.d("ScanningActivity", "--- LOADING CART")
                    }
                }
            })
        }
    }

    private fun setImageDrawable(mode: Int){
        val unwrappedDrawable = AppCompatResources.getDrawable(context!!, R.drawable.inquire)
        val unwrappedDrawable2 = AppCompatResources.getDrawable(context!!, R.drawable.shopping_cart_2)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        val wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2!!)
        when(mode){
            0 -> {// no selected
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.colorAccent, null))
                DrawableCompat.setTint(wrappedDrawable2, resources.getColor(R.color.colorAccent, null))
                inquire_btn.setBackgroundResource(R.drawable.bg)
                addToCart_btn.setBackgroundResource(R.drawable.bg)
            }
            1 -> {// first item selected
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.white, null))
                DrawableCompat.setTint(wrappedDrawable2, resources.getColor(R.color.colorAccent, null))
                inquire_btn.setBackgroundResource(R.drawable.blue_rectangle)
                addToCart_btn.setBackgroundResource(R.drawable.bg)
            }
            2 -> {// second item selected
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.colorAccent, null))
                DrawableCompat.setTint(wrappedDrawable2, resources.getColor(R.color.white, null))
                inquire_btn.setBackgroundResource(R.drawable.bg)
                addToCart_btn.setBackgroundResource(R.drawable.blue_rectangle)
            }
        }
        inquire_btn.setImageDrawable(unwrappedDrawable)
        addToCart_btn.setImageDrawable(unwrappedDrawable2)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.inquire_btn -> {
                mMode = 1
                setImageDrawable(1)
                setProductDetails()
                setViewsVisibility(1)
            }
            R.id.addToCart_btn -> {
                mMode = 2
                setImageDrawable(2)
            }
            R.id.go_btn -> {
                when (mMode) {
                    1 -> { }
                    2 -> {
                        activity?.cachedUser?.getUser()?.let { user ->
                            Utils.getDeviceIMEI(activity!!)?.let { terminal ->
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
                                } ?: viewModel.createCart(
                                    user.lang,
                                    user.api_token,
                                    terminal,
                                    product?.id.toString()
                                )
                            }
                        }
                    }
                    else -> Toast.makeText(context, "Please select an action", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.success_btn -> {
                activity?.onBackPressed()
            }
        }
    }

    private fun setProductDetails(){
        product?.let {product ->
            product_name_value_tv.text = product.name
            quantity_name_value_tv.text = product?.quantity?.toString()
            price_name_value_tv.text = product.price.toString()
        }
    }

    private fun setViewsVisibility(mode: Int){
        when(mode){
            1 ->{product_clicked_cl.visibility = View.GONE
                product_details_cl.visibility = View.VISIBLE
                success_result_cl.visibility = View.GONE}
            2 -> {
                product_clicked_cl.visibility = View.VISIBLE
                product_details_cl.visibility = View.GONE
                success_result_cl.visibility = View.GONE }
            3 -> {
                product_clicked_cl.visibility = View.GONE
                product_details_cl.visibility = View.GONE
                success_result_cl.visibility = View.VISIBLE }
        }
    }

    override fun onBackPressed(): Boolean {
        return if (mMode == 1) {
            product_clicked_cl.visibility = View.VISIBLE
            product_details_cl.visibility = View.GONE
            mMode = -1
            setImageDrawable(0)
            true
        } else {
            false
        }
    }
}
