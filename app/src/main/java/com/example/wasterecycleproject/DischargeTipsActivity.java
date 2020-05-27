package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.wasterecycleproject.model.SearchWordDTO;
import com.example.wasterecycleproject.model.SearchWordResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DischargeTipsActivity extends AppCompatActivity { //배출 요령 화면

    private ArrayList<SearchWordResponseDTO> searchWordResponseList;
    private RestApiUtil mRestApiUtil;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discharge_tips);
        setActionBar();
        init();
        getDischargeTips();
    }

    private void getDischargeTips() {
        SearchWordDTO searchWordDTO = new SearchWordDTO();
        searchWordDTO.setSearchWord(searchText.getText().toString());

        mRestApiUtil.getApi().search_word("Token " + UserToken.getToken(), searchWordDTO).enqueue(new Callback<SearchWordResponseDTO>() {
            @Override
            public void onResponse(Call<SearchWordResponseDTO> call, Response<SearchWordResponseDTO> response) {
                if(response.isSuccessful()){
                    Log.d("discharge","성공");
                    SearchWordResponseDTO searchWordResponseDTO = response.body();
                    Log.d("discharge", String.valueOf(searchWordResponseDTO.getTextVoiceDischargeTips().size()));
                }
                else{
                    Log.d("discharge","실패");
                }
            }

            @Override
            public void onFailure(Call<SearchWordResponseDTO> call, Throwable t) {
                Log.d("discharge","실패2");

            }
        });
    }

    private void init() {
        searchWordResponseList = new ArrayList<>();
        searchText = findViewById(R.id.searchText);
    }


    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

}
