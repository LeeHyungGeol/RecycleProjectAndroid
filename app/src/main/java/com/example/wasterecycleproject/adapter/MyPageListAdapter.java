package com.example.wasterecycleproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wasterecycleproject.LoginActivity;
import com.example.wasterecycleproject.UserCommunityActivity;
import com.example.wasterecycleproject.UserNoteActivity;
import com.example.wasterecycleproject.R;
import com.example.wasterecycleproject.model.LogoutResponseDTO;
import com.example.wasterecycleproject.model.MyPageList;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageListAdapter extends BaseAdapter{

    private ArrayList<MyPageList> myPageList = new ArrayList<MyPageList>();
    private RestApiUtil mRestApiUtil;

    @Override
    public int getCount() {
        return myPageList.size();
    }

    @Override
    public Object getItem(int position) {
        return myPageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 이 부분에서 리스트뷰에 데이터를 넣어줌
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //position은 listview의 위치 , 첫번째면 0
        final int pos=position;
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypage_listview,parent,false);
        }

        TextView title = (TextView)convertView.findViewById(R.id.myPageTitle);
        MyPageList listviewitem = myPageList.get(position);
        title.setText(listviewitem.getTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos==0) //쪽지 보관함 클릭
                {
                    context.startActivity(new Intent(context, UserNoteActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                else if(pos==1){ //게시글 목록 클릭
                    context.startActivity(new Intent(context, UserCommunityActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                else if(pos==2){ //로그아웃 버튼 클릭
                    mRestApiUtil = new RestApiUtil();
                    mRestApiUtil.getApi().logout("Token " + UserToken.getToken()).enqueue(new Callback<LogoutResponseDTO>() {
                        @Override
                        public void onResponse(Call<LogoutResponseDTO> call, Response<LogoutResponseDTO> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(context,"로그아웃되었습니다",Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                            else{
                                Toast.makeText(context,"연결을 확인해주세요",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LogoutResponseDTO> call, Throwable t) {

                        }
                    });

                }

            }
        });

        return convertView;
    }

    public void addPageList(String title){
        MyPageList item = new MyPageList();
        item.setTitle(title);
        myPageList.add(item);
    }
}