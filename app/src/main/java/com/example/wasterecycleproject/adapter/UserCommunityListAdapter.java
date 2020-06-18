package com.example.wasterecycleproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wasterecycleproject.CommunityDetailActivity;
import com.example.wasterecycleproject.CommunityFragment;
import com.example.wasterecycleproject.R;
import com.example.wasterecycleproject.RegisterBoardActivity;
import com.example.wasterecycleproject.model.Community;
import com.example.wasterecycleproject.model.DeleteResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommunityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Community> communities;
    private RestApiUtil mRestApiUtil = new RestApiUtil();


    public UserCommunityListAdapter(List<Community> communityList) {
        communities = communityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_community_list_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int position) { //클릭 이벤트 처리

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CommunityDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position",communities.get(position).getIdx());
                context.startActivity(intent);
            }
        });

        viewHolder.itemView.findViewById(R.id.deleteBtn).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                mRestApiUtil.getApi().delete_community("Token "+ UserToken.getToken(), communities.get(position).getIdx()).enqueue(new Callback<DeleteResponseDTO>() {
                    @Override
                    public void onResponse(Call<DeleteResponseDTO> call, Response<DeleteResponseDTO> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context,"삭제완료",Toast.LENGTH_SHORT).show();
                            ((Activity)context).finish();
                        }
                        else{
                            Log.d("response","실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteResponseDTO> call, Throwable t) {
                        Log.d("통신","실패");
                    }
                });

            }
        });

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return communities == null ? 0 : communities.size();
    }


    @Override
    public int getItemViewType(int position) {
        return communities.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView dateText;
        TextView idText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.communityTitle);
            dateText = itemView.findViewById(R.id.communityDate);
            idText = itemView.findViewById(R.id.communityUserID);
        }
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String title = communities.get(position).getTitle();
        String id = communities.get(position).getUser_id();
        String date = communities.get(position).getDate();
        String str;
        String[] dates = date.split("\\.");
        String[] a = dates[0].split("T");
        String[] b = a[0].split("-");
        String[] c = a[1].split(":");
        str= b[0]+"년 "+b[1]+"월 "+b[2]+"일 "+c[0]+"시 "+c[1]+"분";

        viewHolder.titleText.setText("제목: "+title);
        viewHolder.idText.setText("작성자: "+ id);
        viewHolder.dateText.setText("날짜: "+str);


    }


}
