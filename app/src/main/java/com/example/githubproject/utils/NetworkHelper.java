package com.example.githubproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class NetworkHelper {

    private Context context;

    @Inject
    public NetworkHelper(@ApplicationContext Context context){
        this.context = context;
    }

    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
