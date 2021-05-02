package com.tradex.pos.features.store.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tradex.pos.R
import com.tradex.pos.data.model.Category
import kotlinx.android.synthetic.main.adapter_category_item.view.*

class CategoryAdapter(val context: Context,
                      val listener: OnItemClickListener,
                      val categoryList: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.DataViewHolder>(){

    var mContext:Context = context
    val mListener: OnItemClickListener? = listener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cat: Category, context: Context) {
            itemView.catName_tv.text = cat.name
            if (!cat.image.isNullOrEmpty())
                Glide.with(context).load(cat.imagefullpath).centerCrop().into(itemView.catImage_iv)
            itemView.setOnClickListener { v -> listener.onItemClick(cat, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_category_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(categoryList[position], mContext)

    fun addData(list: List<Category>) {
        if (categoryList != null && !categoryList.isEmpty())
            categoryList.clear()
        categoryList.addAll(list)
    }

    fun addToData(list: List<Category>){
        if (categoryList != null && !categoryList.isEmpty())
            categoryList.addAll(list)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Category?, position: Int)
    }

}
