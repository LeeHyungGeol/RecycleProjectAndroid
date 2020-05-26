package com.example.wasterecycleproject;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wasterecycleproject.adapter.MyPageListAdapter;


public class MyPageFragment extends Fragment {

    private ListView listView;
    private MyPageListAdapter adapter;
    private String[] title = {"나의 쪽지 보관함","나의 게시글 목록","위치설정 하기", "로그아웃"};
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_my_page, container, false);
        init();
        return view;
    }
    private void init(){
        adapter = new MyPageListAdapter();
        listView = view.findViewById(R.id.myPageListView);
        listView.setAdapter(adapter);

        for(int i=0 ;i<title.length;i++){
            adapter.addPageList(title[i]);
        }
    }
}
