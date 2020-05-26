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

public class RecycleConfiguartionActivity extends AppCompatActivity { //딥러닝을 돌린 결과가 나오는 품목 확인 화면

    private Spinner spinner; //콤보박스
    private ArrayList<String> categoryList; //딥러닝 결과를 통해 나온 문자열 리스트
    private ArrayAdapter<String> categoryAdapter;
    private Button dischargeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_configuartion);
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
        dischargeBtn = findViewById(R.id.dischargeBtn);
    }

    private void initSpinner(){
        categoryList.add("선택해주세요"); //리스트에 콤보박스 추가
        categoryList.add("장롱");
        categoryList.add("서랍장");
        categoryAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList);
        spinner.setAdapter(categoryAdapter);
    }

    private void addListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),categoryList.get(i)+"가 선택되었습니다.", //선택되었을때 메소드
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        dischargeBtn.setOnClickListener(new Button.OnClickListener(){ //배출요령 확인 버튼 눌렀을때

            @Override
            public void onClick(View v) { //해당하는 품목의 배출요령으로 이동하게

                Intent intent=new Intent(RecycleConfiguartionActivity.this,DischargeTipsActivity.class);
                startActivity(intent);

            }
        });



    }


}
