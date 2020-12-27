package com.example.githubproject.data.api;

import com.example.githubproject.data.model.CarsModel;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("cars")
    Observable<Response<CarsModel>> getUsers(@Query("page") int page);

}
