package com.example.rxjavatestapp.network.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_favorited")
    @Expose
    private Boolean userFavorited;
    @SerializedName("profile_pic_url")
    @Expose
    private String profilePicUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getUserFavorited() {
        return userFavorited;
    }

    public void setUserFavorited(Boolean userFavorited) {
        this.userFavorited = userFavorited;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

}
