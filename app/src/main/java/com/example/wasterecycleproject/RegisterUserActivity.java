package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasterecycleproject.model.RegisterResponseDTO;
import com.example.wasterecycleproject.model.User;
import com.example.wasterecycleproject.util.RestApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends Activity { //회원가입 창
    private EditText idText;
    private EditText pwText;
    private EditText pwText2;
    private Button registerBtn;
    private RestApiUtil mRestApiUtil;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_user);
        init();
        addListener();
    }

    private void addListener() {
        registerBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(pwText.getText().toString().equals(pwText2.getText().toString())){
                    registerUser();
                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        idText = findViewById(R.id.registerIdText);
        pwText = findViewById(R.id.registerPw);
        pwText2 = findViewById(R.id.registerPw2);
        registerBtn = findViewById(R.id.sendRegsterBtn);
    }

    private void registerUser(){
        mUser = new User();
        String id = idText.getText().toString();
        String pw= pwText.getText().toString();
        mUser.setUser_id(id);
        mUser.setPassword(pw);
        mRestApiUtil = new RestApiUtil();
        mRestApiUtil.getApi().register(mUser).enqueue(new Callback<RegisterResponseDTO>() {
            @Override
            public void onResponse(Call<RegisterResponseDTO> call, Response<RegisterResponseDTO> response) {
                System.out.println(response.body());
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"동일한 아이디가 존재합니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponseDTO> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"통신 실패",Toast.LENGTH_SHORT).show();

            }
        });

    }


    //취소 버튼 클릭
    public void mOnClose(View v) {
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}