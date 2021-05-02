package com.tradex.pos.features.ordersHistory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tradex.pos.R
import com.tradex.pos.data.model.Cart
import com.tradex.pos.features.ordersHistory.HistoryType
import com.tradex.pos.features.ordersHistory.OrdersHistoryActivity
import kotlinx.android.synthetic.main.adapter_sell_history_item.view.*

class SellHistoryAdapter(val context: Context,
                         val listener: OnItemClickListener,
                         val cartList: ArrayList<Cart>): RecyclerView.Adapter<SellHistoryAdapter.DataViewHolder>(){

    var mContext:Context = context
    val mListener: OnItemClickListener? = listener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cart: Cart, context: Context) {
            var sum = 0
            itemView.bill_number_value_tv.text = cart.id.toString()
            for (product in cart.products!!){
                sum += product.quantity
            }
            itemView.bill_quantity_value_tv.text = sum.toString()
            itemView.bill_price_value_tv.text = cart.total.toString()
            itemView.setOnClickListener { v -> listener.onItemClick(cart, adapterPosition) }
            if (context is OrdersHistoryActivity) {
                if ((context as OrdersHistoryActivity).historyType == HistoryType.SELL) {
                    if (cart.is_order == 1) {
                        itemView.bill_number_tv.text = mContext.getString(R.string.invoice_number)
                    } else {
                        itemView.bill_number_tv.text = mContext.getString(R.string.unpaid_invoice_number)
                    }
                }else{
                    if (cart.is_order == 1) {
                        itemView.bill_number_tv.text = mContext.getString(R.string.bill_number)
                    } else {
                        itemView.bill_number_tv.text = mContext.getString(R.string.uncompleted_order)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_sell_history_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = cartList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(cartList[position], mContext)

    fun addData(list: List<Cart>) {
        if (cartList != null && !cartList.isEmpty())
            cartList.clear()
        cartList.addAll(list)
    }

    fun addToData(list: List<Cart>) {
        cartList?.addAll(list)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Cart?, position: Int)
    }

}
