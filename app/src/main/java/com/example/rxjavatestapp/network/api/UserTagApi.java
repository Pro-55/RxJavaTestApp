package com.example.rxjavatestapp.network.api;

import com.example.rxjavatestapp.network.responce.UserSearchResponce;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface UserTagApi {

    @GET("/api/users")
    Observable<UserSearchResponce> userSearch(@Query("session_id") String sessionID,
                                              @Query("count") int count,
                                              @Query("username") String userName);
}
