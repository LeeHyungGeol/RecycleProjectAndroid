package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CommunityDetailActivity extends AppCompatActivity {
    private Button sendNoteBtn;
    private TextView detailTitle;
    private TextView detailContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        init();
        addListener();
        setActionBar();
    }

    private void addListener() {
        sendNoteBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CommunityDetailActivity.this,PopUpSendNoteActivity.class);
                startActivity(intent);

            }
        });
    }

    private void init() {
        sendNoteBtn = findViewById(R.id.sendNoteBtn);
        detailContext = findViewById(R.id.detailContext);
        detailTitle = findViewById(R.id.detailTitle);
    }


    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }


}
