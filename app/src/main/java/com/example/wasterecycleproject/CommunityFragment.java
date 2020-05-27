package com.example.wasterecycleproject;

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
import com.example.wasterecycleproject.model.AllCommunityResponseDTO;
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
    private ArrayList<String> titleList;
    private ArrayList<String> title;
    private ArrayList<String> dateList;
    private ArrayList<String> date;
    private int communitysize;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_community, container, false);
        init();
        addListener();
        return view;
    }

    private void init(){
        upLoadBtn = view.findViewById(R.id.upLoadBtn);
        titleList = new ArrayList<>();
        title = new ArrayList<>();
        dateList = new ArrayList<>();
        date = new ArrayList<>();
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

        mRestApiUtil.getApi().all_community("Token " + UserToken.getToken()).enqueue(new Callback<AllCommunityResponseDTO>() {
            @Override
            public void onResponse(Call<AllCommunityResponseDTO> call, Response<AllCommunityResponseDTO> response) {
                if(response.isSuccessful()){
                    AllCommunityResponseDTO allCommunityResponseDTO = response.body();
                    communitysize = allCommunityResponseDTO.getCommunity_list().size();
                    Log.d("커뮤니티 사이즈", String.valueOf(communitysize));
                    for(int index=0; index<communitysize;index++){
                        titleList.add(allCommunityResponseDTO.getCommunity_list().get(index).getTitle());
                        dateList.add(allCommunityResponseDTO.getCommunity_list().get(index).getDate());
                    }
                    if(communitysize<10){
                        for(int i=0;i<communitysize;i++){
                            title.add(titleList.get(i));
                            date.add(dateList.get(i));
                        }

                    }
                    else{
                        for(int i=0; i<10; i++){
                            title.add(titleList.get(i));
                            date.add(dateList.get(i));
                        }
                    }

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
        title.add(null);
        date.add(null);
        recycleCommunityListAdapter.notifyItemInserted(title.size() -1 );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                title.remove(title.size() -1 );
                date.remove(date.size() -1 );
                int scrollPosition = title.size();
                recycleCommunityListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                for (int i=currentSize; i<nextLimit; i++) {
                    if (i == titleList.size()) {
                        return;
                    }
                    title.add(titleList.get(i));
                    date.add(dateList.get(i));
                }

                recycleCommunityListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }


    private void initAdapter() {
        recycleCommunityListAdapter = new RecycleCommunityListAdapter(title,date);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == title.size() - 1) {
                        dataMore();
                        isLoading = true;
                    }
                }
            }
        });
    }







}
