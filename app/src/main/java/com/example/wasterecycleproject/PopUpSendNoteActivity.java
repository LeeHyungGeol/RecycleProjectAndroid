package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PopUpSendNoteActivity extends Activity {
    private Button sendNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up_send_note);
        init();
        addListener();
    }
    private void init() {
        sendNoteBtn = findViewById(R.id.sendNoteBtn);
    }

    private void addListener() { //쪽지 전송 확인 눌렀을때 리스너
        sendNoteBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }


    //취소 버튼 클릭
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
}
