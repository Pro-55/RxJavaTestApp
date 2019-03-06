package com.example.rxjavatestapp.network.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSearchResponce {
    @SerializedName("op_status")
    @Expose
    private String opStatus;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(String opStatus) {
        this.opStatus = opStatus;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
