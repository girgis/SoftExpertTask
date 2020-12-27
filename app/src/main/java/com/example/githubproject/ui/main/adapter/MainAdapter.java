package com.example.githubproject.ui.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;import com.bumptech.glide.Glide;
import com.example.githubproject.R;
import com.example.githubproject.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.DataViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<User> users;

    public MainAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public MainAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_layout, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.DataViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserName;
        private TextView textViewUserEmail;
        private TextView textIsUsed;
        private ImageView imageViewAvatar;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
            textIsUsed = itemView.findViewById(R.id.textIsUsed);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }

        void bind(User user) {
            textViewUserName.setText(user.getBrand());
            textViewUserEmail.setText(user.getConstractionYear());
            textIsUsed.setText("is used: " + user.isUsed());
            Glide.with(context)
                    .load(user.getImageUrl())
                    .into(imageViewAvatar);
        }
    }

    public void addData(List<User> list, int page) {
        Log.d("MainAdapter", "--- ++page ${page}");
        if (page == 1) {
            Log.d("MainAdapter", "--- page ${page}");
            users.clear();
        }
        users.addAll(list);
    }

}
