package com.example.githubproject.data.model;

import java.util.ArrayList;

public class CarsModel {

    private String status;
    private ArrayList<User> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }
}
