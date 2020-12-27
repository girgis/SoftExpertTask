package com.example.githubproject.data.api;

import com.example.githubproject.data.model.CarsModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class ApiHelperImpl implements ApiHelper{

    private ApiService apiService;

    @Inject
    public ApiHelperImpl(ApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public Observable<Response<CarsModel>> getUsers(int page) {
        return apiService.getUsers(page);
    }
}
