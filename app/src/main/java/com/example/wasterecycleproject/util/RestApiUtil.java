package com.example.wasterecycleproject.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiUtil {
    private RestApi mGetApi;

    public RestApiUtil() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(RestApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGetApi = mRetrofit.create(RestApi.class);
    }

    public RestApi getApi() {
        return mGetApi;
    }
}
