package com.example.rxjavatestapp.network.ApiFunctions;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

public abstract class ApiFail implements Action1<Throwable> {
    @Override
    public void call(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException http = (HttpException) throwable;
            HttpErrorResponse response = new HttpErrorResponse(http.code(),http.response());
            response.setError(http.message());
            httpStatus(response);
        } else if (throwable instanceof IOException) {
            noNetworkError();
        } else {
            unknownError(throwable);
        }
    }

    public abstract void httpStatus(HttpErrorResponse response);

    public abstract void noNetworkError();

    public abstract void unknownError(Throwable throwable);
}
