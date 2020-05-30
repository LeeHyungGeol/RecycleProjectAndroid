package com.example.wasterecycleproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
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
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀바 없애기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        init();
        addListener();
        checkMyPermission();
    }

    private void checkMyPermission() {
        String temp = "";
        //파일 읽기 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.READ_EXTERNAL_STORAGE+" ";
        }
        //파일 쓰기 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.WRITE_EXTERNAL_STORAGE+" ";
        }
        //카메라 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.CAMERA+" ";
        }
        //음성 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.RECORD_AUDIO+" ";
        }
        //인터넷 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.INTERNET+" ";
        }
        //위치 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.ACCESS_FINE_LOCATION+" ";
        }
        //위치 권한 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            temp+= Manifest.permission.ACCESS_COARSE_LOCATION+" ";
        }



        if(!TextUtils.isEmpty(temp)){ //권한 요청
            ActivityCompat.requestPermissions(this,temp.trim().split(" "),1);
        }
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
        progressON("로그인 중입니다");
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
                    setUser(loginResponseDTO.getUser_id(), loginResponseDTO.getToken());
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    progressOFF();
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

    public void setUser(String user_id, String token) {
        AppManager.getInstance().getUser().setUser_id(user_id);
        AppManager.getInstance().getUser().setToken(token);

    }

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

}
