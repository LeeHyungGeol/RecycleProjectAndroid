package com.example.wasterecycleproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wasterecycleproject.adapter.RecycleExpandableListAdapter;
import com.example.wasterecycleproject.model.SearchWordDTO;
import com.example.wasterecycleproject.model.SearchWordResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private ExpandableListView expandableListView;
    private RestApiUtil mRestApiUtil;
    private List<String> listGroup;
    private HashMap<String, List<String>> listItem;
    private RecycleExpandableListAdapter recycleExpandableListAdapter;
    private View view;
    private ImageButton voiceBtn;
    private ImageButton searchBtn;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText searchText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        initListData();
        initTableData();
        addListener();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("음성인식",result.get(0));
                }
                break;
            }

        }
    }

    private void init() {
        expandableListView = view.findViewById(R.id.expandableListView);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        recycleExpandableListAdapter = new RecycleExpandableListAdapter(getActivity(), listGroup, listItem);
        expandableListView.setAdapter(recycleExpandableListAdapter);
        voiceBtn = view.findViewById(R.id.voiceBtn);
        searchBtn = view.findViewById(R.id.searchBtn);
        mRestApiUtil = new RestApiUtil();
        searchText = view.findViewById(R.id.searchText);
    }

    private void addListener() { //리스너 추가

        voiceBtn.setOnClickListener(new Button.OnClickListener() { //음성 버튼 리스너

            @Override
            public void onClick(View v) {
                promptSpeechInput();
//                Intent intent = new Intent(getActivity(), DischargeTipsActivity.class);
//                startActivity(intent);

            }
        });

        searchBtn.setOnClickListener(new Button.OnClickListener() { //검색 버튼 리스너

            @Override
            public void onClick(View v) {
              searchDischargeTips();
            }
        });

    }

    private void initTableData() { //테이블에 들어갈 데이터 넣기(findViewById 해서 setText 해주면 될듯

    }

    private void initListData() { //확장되는 리스트 위젯에 데이터 넣기
        listGroup.add("생활쓰레기");
        listGroup.add("음식물쓰레기");
        listGroup.add("재활용품");
        String[] array;

        List<String> houseWasteList = new ArrayList<>();
        array = new String[]{"배출요일: ", "배출시작시각: ", "배출종료시각: "}; //생활쓰레기 리스트를 누르면 나오는 내용
        for (String item : array) {
            houseWasteList.add(item);
        }

        List<String> foodWasteList = new ArrayList<>();
        array = new String[]{"배출요일: ", "배출시작시각: ", "배출종료시각: "}; //음식물쓰레기 리스트를 누르면 나오는 내용
        for (String item : array) {
            foodWasteList.add(item);
        }
        List<String> recylceWasteList = new ArrayList<>();
        array = new String[]{"배출요일: ", "배출시작시각: ", "배출종료시각: "}; //재활용품 리스트를 누르면 나오는 내용
        for (String item : array) {
            recylceWasteList.add(item);
        }

        listItem.put(listGroup.get(0), houseWasteList);
        listItem.put(listGroup.get(1), foodWasteList);
        listItem.put(listGroup.get(2), recylceWasteList);

        recycleExpandableListAdapter.notifyDataSetChanged();
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() { //음성쪽
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "원하는 품목의 배출요령을 알려드릴께요");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "지원이 되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void searchDischargeTips(){
        SearchWordDTO searchWordDTO = new SearchWordDTO();
        searchWordDTO.setSearchWord(searchText.getText().toString());

        mRestApiUtil.getApi().search_word("Token " + UserToken.getToken(), searchWordDTO).enqueue(new Callback<SearchWordResponseDTO>() {
            @Override
            public void onResponse(Call<SearchWordResponseDTO> call, Response<SearchWordResponseDTO> response) {
                if(response.isSuccessful()){
                    Log.d("home","성공");
                    SearchWordResponseDTO searchWordResponseDTO = response.body();
                    Log.d("배출요령",searchWordResponseDTO.getTextVoiceDischargeTips().get(0).getContent());
                    Log.d("배출요령",searchWordResponseDTO.getTextVoiceDischargeTips().get(0).getItem_corresponding());
                    Log.d("배출요령",searchWordResponseDTO.getTextVoiceDischargeTips().get(0).getItem_discorresponding());
                    Intent intent = new Intent(getActivity(), DischargeTipsActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.d("home","실패");
                }
            }

            @Override
            public void onFailure(Call<SearchWordResponseDTO> call, Throwable t) {
                Log.d("home","실패2");

            }
        });

    }

}