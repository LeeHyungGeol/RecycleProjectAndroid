package com.example.wasterecycleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.example.wasterecycleproject.adapter.UserCommunityListAdapter;

import java.util.ArrayList;

public class UserCommunityActivity extends AppCompatActivity { //마이페이지에서 게시글 목록 버튼 클릭시 나타나는 특정 유저의 게시글 목록

    private RecyclerView recyclerView;
    private UserCommunityListAdapter userCommunityListAdapter;
    private boolean isLoading;
    private ArrayList<String> titleList;
    private ArrayList<String> title;
    private ArrayList<String> dateList;
    private ArrayList<String> date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_community);
        setActionBar();
        init();
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }



    private void init() {
        titleList = new ArrayList<>();
        title = new ArrayList<>();
        dateList = new ArrayList<>();
        date = new ArrayList<>();
        isLoading = false;
        recyclerView = findViewById(R.id.userCommunityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firstData();
        initAdapter();
        initScrollListener();
    }


    private void firstData() {
        // 총 아이템이 50개라 가정, 서버에서 특정 유저의 전체 커뮤니티 리스트 받아와서 제목과 날짜를 넣어주면 됨
        for (int a=0; a<50; a++) {

            titleList.add("게시글 제목 " + a);
            dateList.add("게시글 날짜"+a);
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
        userCommunityListAdapter.notifyItemInserted(title.size() -1 );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                title.remove(title.size() -1 );
                date.remove(date.size() -1 );
                int scrollPosition = title.size();
                userCommunityListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                for (int i=currentSize; i<nextLimit; i++) {
                    if (i == titleList.size()) {
                        return;
                    }
                    title.add(titleList.get(i));
                    date.add(dateList.get(i));
                }

                userCommunityListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }


    private void initAdapter() {
        userCommunityListAdapter = new UserCommunityListAdapter(title,date);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == title.size() - 1) {
                        dataMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
}
