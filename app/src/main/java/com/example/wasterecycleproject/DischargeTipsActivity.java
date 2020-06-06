package com.example.wasterecycleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

import com.example.wasterecycleproject.adapter.DischargeTipsListAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.Discharge;
import com.example.wasterecycleproject.model.MatchingName;
import com.example.wasterecycleproject.model.SearchWordDTO;
import com.example.wasterecycleproject.model.SearchWordResponseDTO;
import com.example.wasterecycleproject.model.SendNoteResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DischargeTipsActivity extends AppCompatActivity { //배출 요령 화면

    private SearchWordDTO searchWordDTO;
    private RestApiUtil mRestApiUtil;
    private RecyclerView recyclerView;
    private DischargeTipsListAdapter dischargeTipsListAdapter;
    private String searchWord;
    private ArrayList<Discharge> discharges;
    private ArrayList<Discharge> discharge;
    private ArrayList<MatchingName> matchingNames;
    private ArrayList<MatchingName> matchingName;

    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        setContentView(R.layout.activity_discharge_tips);
        setActionBar();
        init();
        setSearchWord();
        initScrollListener();
    }


    private void init() {
        searchWord=getIntent().getStringExtra("searchWord");
        Log.d("dischargeTipsActivity",searchWord);
        mRestApiUtil = new RestApiUtil();
        searchWordDTO = new SearchWordDTO();
        discharges = new ArrayList<>();
        discharge = new ArrayList<>();
        matchingNames = new ArrayList<>();
        matchingName = new ArrayList<>();
        recyclerView = findViewById(R.id.dischargeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(DischargeTipsActivity.this));
        dischargeTipsListAdapter = new DischargeTipsListAdapter(discharge);
        recyclerView.setAdapter(dischargeTipsListAdapter);
        isLoading = false;
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    private void setSearchWord() {
        progressON("로딩중입니다");
        searchWordDTO.setSearchWord(searchWord);
        mRestApiUtil.getApi().search_word("Token " + UserToken.getToken(),searchWordDTO).enqueue(new Callback<SearchWordResponseDTO>() {
            @Override
            public void onResponse(Call<SearchWordResponseDTO> call, Response<SearchWordResponseDTO> response) {
                if(response.isSuccessful()){
                    dischargeTipsListAdapter.notifyDataSetChanged();
                    SearchWordResponseDTO searchWordResponseDTO = response.body();
                    discharges= searchWordResponseDTO.getTextVoiceDischargeTips();
                    matchingNames = searchWordResponseDTO.getMatching_name();

                    final int dischargeSize = discharges.size();

                    if(matchingNames.size()==0){
                        for(int i=0;i<dischargeSize;i++){
                            discharges.get(i).setName(discharges.get(i).getCategory_m_name());
                        }
                    }
                    else{
                        for(int i=0;i<dischargeSize;i++){
                            discharges.get(i).setName(matchingNames.get(i).getCg_name());
                        }
                    }


                    for(int i=0;i<dischargeSize;i++){
                        discharge.add(discharges.get(i));
                    }
                    progressOFF();

                }
                else {
                     Log.d("DisChargeTipsActivity","response 실패");

                }
            }

            @Override
            public void onFailure(Call<SearchWordResponseDTO> call, Throwable t) {

                Log.d("DisChargeTipsActivity","통신 실패");

            }
        });

    }

    private void dataMore(){
        discharge.add(null);
        dischargeTipsListAdapter.notifyItemInserted(discharge.size() -1 );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                discharge.remove(discharge.size() -1 );
                int scrollPosition = discharge.size();
                dischargeTipsListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                for (int i=currentSize; i<nextLimit; i++) {
                    if (i == discharges.size()) {
                        return;
                    }
                    discharge.add(discharges.get(i));
                }

                dischargeTipsListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }

    // 리싸이클러뷰 이벤트시
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == discharge.size() - 1) {
                        dataMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity) AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }



}
