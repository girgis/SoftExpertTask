package com.mindorks.framework.mvvm.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.R
import com.example.githubproject.data.model.FavoriteProducts
import com.example.githubproject.data.model.Product
import com.example.githubproject.ui.main.MainActivity
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainAdapter(
    val mContext: Context,
    val listener: OnItemClickListener,
    private val products : ArrayList<Product>
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    var context:Context = mContext
    val mListener: OnItemClickListener? = listener

    interface OnItemClickListener {
        fun onItemClick(item: Product?, position: Int)
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, context: Context) {
            itemView.textViewUserName.text = product.ProductName
            itemView.addToFavorite.text = "ADD TO CART"
            itemView.addToFavorite.setOnClickListener {
                mListener?.onItemClick(product, adapterPosition)
            }
            if (context is MainActivity){
                GlobalScope.launch(Dispatchers.IO) {
                    if (!(context as MainActivity).db.favoriteProductsDao().getAll().isNullOrEmpty()) {
                        for (p in (context as MainActivity).db.favoriteProductsDao().getAll()) {
                            if (p.product_id == product.uid) {
                                context.runOnUiThread(Runnable { itemView.addToFavorite.text = "REMOVE" })
                            }
                        }
                    }
                }
            }
//            itemView.textViewUserEmail.text = user.constractionYear
//            itemView.textIsUsed.text = "is used: ".plus(user.isUsed)
//            Glide.with(itemView.imageViewAvatar.context)
//                .load(user.imageUrl)
//                .into(itemView.imageViewAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(products[position], context)

    fun addData(list: List<Product>) {
        products.addAll(list)
    }
}