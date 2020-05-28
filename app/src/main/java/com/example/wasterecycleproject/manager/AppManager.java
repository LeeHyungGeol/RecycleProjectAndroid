package com.example.wasterecycleproject.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.wasterecycleproject.MainActivity;
//import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.model.LoginResponseDTO;
import com.example.wasterecycleproject.model.User;

import java.util.ArrayList;

public class AppManager {

    private static AppManager instance;
    private MainActivity MA;

    private long backKeyPressedTime = 0;

    private Context context;
    private Resources resources;

    private LoginResponseDTO mLoginResponseDTO;
    private User user;

    private AppManager() {
        user = new User();
        mLoginResponseDTO = new LoginResponseDTO();
    }


    public static AppManager getInstance() { //중요!! singleton pattern
        if (instance == null)
            return instance = new AppManager();
        return instance;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ((Activity)AppManager.getInstance().getContext()).finish();
        }
    }

    public void showGuide() {
        Toast.makeText(context, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }

    //객체를 저장하고 불러오는 메소드들을 생성.


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginResponseDTO getmLoginResponseDTO() {
        return mLoginResponseDTO;
    }

    public void setmLoginResponseDTO(LoginResponseDTO mLoginResponseDTO) {
        this.mLoginResponseDTO = mLoginResponseDTO;
    }

    public Context getContext() { return context;}
    public void setContext(Context context) { this.context = context; }

    public Resources getResources() { return resources; }
    public void setResources(Resources resources) { this.resources = resources; }


    public Bitmap getBitmap(int r) {
        return BitmapFactory.decodeResource(resources, r);
    }

    public void setMainActivity(MainActivity MA){this.MA = MA;}
    public MainActivity getMainActivity(){ return MA; }
}
