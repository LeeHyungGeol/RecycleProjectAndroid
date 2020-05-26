package com.example.wasterecycleproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wasterecycleproject.adapter.RecycleCommunityListAdapter;

import java.util.ArrayList;


public class CommunityFragment extends Fragment { //게시글 리스트 화면

    private Button upLoadBtn;
    private View view;
    private RecyclerView recyclerView;
    private RecycleCommunityListAdapter recycleCommunityListAdapter;
    private boolean isLoading;
    private ArrayList<String> titleList;
    private ArrayList<String> title;
    private ArrayList<String> dateList;
    private ArrayList<String> date;


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
        // 총 아이템이 50개라 가정, 서버에서 커뮤니티 모든 유저 전체 리스트 받아와서 제목과 날짜를 넣어주면 됨
        for (int a=0; a<50; a++) {

            titleList.add("title " + a);
            dateList.add("date"+a);
        }

        // 총 아이템에서 10개를 받아옴
        for (int i=0; i<10; i++) {
            title.add(titleList.get(i));
            date.add(dateList.get(i));
        }
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
