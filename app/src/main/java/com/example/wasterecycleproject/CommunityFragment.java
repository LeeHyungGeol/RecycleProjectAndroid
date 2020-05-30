package com.example.wasterecycleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wasterecycleproject.adapter.RecycleCommunityListAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.AllCommunityResponseDTO;
import com.example.wasterecycleproject.model.Community;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommunityFragment extends Fragment { //게시글 리스트 화면


    private RestApiUtil mRestApiUtil;
    private Button upLoadBtn;
    private View view;
    private RecyclerView recyclerView;
    private RecycleCommunityListAdapter recycleCommunityListAdapter;
    private boolean isLoading;
    private ArrayList<Community> communityList;
    private ArrayList<Community> community;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_community, container, false);
        AppManager.getInstance().setContext(getActivity());
        AppManager.getInstance().setResources(getResources());
        init();
        addListener();
        return view;
    }

    private void init(){
        upLoadBtn = view.findViewById(R.id.upLoadBtn);
        communityList = new ArrayList<>();
        community = new ArrayList<>();
        isLoading = false;
        recyclerView = view.findViewById(R.id.allCommunityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestApiUtil = new RestApiUtil();
        firstData();
        initAdapter();
        initScrollListener();
    }

    private void addListener() {
        upLoadBtn.setOnClickListener(new Button.OnClickListener(){ //음성 버튼 리스너

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RegisterBoardActivity.class);
                startActivity(intent);

            }
        });
    }

    private void firstData() {
        progressON("로딩중입니다");

        mRestApiUtil.getApi().all_community("Token " + UserToken.getToken()).enqueue(new Callback<AllCommunityResponseDTO>() {
            @Override
            public void onResponse(Call<AllCommunityResponseDTO> call, Response<AllCommunityResponseDTO> response) {
                if(response.isSuccessful()){
                    recycleCommunityListAdapter.notifyDataSetChanged();
                    AllCommunityResponseDTO allCommunityResponseDTO = response.body();
                    final int communitysize = allCommunityResponseDTO.getCommunity_list().size();
                    for(int index=0; index<communitysize;index++){
                        communityList.add(allCommunityResponseDTO.getCommunity_list().get(index));
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
                    Log.d("CommunityFragment","response 실패");
                }

            }

            @Override
            public void onFailure(Call<AllCommunityResponseDTO> call, Throwable t) {
                Log.d("CommunityFragment","통신 실패");

            }
        });

    }

    private void dataMore() {
        community.add(null);
        recycleCommunityListAdapter.notifyItemInserted(community.size() -1 );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                community.remove(community.size() -1 );
                int scrollPosition = community.size();
                recycleCommunityListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                for (int i=currentSize; i<nextLimit; i++) {
                    if (i == communityList.size()) {
                        return;
                    }
                    community.add(communityList.get(i));
                }

                recycleCommunityListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }


    private void initAdapter() {
        recycleCommunityListAdapter = new RecycleCommunityListAdapter(community);
        recyclerView.setAdapter(recycleCommunityListAdapter);
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
