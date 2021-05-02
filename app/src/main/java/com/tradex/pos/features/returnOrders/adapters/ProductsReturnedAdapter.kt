package com.tradex.pos.features.returnOrders.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tradex.pos.R
import com.tradex.pos.data.model.Product
import kotlinx.android.synthetic.main.adapter_product_item.view.*

class ProductsReturnedAdapter(val context: Context,
                              val listener: OnItemClickListener,
                              val productList: ArrayList<Product>): RecyclerView.Adapter<ProductsReturnedAdapter.DataViewHolder>(){

    var mContext:Context = context
    val mListener: OnItemClickListener? = listener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, context: Context) {
            itemView.product_value_tv.text = product?.name
            itemView.quantity_value_tv.text = product.quantity.toString()
            itemView.price_value_tv.text = product.price.toString()

            itemView.item_plus_iv.setOnClickListener(View.OnClickListener { listener.incrementReturnedProduct(product, adapterPosition) })
            itemView.item_minus_iv.setOnClickListener(View.OnClickListener { listener.decrementReturnedProduct(product, adapterPosition) })
            itemView.detele_product.setOnClickListener(View.OnClickListener { listener.deleteReturnedProduct(product, adapterPosition) })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_product_item, parent,
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

    interface OnItemClickListener {
        fun onReturnedItemClick(item: Product?, position: Int)
        fun incrementReturnedProduct(item: Product?, position: Int)
        fun decrementReturnedProduct(item: Product?, position: Int)
        fun deleteReturnedProduct(item: Product?, position: Int)
    }

}
