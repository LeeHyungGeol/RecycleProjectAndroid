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
import com.example.wasterecycleproject.PopUpSendNoteActivity;
import com.example.wasterecycleproject.R;
import com.example.wasterecycleproject.RegisterBoardActivity;
import com.example.wasterecycleproject.model.Community;
import java.util.List;

public class RecycleDimensionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<String> dimensionList;


    public RecycleDimensionListAdapter(List<String> dimensionlist) {
        dimensionList = dimensionlist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dimension_list_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int position) { //클릭 이벤트 처리
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return dimensionList == null ? 0 : dimensionList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return dimensionList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView dimensionText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            dimensionText = itemView.findViewById(R.id.dimensionText);
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

        String dimension = dimensionList.get(position);
        viewHolder.dimensionText.setText("규격"+ dimension);

    }


}
