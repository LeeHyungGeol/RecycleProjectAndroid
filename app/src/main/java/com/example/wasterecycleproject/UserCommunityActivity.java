package com.example.wasterecycleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.wasterecycleproject.adapter.RecycleCommunityListAdapter;
import com.example.wasterecycleproject.adapter.UserCommunityListAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.Community;
import com.example.wasterecycleproject.model.UserCommunityResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommunityActivity extends AppCompatActivity { //마이페이지에서 게시글 목록 버튼 클릭시 나타나는 특정 유저의 게시글 목록

    private RestApiUtil mRestApiUtil;
    private RecyclerView recyclerView;
    private UserCommunityListAdapter userCommunityListAdapter;
    private boolean isLoading;
    private ArrayList<Community> communityList;
    private ArrayList<Community> community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        setContentView(R.layout.activity_user_community);
        setActionBar();
        init();
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }



    private void init() {
        communityList = new ArrayList<>();
        community = new ArrayList<>();
        isLoading = false;
        recyclerView = findViewById(R.id.userCommunityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestApiUtil = new RestApiUtil();
        firstData();
        initAdapter();
        initScrollListener();
    }


    private void firstData() {
        progressON("로딩중입니다");
        String user_id = AppManager.getInstance().getUser().getUser_id();
        mRestApiUtil.getApi().user_community("Token " + UserToken.getToken(),user_id)
                .enqueue(new Callback<UserCommunityResponseDTO>() {
                    @Override
                    public void onResponse(Call<UserCommunityResponseDTO> call, Response<UserCommunityResponseDTO> response) {
                        if(response.isSuccessful()){
                            Log.d("유저 커뮤니티 통신성공","통신성공");
                            userCommunityListAdapter.notifyDataSetChanged();
                            UserCommunityResponseDTO userCommunityResponseDTO = response.body();
                            final int communitysize = userCommunityResponseDTO.getCommunity_list().size();
//                    Log.d("커뮤니티 사이즈", String.valueOf(communitysize));
                            for(int index=0; index<communitysize;index++){
//                        Log.d("타이틀",allCommunityResponseDTO.getCommunity_list().get(index).getTitle());
//                        Log.d("내용",allCommunityResponseDTO.getCommunity_list().get(index).getDate());
                                Log.d("아이디",userCommunityResponseDTO.getCommunity_list().get(index).getUser_id());
                                communityList.add(userCommunityResponseDTO.getCommunity_list().get(index));
                            }
                            if(communitysize<10){
                                for(int i=0;i<communitysize;i++){
                                    community.add(communityList.get(i));
                                }

                            }
                            else{
                                for(int i=0; i<10; i++){
                                    community.add(communityList.get(i));
                                }
                            }
                            progressOFF();
                        }
                        else{
                            Log.d("UserCommunityActivity","response 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserCommunityResponseDTO> call, Throwable t) {
                        Log.d("UserCommunityActivity","통신 실패");

                    }
                });

    }



    private void dataMore() {
        community.add(null);
        userCommunityListAdapter.notifyItemInserted(community.size() -1 );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                community.remove(community.size() -1 );
                int scrollPosition = community.size();
                userCommunityListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                for (int i=currentSize; i<nextLimit; i++) {
                    if (i == communityList.size()) {
                        return;
                    }
                    community.add(communityList.get(i));
                }

                userCommunityListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }


    private void initAdapter() {
        userCommunityListAdapter = new UserCommunityListAdapter(community);
        recyclerView.setAdapter(userCommunityListAdapter);
    }

    // 리싸이클러뷰 이벤트시
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == community.size() - 1) {
                        dataMore();
                        isLoading = true;
                    }
                }
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
