package com.example.githubproject.ui.main.viewmodel;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubproject.data.model.CarsModel;
import com.example.githubproject.data.repository.MainRepository;
import com.example.githubproject.utils.NetworkHelper;
import com.example.githubproject.utils.Resource;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Resource<CarsModel>> _users;
    private NetworkHelper networkHelper;
    private MainRepository mainRepository;

    @ViewModelInject
    public MainViewModel(NetworkHelper networkHelper, MainRepository mainRepository){
        this.networkHelper = networkHelper;
        this.mainRepository = mainRepository;
    }

    public LiveData<Resource<CarsModel>> getUsers(){
        if (_users == null){
            _users = new MutableLiveData<Resource<CarsModel>>();
            fetchUsers(1);
        }
        return _users;
    }

    public void fetchUsers(int page){
        _users.postValue(Resource.Companion.loading(null));
        Log.d("zzzz", "--- page: " + page);
        if (networkHelper.isNetworkConnected()) {
            mainRepository.getUsers(page).subscribeOn(Schedulers.io())
                    .subscribe(carsModelResponse -> {
                        if (carsModelResponse != null){
                            if (carsModelResponse.isSuccessful()) {
                                _users.postValue(Resource.Companion.success(carsModelResponse.body()));
                            } else _users.postValue(Resource.Companion.error(carsModelResponse.errorBody().toString(), null));
                        }
                    });
        } else _users.postValue(Resource.Companion.error("No internet connection", null));

    }

}
