package com.nosa.posapp.features.store.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nosa.posapp.R
import com.nosa.posapp.data.model.Category
import com.nosa.posapp.data.model.Product
import com.nosa.posapp.data.model.Products
import kotlinx.android.synthetic.main.adapter_category_item.view.*
import kotlinx.android.synthetic.main.adapter_category_item.view.catImage_iv
import kotlinx.android.synthetic.main.adapter_category_product_item.view.*
import kotlinx.android.synthetic.main.adapter_product_item.view.*

class CategoryProductAdapter(val context: Context,
                             val listener: OnItemClickListener,
                             val productList: ArrayList<Product>): RecyclerView.Adapter<CategoryProductAdapter.DataViewHolder>(){

    var mContext:Context = context
    val mListener: OnItemClickListener? = listener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, context: Context) {
            itemView.productName_tv.text = product.name
            itemView.productQuantity_tv.text = product.quantity
            itemView.productQuantityType_tv.text = product.special
            if (!product.images.isNullOrEmpty())
                Glide.with(context).load(product.images[0].imagefullpath).centerCrop().into(itemView.catImage_iv)
            itemView.setOnClickListener { v -> listener.onItemClick(product, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_category_product_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(productList[position], mContext)

    fun addData(list: List<Product>) {
        if (productList != null && !productList.isEmpty())
            productList.clear()
        productList.addAll(list)
    }

    fun addToData(list: List<Product>) {
        productList?.addAll(list)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Product?, position: Int)
    }

}
