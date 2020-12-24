package com.mindorks.framework.mvvm.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.data.model.User
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.textViewUserName.text = user.brand
            itemView.textViewUserEmail.text = user.constractionYear
            itemView.textIsUsed.text = "is used: ".plus(user.isUsed)
            Glide.with(itemView.imageViewAvatar.context)
                .load(user.imageUrl)
                .into(itemView.imageViewAvatar)
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
        holder.bind(users[position])

    fun addData(list: List<User>, page: Int) {
        Log.d("MainAdapter", "--- ++page ${page}")
        if (page == 1) {
            Log.d("MainAdapter", "--- page ${page}")
            users.clear()
        }
        users.addAll(list)
    }
}