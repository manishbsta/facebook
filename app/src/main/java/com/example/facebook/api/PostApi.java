package com.example.facebook.api;

import com.example.facebook.model.Post;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostApi {
    @GET("findpost")
    Call<List<Post>> getPost(@Header("Authorization") String auth);


    @Multipart //for image
    @POST("createpost")
    Call<Void> createpost(@Part MultipartBody.Part img, @Part("status") RequestBody status,@Part("name") RequestBody name); //image file data type MultipartBody




}
