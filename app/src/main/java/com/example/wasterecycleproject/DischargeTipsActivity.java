package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DischargeTipsActivity extends AppCompatActivity { //배출 요령 화면

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discharge_tips);
        setActionBar();
    }


    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }
}
