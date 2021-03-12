package com.mindorks.framework.mvvm.ui.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubproject.R
import com.example.githubproject.data.model.FavoriteProducts
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*

class SecondAdapter(
    val mContext: Context,
    val listener: OnItemClickListener,
    private val products : ArrayList<FavoriteProducts>
) : RecyclerView.Adapter<SecondAdapter.DataViewHolder>() {

    var context:Context = mContext
    val mListener: OnItemClickListener? = listener

    interface OnItemClickListener {
        fun onItemClick(item: FavoriteProducts?, position: Int)
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: FavoriteProducts, context: Context) {
            itemView.textViewUserName.text = product.ProductName
            itemView.textIsUsed.visibility = View.VISIBLE
            itemView.textIsUsed.text = "Time to expire ".plus(getDateDifference(product.updatedAt))
            itemView.addToFavorite.text = "REMOVE"
            itemView.addToFavorite.setOnClickListener {
                mListener?.onItemClick(product, adapterPosition)
            }
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

    fun addData(list: List<FavoriteProducts>) {
        if (products != null && !products.isEmpty())
            products.clear()
        products.addAll(list)
    }

    fun getDateDifference(mDate: Date): String{
        val calendar = Calendar.getInstance()
        calendar.time = mDate
        calendar.add(Calendar.DAY_OF_YEAR, 3)
        val diff = calendar.timeInMillis - Calendar.getInstance().timeInMillis

        if (diff > 0) {
            val numOfDays = (diff / (1000 * 60 * 60 * 24)).toInt()
            val hours = (diff / (1000 * 60 * 60)).toInt()
            val minutes = (diff / (1000 * 60)).toInt()
            val seconds = (diff / 1000).toInt()

            Log.d("SecondAdapter", "--- hours: ${hours}")

            var hoursS = hours.toString()
            if (hours < 10)
                hoursS = "0".plus(hours)

            return hoursS.plus(" h")
        }else return ""
    }
}