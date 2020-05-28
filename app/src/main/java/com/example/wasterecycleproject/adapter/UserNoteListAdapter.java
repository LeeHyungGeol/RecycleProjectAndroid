package com.example.wasterecycleproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasterecycleproject.R;
import com.example.wasterecycleproject.model.Message;

import java.util.List;

public class UserNoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Message> messages;


    public UserNoteListAdapter(List<Message> message) {
        messages=message;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list_item, parent, false);
            return new UserNoteListAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_loading, parent, false);
            return new UserNoteListAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) { //클릭 이벤트 처리
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Toast.makeText(context, position +"", Toast.LENGTH_LONG).show();
            }
        });

        if (viewHolder instanceof UserNoteListAdapter.ItemViewHolder) {
            populateItemRows((UserNoteListAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof UserNoteListAdapter.LoadingViewHolder) {
            showLoadingView((UserNoteListAdapter.LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }


    @Override
    public int getItemViewType(int position) {
        return messages.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
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

    private void showLoadingView(UserNoteListAdapter.LoadingViewHolder viewHolder, int position) {
        //
    }

    private void populateItemRows(UserNoteListAdapter.ItemViewHolder viewHolder, int position) {

        String title = messages.get(position).getContent();
        String id = messages.get(position).getSender_id();
        String date = messages.get(position).getSend_date();
        viewHolder.titleText.setText("쪽지 내용: "+title);
        viewHolder.idText.setText("보낸 사람: "+ id);
        viewHolder.dateText.setText("보낸 날짜: "+date);

    }
}
