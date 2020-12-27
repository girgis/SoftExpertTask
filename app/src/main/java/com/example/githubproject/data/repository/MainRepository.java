package com.example.githubproject.data.repository;

import com.example.githubproject.data.api.ApiHelper;

import javax.inject.Inject;

public class MainRepository {

    private ApiHelper apiHelper;

    @Inject
    public MainRepository(ApiHelper apiHelper){
        this.apiHelper = apiHelper;
    }

    public io.reactivex.Observable<retrofit2.Response<com.example.githubproject.data.model.CarsModel>> getUsers(int page){
        return apiHelper.getUsers(page);
    }

}
