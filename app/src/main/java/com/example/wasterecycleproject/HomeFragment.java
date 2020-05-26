package com.example.wasterecycleproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.example.wasterecycleproject.adapter.RecycleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;


public class HomeFragment extends Fragment {

    private ExpandableListView expandableListView;
    private List<String> listGroup;
    private HashMap<String,List<String>> listItem;
    private RecycleExpandableListAdapter recycleExpandableListAdapter;
    private View view;
    private ImageButton voiceBtn;
    private ImageButton searchBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home,container,false);
        init();
        initListData();
        initTableData();
        addListener();
        return view;
    }

    private void init(){
        expandableListView = view.findViewById(R.id.expandableListView);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        recycleExpandableListAdapter = new RecycleExpandableListAdapter(getActivity(),listGroup,listItem);
        expandableListView.setAdapter(recycleExpandableListAdapter);
        voiceBtn = view.findViewById(R.id.voiceBtn);
        searchBtn = view.findViewById(R.id.searchBtn);
    }

    private void addListener() { //리스너 추가

        voiceBtn.setOnClickListener(new Button.OnClickListener(){ //음성 버튼 리스너

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),DischargeTipsActivity.class);
                startActivity(intent);

            }
        });

        searchBtn.setOnClickListener(new Button.OnClickListener(){ //검색 버튼 리스너

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),DischargeTipsActivity.class);
                startActivity(intent);

            }
        });



    }

    private void initTableData() { //테이블에 들어갈 데이터 넣기(findViewById 해서 setText 해주면 될듯

    }

    private void initListData() { //확장되는 리스트 위젯에 데이터 넣기
        listGroup.add("생활쓰레기");
        listGroup.add("음식물쓰레기");
        listGroup.add("재활용품");
        String[] array;

        List<String> houseWasteList = new ArrayList<>();
        array= new String[]{"배출요일: ", "배출시작시각: ", "배출종료시각: "}; //생활쓰레기 리스트를 누르면 나오는 내용
        for(String item: array){
            houseWasteList.add(item);
        }

        List<String> foodWasteList = new ArrayList<>();
        array= new String[]{"배출요일: ", "배출시작시각: ", "배출종료시각: "}; //음식물쓰레기 리스트를 누르면 나오는 내용
        for(String item: array){
            foodWasteList.add(item);
        }
        List<String> recylceWasteList = new ArrayList<>();
        array= new String[]{"배출요일: ", "배출시작시각: ", "배출종료시각: "}; //재활용품 리스트를 누르면 나오는 내용
        for(String item: array){
            recylceWasteList.add(item);
        }

        listItem.put(listGroup.get(0),houseWasteList);
        listItem.put(listGroup.get(1),foodWasteList);
        listItem.put(listGroup.get(2),recylceWasteList);

        recycleExpandableListAdapter.notifyDataSetChanged();
    }
}
