package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;

public class PopupProperRecycleActivity extends Activity { //RecycleFragment 에 있는 도움말 버튼 클릭시 나타나는 팝업

    private TextView popUpText;
    private TextView popUpTitle;

    private String title;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);         //타이틀바 없애기
        setContentView(R.layout.activity_popup_proper_recycle);
        init();

        popUpText.setText(data);
        popUpTitle.setText(title);
    }

    private void init() {
        popUpText = findViewById(R.id.properRecycleText);
        popUpTitle = findViewById(R.id.popUpTitle);
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        title = intent.getStringExtra("title");

    }

    //확인 버튼 클릭
    public void mOnClose(View v){

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity) AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

}
