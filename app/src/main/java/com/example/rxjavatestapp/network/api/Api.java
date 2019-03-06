package com.example.rxjavatestapp.network.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static final String BASEURL = "https://api.theshareapp.co";
    private static OkHttpClient.Builder mHttpClient = null;
    private static Retrofit mRetrofit = null;

    private Api() {
    }

    private static Retrofit getRetrofit() {
        if (mHttpClient == null) {
            mHttpClient = new OkHttpClient.Builder();
        }

        mHttpClient.connectTimeout(5, TimeUnit.MINUTES);
        mHttpClient.writeTimeout(5, TimeUnit.MINUTES);
        mHttpClient.readTimeout(5, TimeUnit.MINUTES);


        OkHttpClient client = mHttpClient.build();


        Retrofit.Builder mBuilder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        if (mRetrofit == null) {
            mRetrofit = mBuilder.client(client).build();
        }

        return mRetrofit;
    }


    public static UserTagApi userTagApi() {
        return getRetrofit().create(UserTagApi.class);
    }
}
