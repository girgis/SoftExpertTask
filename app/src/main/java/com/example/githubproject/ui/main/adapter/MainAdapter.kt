package com.mindorks.framework.mvvm.ui.main.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.data.model.Region
import com.example.githubproject.data.model.User
import com.example.githubproject.ui.main.MainActivity
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(
    private val users: ArrayList<Region>,
    private val activity: Activity
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    val mActivity: Activity = activity

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(region: Region, activity: Activity) {
            itemView.textViewUserName.text = region.name + " - " + region.region
            itemView.setOnClickListener { v: View? ->
                if (activity is MainActivity){
                    (activity as MainActivity).setSelectedRegion(region.name)
                }
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

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position], mActivity)

    fun addData(list: List<Region>, page: Int) {
        Log.d("MainAdapter", "--- ++page ${page}")
        if (page == 1) {
            Log.d("MainAdapter", "--- page ${page}")
            users.clear()
        }
        users.addAll(list)
    }
}