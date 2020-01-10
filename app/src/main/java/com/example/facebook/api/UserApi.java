package com.example.facebook.api;

import com.example.facebook.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("register")
    Call<Void> register(@Body User usr);

    @POST("login")
    Call<User> userLogin(@Body User user);
}
