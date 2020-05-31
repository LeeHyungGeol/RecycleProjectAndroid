package com.example.wasterecycleproject;

import android.app.Activity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wasterecycleproject.adapter.MyPageListAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.UserProfileResponse;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment {

    private ListView listView;
    private MyPageListAdapter adapter;
    private String[] title = {"나의 쪽지 보관함","나의 게시글 목록","로그아웃"};
    private View view;
    private RestApiUtil mRestApiUtil;
    private TextView idText;
    private TextView locationText;
    private TextView pointText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_my_page, container, false);
        AppManager.getInstance().setContext(getActivity());
        AppManager.getInstance().setResources(getResources());
        init();
        setMyPage();
        return view;
    }

    private void init(){
        adapter = new MyPageListAdapter();
        listView = view.findViewById(R.id.myPageListView);
        listView.setAdapter(adapter);

        for(int i=0 ;i<title.length;i++){
            adapter.addPageList(title[i]);
        }
        mRestApiUtil = new RestApiUtil();
        idText = view.findViewById(R.id.myNickNameText);
        locationText = view.findViewById(R.id.myLocationText);
        pointText = view.findViewById(R.id.myPointText);
    }

    private void setMyPage() {
        progressON("로딩중입니다");
        mRestApiUtil.getApi().user_profile("Token " + UserToken.getToken()).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful()){
                    UserProfileResponse userProfileResponse = response.body();
                    String id = userProfileResponse.getMy_page().getUser_id();
                    String location = userProfileResponse.getMy_page().getLocation_name();
                    int point = userProfileResponse.getMy_page().getPoint();
                    Log.d("id",id);
                    Log.d("location",location);
                    Log.d("point", String.valueOf(point));
                    try{
                        idText.setText("아이디: "+id);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        locationText.setText("위치: "+location);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        pointText.setText("포인트: "+point);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    progressOFF();

                }
                else{
                    Log.d("MyPageFragment","reponse 실패");

                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Log.d("MyPageFragment","통신 실패");

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
