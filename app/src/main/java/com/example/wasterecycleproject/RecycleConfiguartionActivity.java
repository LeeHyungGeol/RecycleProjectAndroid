package com.example.wasterecycleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wasterecycleproject.adapter.RecycleDimensionListAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.DetectionResponseDTO;
import com.example.wasterecycleproject.model.Detection_List;
import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.util.RestApi;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecycleConfiguartionActivity extends AppCompatActivity { //딥러닝을 돌린 결과가 나오는 품목 확인 화면

    private File imgFile;
    private ConfirmDialog confirmDialog;
    private String imgPath;
    private Intent intent;
    private ArrayList<Detection_List> detection_lists;
    private Spinner spinner; //콤보박스
    private ArrayList<String> categoryList; //딥러닝 결과를 통해 나온 문자열 리스트
    private ArrayAdapter<String> categoryAdapter;
    private Button dischargeBtn;
    private DetectionResponseDTO detectionResponseDTO;
    private TextView dimensionText;
    private TextView dimensionText2;
    private TextView dimensionText3;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        setContentView(R.layout.activity_recycle_configuartion);
        setActionBar();
        init();
        initSpinner();
        DetectionCategory();
        addListener();
        imageView = findViewById(R.id.ImageView);
        intent = getIntent();                                   // RecycleFragment 로부터 받은 intent
        imgPath = intent.getStringExtra("imgFilePath");  // intent 에서 얻은 imgPath
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        imageView.setImageBitmap(bitmap);
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    private void init() {
        intent = getIntent();                                   // RecycleFragment 로부터 받은 intent
        imgPath = intent.getStringExtra("imgFilePath");  // intent 에서 얻은 imgPath
        spinner = findViewById(R.id.spinner);
        categoryList = new ArrayList<>();
        detection_lists = new ArrayList<>();
        dischargeBtn = findViewById(R.id.dischargeBtn);
        dimensionText = findViewById(R.id.configuration_dimension);
        dimensionText2 = findViewById(R.id.configuration_dimension2);
        dimensionText3 = findViewById(R.id.configuration_dimension3);
    }

    private void initSpinner(){
        categoryList.add("선택해주세요"); //리스트에 콤보박스 추가
        categoryAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList);
        spinner.setAdapter(categoryAdapter);
    }

    private void addListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


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

    public void DetectionCategory() {
        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        if(imgPath == null) {
            confirmDialog.setMessage("이미지를 선택해주세요.");
            confirmDialog.show();
            return;
        }
        else {
            progressON("품목 분석 중입니다...");
            String token = "Token " + UserToken.getToken();
            Retrofit mRetrofit = RestApiUtil.getRetrofitClient(this);
            RestApi restApi = mRetrofit.create(RestApi.class);

            imgFile = new File(imgPath);

            RequestBody imgFileReqBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", imgFile.getName(), imgFileReqBody);

            Call<DetectionResponseDTO> call = restApi.detection_category(token, image);

            call.enqueue(new Callback<DetectionResponseDTO>() {
                @Override
                public void onResponse(Call<DetectionResponseDTO> call, Response<DetectionResponseDTO> response) {

                    System.out.println(response.isSuccessful());
                    if (response.isSuccessful()) {

                        if (response.code() == 204) {
                            Log.d("품목 확인", "실패");
                            progressOFF();
                            confirmDialog.setMessage("품목 확인 실패");  //딥러닝에서 결과가 안나왔을때 //빈 리스트 //서버에서 주는 것이 아무것도 없음
                            confirmDialog.show();

                        }
                        else {
                            progressOFF();
                            Log.d("품목 확인", "성공");
                            confirmDialog.setMessage("품목 확인 성공");
                            confirmDialog.show();
                            detectionResponseDTO = response.body();
                            detection_lists = detectionResponseDTO.getDetection_list();
                            Log.d("규격",detectionResponseDTO.getDetection_list().get(0).getRegulation().get(0).getCg_name());
                            dimensionText.setText(detectionResponseDTO.getDetection_list().get(0).getRegulation().get(0).getCg_name());
                            dimensionText2.setText(detectionResponseDTO.getDetection_list().get(0).getRegulation().get(1).getCg_name());
                            dimensionText3.setText(detectionResponseDTO.getDetection_list().get(0).getRegulation().get(2).getCg_name());

                            if(detection_lists != null) {
                                for (int i = 0; i < detection_lists.size(); i++) {
                                    categoryList.add(detection_lists.get(i).getCg_name());
                                }
                            }
                        }

                    }
                    else {
                        Log.d("이미지 업로드", "실패");
                        progressOFF();
                        confirmDialog.setMessage("이미지 확인 실패");  //딥러닝에서 결과가 안나왔을때 //빈 리스트 //서버에서 주는 것이 아무것도 없음
                        confirmDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<DetectionResponseDTO> call, Throwable t) {
                    progressOFF();
                    System.out.println(t.getMessage());
                    Log.d("이미지 업로드", "실패");
                    confirmDialog.setMessage("이미지 확인 실패..."); //서버에 이미지 전송 실패
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
