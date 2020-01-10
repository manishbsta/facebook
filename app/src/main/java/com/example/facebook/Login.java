package com.example.facebook;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.facebook.api.UserApi;
import com.example.facebook.model.User;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText email, password;
    Button login,signup;
    SharedPreferences sharedPreferences;
    Retrofit retrofit;
    UserApi userApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password= findViewById(R.id.password);
        login = findViewById(R.id.login_btn);
        signup = findViewById(R.id.btn_signup);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.btn_signup:
                intent = new Intent(this, Registration.class );
                startActivity(intent);
                break;
            case R.id.login_btn:
                getInstance();
                login();
                break;
        }
    }

    private void getInstance() {
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3030/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        userApi = retrofit.create(UserApi.class);

    }

    private void login(){
        User usr = new User(email.getText().toString(),password.getText().toString());
        Call<User> loginCall = userApi.userLogin(usr);

        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Token",response.body().getToken());
                    editor.putString("Name",response.body().getName());

                    editor.commit();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(Login.this, "incorrect crenditial", Toast.LENGTH_SHORT).show();
                    email.setError("incorrect email or password");
                    password.setError("incorrect email or password");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });



    }
}
