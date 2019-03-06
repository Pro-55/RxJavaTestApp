package com.example.rxjavatestapp.network.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("users")
    @Expose
    private ArrayList<User> users = null;

    public Integer getCount() {
        return count;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
