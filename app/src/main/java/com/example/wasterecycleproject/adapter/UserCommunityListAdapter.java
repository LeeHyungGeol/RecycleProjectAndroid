package com.example.wasterecycleproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasterecycleproject.CommunityDetailActivity;
import com.example.wasterecycleproject.R;

import java.util.List;

public class UserCommunityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<String> titleText;
    public List<String> dateText;


    public UserCommunityListAdapter(List<String> titleList,List<String> dateList) {
        titleText = titleList;
        dateText = dateList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list_item, parent, false);
            return new UserCommunityListAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_loading, parent, false);
            return new UserCommunityListAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) { //클릭 이벤트 처리

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CommunityDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                Toast.makeText(context, position +"", Toast.LENGTH_LONG).show();
            }
        });

        if (viewHolder instanceof UserCommunityListAdapter.ItemViewHolder) {
            populateItemRows((UserCommunityListAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof UserCommunityListAdapter.LoadingViewHolder) {
            showLoadingView((UserCommunityListAdapter.LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return titleText == null ? 0 : titleText.size();
    }


    @Override
    public int getItemViewType(int position) {
        return titleText.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView dateText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // TODO : use pos.
                    }
                    else{
                        Log.d("parkwoojin",Integer.toString(pos));
                    }

                }
            });

            titleText = itemView.findViewById(R.id.communityTitle);
            dateText = itemView.findViewById(R.id.communityDate);
        }
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    private void showLoadingView(UserCommunityListAdapter.LoadingViewHolder viewHolder, int position) {
        //
    }

    private void populateItemRows(UserCommunityListAdapter.ItemViewHolder viewHolder, int position) {

        String title = titleText.get(position);
        String date = dateText.get(position);
        viewHolder.titleText.setText(title);
        viewHolder.dateText.setText(date);

    }
}
