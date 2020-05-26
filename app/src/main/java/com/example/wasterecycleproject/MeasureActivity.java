package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MeasureActivity extends AppCompatActivity { //길이 확인 화면

    private Spinner spinner; //콤보박스
    private ArrayList<String> categoryList; //딥러닝 결과를 통해 나온 문자열 리스트
    private ArrayAdapter<String> categoryAdapter;
    private Button measureFeeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        setActionBar();
        init();
        initSpinner();
        addListener();
    }


    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        categoryList = new ArrayList<>();
        measureFeeBtn = findViewById(R.id.measureFeeBtn);
    }

    private void initSpinner() {
        categoryList.add("선택해주세요"); //리스트에 콤보박스 추가
        categoryList.add("노트북");
        categoryList.add("TV");
        categoryAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList);
        spinner.setAdapter(categoryAdapter);
    }

    private void addListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), categoryList.get(i) + "가 선택되었습니다.", //선택되었을때 메소드
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        measureFeeBtn.setOnClickListener(new Button.OnClickListener(){ //처분 수수료 확인 버튼 눌렀을때

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeasureActivity.this,PopupProperRecycleActivity.class);
                intent.putExtra("title","규격에 따른 수수료 측정");
                intent.putExtra("data","처분을 위한 수수료는 0원입니다");
                startActivity(intent);

            }
        });



    }
}