package com.example.facebook.api;

import com.example.facebook.apiHandeler;
import com.example.facebook.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class postapiheandel {

    PostApi postApi = apiHandeler.getInstance().create(PostApi.class);
    List<Post> postdata;

    public List<Post> getpost(String tok){
        Call<List<Post>> postCall = postApi.getPost(apiHandeler.Token + tok);

        try{
            Response<List<Post>> gettpost = postCall.execute();
            postdata =  gettpost.body();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  postdata;
    }


}
