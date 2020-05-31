package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PopupHelpActivity extends Activity { //RecycleFragment 에 있는 도움말 버튼 클릭시 나타나는 팝업
    private Button imageDownloadBtn;
    private final String DOWNLOAD_FILE="https://bit.ly/2yNTkk5"; //마커 다운로드 주소
    private Uri downloadUri = Uri.parse(DOWNLOAD_FILE);
    private DownloadManager downloadManager;
    private long id =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_help);
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        init();
        addListener();
    }

    public void init() {
        imageDownloadBtn = findViewById(R.id.imageDownladBtn);
    }

    public void addListener(){
        imageDownloadBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });
    }

    public void downloadImage(){
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setTitle("길이 측정을 위한 이미지");
        request.setDescription("가구에 부착");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        id = downloadManager.enqueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter =
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);
            if(cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);

//                if(status == DownloadManager.STATUS_SUCCESSFUL){
//                    try{
//                        ParcelFileDescriptor file = downloadManager.openDownloadedFile(id);
//                        FileInputStream fileInputStream =
//                                new ParcelFileDescriptor.AutoCloseInputStream(file);
//
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    };


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

}
