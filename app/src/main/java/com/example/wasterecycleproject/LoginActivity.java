package com.example.wasterecycleproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasterecycleproject.model.LoginDTO;
import com.example.wasterecycleproject.model.LoginResponseDTO;
import com.example.wasterecycleproject.model.RegisterResponseDTO;
import com.example.wasterecycleproject.model.User;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private RestApiUtil mRestApiUtil;
    private EditText idText;
    private EditText pwText;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        addListener();
    }

    private void init() {
        idText = findViewById(R.id.idText);
        pwText = findViewById(R.id.pwText);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
    }

    private void addListener() {
        loginBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterUserActivity.class);
                startActivity(intent);

            }
        });
    }

    private void loginUser(){
        LoginDTO loginDTO = new LoginDTO();
        String id = idText.getText().toString();
        String pw= pwText.getText().toString();
        loginDTO.setUsername(id);
        loginDTO.setPassword(pw);
        mRestApiUtil = new RestApiUtil();
        mRestApiUtil.getApi().login(loginDTO).enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {

                if(response.isSuccessful()) {
                    LoginResponseDTO loginResponseDTO = response.body();
                    Log.d("토큰",loginResponseDTO.getToken());
                    UserToken.setToken(loginResponseDTO.getToken());
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"아이디 비밀번호가 맞지않습니다",Toast.LENGTH_SHORT).show();
                    Log.d("아이디 비밀번호가 맞지않습니다","실패");
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                Log.d("통신","실패");
            }
        });



    }

}
