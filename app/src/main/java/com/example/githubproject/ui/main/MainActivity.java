package com.example.githubproject.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.githubproject.R;
import com.example.githubproject.data.model.User;
import com.example.githubproject.ui.main.adapter.MainAdapter;
import com.example.githubproject.ui.main.viewmodel.MainViewModel;
import com.example.githubproject.utils.Status;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private MainAdapter adapter;
    private int page = 1;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeToRefresh;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        setupObserver();
    }

    private void setupUI() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        progressBar = findViewById(R.id.progressBar);
        adapter = new MainAdapter(this, new ArrayList<User>());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page += 1;
                    mainViewModel.fetchUsers(page);
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mainViewModel.fetchUsers(page);
            }
        });
    }

    private void setupObserver() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUsers().observe(this, carsModelResource -> {
            switch (carsModelResource.getStatus()) {
                case SUCCESS: {
                    swipeToRefresh.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    if (carsModelResource.getData() != null){
                        if (carsModelResource.getData().getData() !=null && !carsModelResource.getData().getData().isEmpty()){
                            renderList(carsModelResource.getData().getData());
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    break;
                }
                case LOADING: {
                    swipeToRefresh.setRefreshing(false);
                    if (page == 1) {
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    break;
                }
                case ERROR: {
                    swipeToRefresh.setRefreshing(false);
                    //Handle Error
                    progressBar.setVisibility(View.GONE);
                    if (carsModelResource.getStatus() != null) {
                        Toast.makeText(this, carsModelResource.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        });
    }

    private void renderList(List<User> users) {
        adapter.addData(users, page);
        adapter.notifyDataSetChanged();
    }

}
