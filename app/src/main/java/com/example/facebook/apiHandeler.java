package com.example.facebook;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiHandeler {
    private static final  String IMG_URL = "http://10.0.2.2:3030/image";
    private static final  String BASE_URL = "http://10.0.2.2:3030/";
    public static final  String Token = "Bearer ";

    public static Retrofit getInstance() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}
