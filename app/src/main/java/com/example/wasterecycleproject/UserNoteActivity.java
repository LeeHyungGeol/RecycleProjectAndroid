package com.example.wasterecycleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.example.wasterecycleproject.adapter.UserNoteListAdapter;
import com.example.wasterecycleproject.model.AllCommunityResponseDTO;
import com.example.wasterecycleproject.model.AllNoteResponseDTO;
import com.example.wasterecycleproject.model.Message;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNoteActivity extends AppCompatActivity { //마이페이지에서 쪽지 목록 버튼 클릭시 나타나는 특정 유저의 쪽지 목록

    private RestApiUtil mRestApiUtil;
    private RecyclerView recyclerView;
    private UserNoteListAdapter userNoteListAdapter;
    private boolean isLoading;
    private ArrayList<Message> message;
    private ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_note);
        setActionBar();
        init();
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }



    private void init() {
        message = new ArrayList<>();
        messageList = new ArrayList<>();
        isLoading = false;
        recyclerView = findViewById(R.id.userNoteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestApiUtil = new RestApiUtil();
        firstData();
        initAdapter();
        initScrollListener();
    }


    private void firstData() {
        mRestApiUtil.getApi().all_note("Token " + UserToken.getToken()).enqueue(new Callback<AllNoteResponseDTO>() {
            @Override
            public void onResponse(Call<AllNoteResponseDTO> call, Response<AllNoteResponseDTO> response) {
                if(response.isSuccessful())
                {
                    userNoteListAdapter.notifyDataSetChanged();
                    AllNoteResponseDTO allNoteResponseDTO = response.body();
                    int noteSize = allNoteResponseDTO.getMessage_list().getRecv_message().size();

                    for(int index=0; index<noteSize;index++){
                        messageList.add(allNoteResponseDTO.getMessage_list().getRecv_message().get(index));
                    }
                    noteSize = allNoteResponseDTO.getMessage_list().getSend_message().size();
                    for(int index=0; index<noteSize;index++){
                        messageList.add(allNoteResponseDTO.getMessage_list().getSend_message().get(index));
                    }

                    if(messageList.size()<10){
                        for(int i=0;i<messageList.size();i++){
                            message.add(messageList.get(i));
                        }
                    }
                    else {
                        for(int i=0;i<10;i++){
                            message.add(messageList.get(i));
                        }
                    }

                }
                else{

                    Log.d("UserNoteActivity","response 실패");

                }
            }

            @Override
            public void onFailure(Call<AllNoteResponseDTO> call, Throwable t) {
                Log.d("UserNoteActivity","통신 실패");

            }
        });

    }

    private void dataMore() {
        message.add(null);
        userNoteListAdapter.notifyItemInserted(message.size() -1 );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                message.remove(message.size() -1 );
                int scrollPosition = message.size();
                userNoteListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                for (int i=currentSize; i<nextLimit; i++) {
                    if (i == messageList.size()) {
                        return;
                    }
                    message.add(messageList.get(i));
                }

                userNoteListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }


    private void initAdapter() {
        userNoteListAdapter = new UserNoteListAdapter(message);
        recyclerView.setAdapter(userNoteListAdapter);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == message.size() - 1) {
                        dataMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
}
