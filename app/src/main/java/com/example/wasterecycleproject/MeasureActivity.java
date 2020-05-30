package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.Measure;
import com.example.wasterecycleproject.model.MeasureLengthResponseDTO;
import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.util.RestApi;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;
import static com.example.wasterecycleproject.util.RestApi.BASE_URL;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MeasureActivity extends AppCompatActivity { //길이 확인 화면

    private File imgFile;
    private ConfirmDialog confirmDialog;
    private ImageView imageView;
    private String imgPath;
    private Intent intent;
    ArrayList<Measure> measureArrayList;

    private Spinner spinner; //콤보박스
    private ArrayList<String> categoryList; //딥러닝 결과를 통해 나온 문자열 리스트
    private ArrayAdapter<String> categoryAdapter;
    private Button measureFeeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_measure);
        setActionBar();
        init();
        initSpinner();
        addListener();

        MeasureLength();
    }


    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    private void init() {
        imageView = findViewById(R.id.ImageView);
        intent = getIntent();                                   // RecycleFragment 로부터 받은 intent
        imgPath = intent.getStringExtra("imgFilePath");  // intent 에서 얻은 imgPath
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        imageView.setImageBitmap(bitmap);


        spinner = findViewById(R.id.spinner);
        categoryList = new ArrayList<>();
        measureFeeBtn = findViewById(R.id.measureFeeBtn);
    }

    private void initSpinner() {
        categoryList.add("선택해주세요"); //리스트에 콤보박스 추가



        categoryAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList);
        spinner.setAdapter(categoryAdapter);
    }

    private void addListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                Toast.makeText(getApplicationContext(), categoryList.get(i) + "가 선택되었습니다.", //선택되었을때 메소드
//                        Toast.LENGTH_SHORT).show();

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

    public void MeasureLength() {
        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        if(imgPath == null) {
            confirmDialog.setMessage("이미지를 선택해주세요.");
            confirmDialog.show();
            return;
        }
        else {
            progressON("길이 측정 중입니다...");
            String token = "Token " + UserToken.getToken();
            Retrofit mRetrofit = RestApiUtil.getRetrofitClient(this);
            RestApi restApi = mRetrofit.create(RestApi.class);

            imgFile = new File(imgPath);

            RequestBody imgFileReqBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", imgFile.getName(), imgFileReqBody);

            Call<MeasureLengthResponseDTO> call = restApi.measure_length(token, image);

            call.enqueue(new Callback<MeasureLengthResponseDTO>() {
                @Override
                public void onResponse(Call<MeasureLengthResponseDTO> call, Response<MeasureLengthResponseDTO> response) {
                    System.out.println(response.isSuccessful());

                    if(response.isSuccessful()) {

                        MeasureLengthResponseDTO measureLengthResponseDTO = response.body();
                        measureArrayList = measureLengthResponseDTO.getMeasure();

                        System.out.println(measureArrayList.size());

                        Glide.with(AppManager.getInstance().getContext())
                                .load("http://3fc7e29c561a.ngrok.io" + measureArrayList.get(1).getImage())
                                .into(imageView);

                        progressOFF();
                        confirmDialog.setMessage("길이 측정 성공");
                        confirmDialog.show();

                    }
                    else {
                        Log.d("길이 측정", "실패");
                        progressOFF();
                        confirmDialog.setMessage("길이 측정 실패");  //딥러닝에서 결과가 안나왔을때 //빈 리스트 //서버에서 주는 것이 아무것도 없음
                        confirmDialog.show();

                    }
                }

                @Override
                public void onFailure(Call<MeasureLengthResponseDTO> call, Throwable t) {
                    progressOFF();
                    System.out.println(t.getMessage());
                    Log.d("이미지 업로드", "실패");
                    confirmDialog.setMessage("이미지 업로드 실패..."); //서버에 이미지 전송 실패
                    confirmDialog.show();
                }
            });
        }
    }


    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }
}