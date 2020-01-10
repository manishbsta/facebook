package com.example.facebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.facebook.api.PostApi;
import com.example.facebook.api.UserApi;
import com.example.facebook.model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    BottomNavigationView home_navigation;
    Button imageupload,post;
    ImageView choosenimage;
    EditText statuspost;
    Uri uri;
    PostApi postApi;
    LinearLayout statusbar;
    MultipartBody.Part image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageupload = findViewById(R.id.image_upload);
        post=findViewById(R.id.post);
        choosenimage = findViewById(R.id.image_choose);
        statuspost = findViewById(R.id.statustext);
        home_navigation = findViewById(R.id.homeNavigation);
        statusbar = findViewById(R.id.status);
        DashboardFragment dashboardFragment = new DashboardFragment();
        setFragment(dashboardFragment);

        imageupload.setOnClickListener(this);
        post.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 ){
            uri = data.getData();
            choosenimage.setImageURI(uri);
            askPermission();
        }
    }

    private void getInstance() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3030/")
                .addConverterFactory(GsonConverterFactory.create()).build();

         postApi = retrofit.create(PostApi.class);

    }

    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {
            getImgReady();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getImgReady();
            }
            else {
                Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  void stricMode(){
        android.os.StrictMode.ThreadPolicy policy = new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);
    }

    private void getImgReady(){
        String [] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn,null,null,null);
        assert cursor !=null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        System.out.println(imgPath);
        File file = new File(imgPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        image = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
    }

    private void uploadImage (MultipartBody.Part image){
        getInstance();

        RequestBody postcaption = RequestBody.create(MediaType.parse("text/plain"), statuspost.getText().toString());
        SharedPreferences savedata = MainActivity.this.getSharedPreferences("User", Context.MODE_PRIVATE);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),savedata.getString("Name",""));

        Call<Void> flagUpload = postApi.createpost(image,postcaption,name);
        System.out.println(image.toString());

        flagUpload.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_upload:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.
                                Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                statusbar.setVisibility(View.VISIBLE);
                break;
            case R.id.post:
                uploadImage(image);
                statusbar.setVisibility(View.GONE);
                reload();
                break;
        }
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
