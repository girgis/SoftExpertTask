package com.tradex.pos.features.orderDetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tradex.pos.R
import com.tradex.pos.data.model.Products
import com.tradex.pos.db.CachedUser
import kotlinx.android.synthetic.main.adapter_product_item.view.*

class ProductDetailsAdapter(val context: Context,
                            val listener: OnItemClickListener,
                            val productList: ArrayList<Products>): RecyclerView.Adapter<ProductDetailsAdapter.DataViewHolder>(){

    var mContext:Context = context
    val mListener: OnItemClickListener? = listener
    val cachedUser: CachedUser = CachedUser(context)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Products, context: Context) {
            if (cachedUser?.getUser()?.lang == "ar"){
                itemView.product_value_tv.text = product.product?.name_ar
            }else {
                itemView.product_value_tv.text = product.product?.name
            }
            itemView.quantity_value_tv.text = product.quantity.toString()
            itemView.price_value_tv.text = product.price.toString()

            itemView.item_plus_iv.setOnClickListener(View.OnClickListener { mListener?.incrementProduct(product, adapterPosition) })
            itemView.item_minus_iv.setOnClickListener(View.OnClickListener { mListener?.decrementProduct(product, adapterPosition) })
            itemView.detele_product.setOnClickListener(View.OnClickListener { mListener?.deleteProduct(product, adapterPosition) })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_product_details_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(productList[position], mContext)

    fun addData(list: List<Products>) {
        if (productList != null && !productList.isEmpty())
            productList.clear()
        productList.addAll(list)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Products?, position: Int)
        fun incrementProduct(item: Products?, position: Int)
        fun decrementProduct(item: Products?, position: Int)
        fun deleteProduct(item: Products?, position: Int)
    }

}
