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
import android.widget.TextView;

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
    private ImageView imageView;
    private Boolean properOrNot;
    private String selectedItem;

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
    }

    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        categoryList = new ArrayList<>();
        detection_lists = new ArrayList<>();
        dischargeBtn = findViewById(R.id.dischargeBtn);
        dimensionText = findViewById(R.id.configuration_dimension);
        imageView = findViewById(R.id.ImageView);
        intent = getIntent();                                   // RecycleFragment 로부터 받은 intent
        imgPath = intent.getStringExtra("imgFilePath");  // intent 에서 얻은 imgPath
//        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        imageView.setImageBitmap(ImageManager.getInstance().getRotatedBitmap(imgPath)); //새로 추가한 코드 : 회전시킨 image
//        imageView.setImageBitmap(bitmap);
        properOrNot= false;
    }

    private void initSpinner(){
        categoryList.add("선택해주세요"); //리스트에 콤보박스 추가
        categoryAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList);
        spinner.setAdapter(categoryAdapter);
    }

    private void addListener() {

        dischargeBtn.setOnClickListener(new Button.OnClickListener(){ //배출요령 확인 버튼 눌렀을때

            @Override
            public void onClick(View v) { //해당하는 품목의 배출요령으로 이동하게
                if(properOrNot && !selectedItem.equals("선택해주세요")) //품목 확인 성공 후 배출 요령 버튼 클릭 시 & 맨 처음 선택해주세요를 선택하지 않았을시
                {
                    Intent intent=new Intent(RecycleConfiguartionActivity.this,DischargeTipsActivity.class);
                    intent.putExtra("searchWord",selectedItem);
                    startActivity(intent);
                }
                else{ //품목 확인 실패 후 배출 요령 버튼 클릭 시
                    confirmDialog.setMessage("배출 요령을 확인할 수 없습니다");
                    confirmDialog.show();
                }

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
                            properOrNot= false;

                        }
                        else {
                            progressOFF();
                            Log.d("품목 확인", "성공");
                            confirmDialog.setMessage("품목 확인 성공");
                            confirmDialog.show();
                            properOrNot= true;
                            detectionResponseDTO = response.body();
                            detection_lists = detectionResponseDTO.getDetection_list();

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    StringBuilder stringBuilder = new StringBuilder("");
                                    if(position==0){
                                        selectedItem = "선택해주세요";
                                    }
                                    else{
                                        selectedItem = detection_lists.get(position-1).getCg_name();
                                        Log.d("선택된 아이템",selectedItem);
                                        for(int i=0; i<detection_lists.get(position-1).getRegulation().size();i++){
                                            stringBuilder.append(detectionResponseDTO.getDetection_list().get(position-1).getRegulation().get(i).getCg_name()).append('\n');

                                        }
                                    }
                                    dimensionText.setText(stringBuilder);


                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });

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
                        confirmDialog.setMessage("품목 확인 실패");  //딥러닝에서 결과가 안나왔을때 //빈 리스트 //서버에서 주는 것이 아무것도 없음
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
