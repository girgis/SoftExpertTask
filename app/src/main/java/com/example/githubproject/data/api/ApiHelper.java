package com.example.githubproject.data.api;

import com.example.githubproject.data.model.CarsModel;

import io.reactivex.Observable;
import retrofit2.Response;

public interface ApiHelper {
    Observable<Response<CarsModel>> getUsers(int page);
}
