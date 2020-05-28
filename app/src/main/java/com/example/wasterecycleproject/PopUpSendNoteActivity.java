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
import android.widget.EditText;
import android.widget.Toast;

import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.model.SendNoteDTO;
import com.example.wasterecycleproject.model.SendNoteResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopUpSendNoteActivity extends Activity {
    private Intent intent;
    private Button sendNoteBtn;
    private String user_id;
    private RestApiUtil mRestApiUtil;
    private EditText sendNoteContent;

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
        intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        mRestApiUtil = new RestApiUtil();
        sendNoteContent = findViewById(R.id.sendNoteContext);
    }

    private void addListener() { //쪽지 전송 확인 눌렀을때 리스너
        sendNoteBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(user_id,AppManager.getInstance().getUser().getUser_id());
                if(user_id.equals(AppManager.getInstance().getUser().getUser_id())){
                    Toast.makeText(getApplicationContext(),"자기 자신한테 쪽지를 보낼 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    SendNoteDTO sendNoteDTO = new SendNoteDTO();
                    sendNoteDTO.setContent(sendNoteContent.getText().toString());
                    mRestApiUtil.getApi().send_note("Token " + UserToken.getToken(),sendNoteDTO,user_id).enqueue(new Callback<SendNoteResponseDTO>() {
                        @Override
                        public void onResponse(Call<SendNoteResponseDTO> call, Response<SendNoteResponseDTO> response) {
                            if(response.isSuccessful()){
                                SendNoteResponseDTO sendNoteResponseDTO = response.body();
                                Toast.makeText(getApplicationContext(),"쪽지 전송이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"쪽지 전송이 실패되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendNoteResponseDTO> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"자기 자신한테 쪽지를 보낼 수 없습니다.",Toast.LENGTH_SHORT).show();
                        }
                    });

                }


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
