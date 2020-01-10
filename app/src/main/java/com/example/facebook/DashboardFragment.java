package com.example.facebook;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.facebook.adaptor.PostAdaptor;
import com.example.facebook.api.PostApi;
import com.example.facebook.api.postapiheandel;
import com.example.facebook.model.Post;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    Retrofit retrofit;
    PostApi postApi;
    private RecyclerView postview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_dashboard, container, false);
        postview = root.findViewById(R.id.postlist);
        SharedPreferences savedata = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);

        getPost("Bearer " + savedata.getString("Token",""));
//        postapiheandel postapiheandel = new postapiheandel();
//        postapiheandel.getpost(savedata.getString("Token",""));
        return root;
    }

    private void getInstance() {
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3030/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        postApi = retrofit.create(PostApi.class);

    }

    private void getPost(String Token){
        getInstance();

        Call<List<Post>> postCall = postApi.getPost(Token);

        postCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> postlist = response.body();
                PostAdaptor adapter = new PostAdaptor(postlist,getContext());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                postview.setLayoutManager(layoutManager);
                postview.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

}
