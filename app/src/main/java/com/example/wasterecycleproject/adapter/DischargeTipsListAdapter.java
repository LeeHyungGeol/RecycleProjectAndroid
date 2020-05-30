package com.example.wasterecycleproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasterecycleproject.R;
import com.example.wasterecycleproject.model.Discharge;

import java.util.List;

public class DischargeTipsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Discharge> discharges;


    public DischargeTipsListAdapter(List<Discharge> discharge) {
        discharges=discharge;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discharge_list_item, parent, false);
            return new DischargeTipsListAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_loading, parent, false);
            return new DischargeTipsListAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) { //클릭 이벤트 처리
        if (viewHolder instanceof DischargeTipsListAdapter.ItemViewHolder) {
            populateItemRows((DischargeTipsListAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof DischargeTipsListAdapter.LoadingViewHolder) {
            showLoadingView((DischargeTipsListAdapter.LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return discharges == null ? 0 : discharges.size();
    }


    @Override
    public int getItemViewType(int position) {
        return discharges.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView categoryText;
        TextView howToRecycleText;
        TextView correspondingText;
        TextView disCorrespondingText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.recycleCategoryText);
            howToRecycleText = itemView.findViewById(R.id.howToRecycleText);
            correspondingText = itemView.findViewById(R.id.correspondingText);
            disCorrespondingText = itemView.findViewById(R.id.disCorrespondingText);
        }
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    private void showLoadingView(DischargeTipsListAdapter.LoadingViewHolder viewHolder, int position) {
        //
    }

    private void populateItemRows(DischargeTipsListAdapter.ItemViewHolder viewHolder, int position) {

        String category = discharges.get(position).getCategory_m_name();
        String howToRecycle = discharges.get(position).getContent();
        String[] temps = howToRecycle.split("/");
        StringBuilder recycleTip = new StringBuilder(new String());
        for(String temp: temps){
            recycleTip.append("- ").append(temp).append("\n");
        }

        String corresponding = discharges.get(position).getItem_corresponding();
        String disCorresponding = discharges.get(position).getItem_discorresponding();
        viewHolder.categoryText.setText("- "+category);
        viewHolder.howToRecycleText.setText(recycleTip);
        viewHolder.correspondingText.setText("- "+corresponding);
        viewHolder.disCorrespondingText.setText("- "+disCorresponding);

    }

}
