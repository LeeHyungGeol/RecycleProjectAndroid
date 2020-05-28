package com.example.wasterecycleproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;

public class UserCommunityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Community> communities;


    public UserCommunityListAdapter(List<Community> communityList) {
        communities = communityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list_item, parent, false);
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
                Toast.makeText(context, communities.get(position).getIdx() +"", Toast.LENGTH_LONG).show();
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
        viewHolder.titleText.setText("제목: "+title);
        viewHolder.idText.setText("작성자: "+ id);
        viewHolder.dateText.setText("날짜: "+date);


    }


}
