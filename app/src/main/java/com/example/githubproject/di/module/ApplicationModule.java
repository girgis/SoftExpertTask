package com.example.githubproject.di.module;


import com.example.githubproject.BuildConfig;
import com.example.githubproject.data.api.ApiHelper;
import com.example.githubproject.data.api.ApiHelperImpl;
import com.example.githubproject.data.api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class ApplicationModule {

    @Provides
    public String provideBaseUrl(){
        return BuildConfig.BASE_URL;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(){
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
        }else {
            return new OkHttpClient.Builder().build();
        }
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, String BASE_URL){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(
                                Schedulers.io()))
                .build();
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public ApiHelper provideApiHelper(ApiHelperImpl apiHelper){
        return apiHelper;
    }

}
