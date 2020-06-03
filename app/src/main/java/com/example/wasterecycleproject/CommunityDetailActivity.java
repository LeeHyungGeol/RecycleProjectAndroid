package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.CommunityDetailResponseDTO;
import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.util.RestApi;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wasterecycleproject.util.RestApi.BASE_URL;

public class CommunityDetailActivity extends AppCompatActivity {

    private Button sendNoteBtn;
    private TextView detailID;
    private ImageView detailImage;
    private RestApiUtil mRestApiUtil;
    private int communityidx;
    private TextView titleText;
    private TextView contextText;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_community_detail);
        init();
        addListener();
        setActionBar();
        showDetail();
    }


    private void addListener() {
        sendNoteBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CommunityDetailActivity.this,PopUpSendNoteActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);

            }
        });
    }

    private void init() {
        sendNoteBtn = findViewById(R.id.sendNoteBtn);
        detailID = findViewById(R.id.detailID);
        detailImage = findViewById(R.id.detailImage);
        mRestApiUtil = new RestApiUtil();
        Intent intent = getIntent();
        communityidx = intent.getIntExtra("position",1);
        titleText = findViewById(R.id.detailTitle);
        contextText = findViewById(R.id.detailContext);
    }


    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }


    private void showDetail() {
        progressON("게시글을 불러오는 중입니다");

        mRestApiUtil.getApi().detail_community("Token " + UserToken.getToken(),communityidx).enqueue(new Callback<CommunityDetailResponseDTO>() {
            @Override
            public void onResponse(Call<CommunityDetailResponseDTO> call, Response<CommunityDetailResponseDTO> response) {
                if(response.isSuccessful()){

                    CommunityDetailResponseDTO communityDetailResponseDTO = response.body();
                    titleText.setText(communityDetailResponseDTO.getCommunity().getTitle());
                    contextText.setText(communityDetailResponseDTO.getCommunity().getContent());
                    user_id=communityDetailResponseDTO.getCommunity().getUser_id();
                    detailID.setText(user_id);
                    Glide.with(CommunityDetailActivity.this)
                            .load("http://de4087a5d582.ngrok.io"+communityDetailResponseDTO.getCommunity().getImage())
                            .into(detailImage);


                    String url = ImageManager.getInstance().getFullImageString(communityDetailResponseDTO.getCommunity().getImage());
                    ImageManager.getInstance().GlideWithContext(AppManager.getInstance().getContext(), detailImage, url);

                    Log.d("CommmunityDetail","response 성공");

                    progressOFF();
                }
                else{
                    Log.d("CommmunityDetail","response 실패");
                }
            }

            @Override
            public void onFailure(Call<CommunityDetailResponseDTO> call, Throwable t) {
                Log.d("CommmunityDetail","통신 실패");
            }
        });
    }

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }


}
