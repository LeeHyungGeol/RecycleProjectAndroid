package com.example.wasterecycleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wasterecycleproject.adapter.MeasureSliderAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.ListForSearchWaste;
import com.example.wasterecycleproject.model.Measure;
import com.example.wasterecycleproject.model.MeasureFee;
import com.example.wasterecycleproject.model.MeasureFeeDTO;
import com.example.wasterecycleproject.model.MeasureFeeResponseDTO;
import com.example.wasterecycleproject.model.MeasureLengthResponseDTO;
import com.example.wasterecycleproject.model.SliderItem;
import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.util.RestApi;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import static com.example.wasterecycleproject.util.RestApi.BASE_URL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private String imgPath;
    private Intent intent;
    ArrayList<Measure> measureArrayList;

    //autoCompleteTextView
    private AutoCompleteTextView autoCompleteTextView;
    private ListForSearchWaste listForSearchWaste;
    private List<String> list;
    //가로, 세로 길이
    private float width;
    private float height;
    private TextView tv_width;
    private TextView tv_height;
    //sliderView
    private SliderView sliderView;
    private MeasureSliderAdapter adapter;
    //수수료 측정
    private Button measureFeeBtn;
    private MeasureFeeDTO measureFeeDTO;
    private RestApiUtil mRestApiUtil;
    private Intent measureFeeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_measure);
        setActionBar();
        init();
        //
        MeasureLength();
        addListener();
    }


    public void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    public void init() {

        intent = getIntent();                                   // RecycleFragment 로부터 받은 intent
        imgPath = intent.getStringExtra("imgFilePath");  // intent 에서 얻은 imgPath
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

        //confirmDialog
        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        //autoCompleteTextView, TextView
        list = new ArrayList<String>();
        listForSearchWaste = new ListForSearchWaste(list);
        listForSearchWaste.addSearchList();

        autoCompleteTextView = findViewById(R.id.MeasureAutoCompleteTextView);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  listForSearchWaste.getSearchWasteList() ));

        //가로 세로 길이 textView
        tv_height = findViewById(R.id.heightTextView);
        tv_width = findViewById(R.id.widthTextView);

        //sliderView
        sliderView = findViewById(R.id.imageSlider);

        adapter = new MeasureSliderAdapter(this);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(6000); //slide 하나 자동으로 넘어가는 시간
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        //수수료 측정 버튼
        measureFeeBtn = findViewById(R.id.measureFeeBtn);
    }


    public void addListener() {
        sliderView.setCurrentPageListener(new SliderView.OnSliderPageListener() {
            @Override
            public void onSliderPageChanged(int position) {
//                sliderView.setCurrentPagePosition(position);
                try {
                    width = measureArrayList.get(position).getWidth();
                    height = measureArrayList.get(position).getHeight();
                    Log.d("width", String.valueOf(width));
                    Log.d("height",String.valueOf(height));
                    String widthText = "가로 길이: "+String.format("%.1f",measureArrayList.get(position).getWidth())+"cm";
                    String heightText = "세로 길이: "+String.format("%.1f",measureArrayList.get(position).getHeight())+"cm";
                    tv_width.setText(widthText);
                    tv_height.setText(heightText);
                }
                catch (Exception e) {
                    Log.d("Exception : ", String.valueOf(e));
                    Log.d("가로 길이", String.valueOf(width));
                    Log.d("세로 길이", String.valueOf(height));
                    tv_width.setText("가로 길이를 확인할 수 없습니다");
                    tv_height.setText("세로 길이를 확인할 수 없습니다");
                }
            }
        });

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
                try {
                    width = measureArrayList.get(position).getWidth();
                    height = measureArrayList.get(position).getHeight();
                    Log.d("width", String.valueOf(width));
                    Log.d("height",String.valueOf(height));
                    String widthText = "가로 길이: "+String.format("%.1f",measureArrayList.get(position).getWidth())+"cm";
                    String heightText = "세로 길이: "+String.format("%.1f",measureArrayList.get(position).getHeight())+"cm";
                    tv_width.setText(widthText);
                    tv_height.setText(heightText);
                }
                catch (Exception e) {
                    Log.d("Exception : ", String.valueOf(e));
                    Log.d("가로 길이", String.valueOf(width));
                    Log.d("세로 길이", String.valueOf(height));
                    tv_width.setText("가로 길이를 확인할 수 없습니다");
                    tv_height.setText("세로 길이를 확인할 수 없습니다");
                }
            }
        });


        measureFeeBtn.setOnClickListener(new Button.OnClickListener(){ //처분 수수료 확인 버튼 눌렀을때

            @Override
            public void onClick(View v) {
                String searchWord = autoCompleteTextView.getText().toString();
                measureFeeIntent = new Intent(AppManager.getInstance().getContext(), PopupProperRecycleActivity.class);
                checkCase(searchWord);
            }
        });
    }

    public void checkCase(String searchWord) {
        if(searchWord.length() == 0) {
            measureFeeIntent.putExtra("data", "검색어를 입력해주세요.");
            measureFeeIntent.putExtra("title","폐기물 수수료 측정");
            startActivity(measureFeeIntent);
        }
        else {
            measure_fee(searchWord);
        }
        return;
    }

    public void MeasureLength() {
        progressON("길이 측정 중입니다");

        String token = "Token " + AppManager.getInstance().getUser().getToken();
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
                System.out.println(response.body());
                MeasureLengthResponseDTO measureLengthResponseDTO = response.body();

                if (response.isSuccessful()) {
                    measureArrayList = measureLengthResponseDTO.getMeasure();
                    System.out.println(measureArrayList.size());

                    if (measureArrayList.get(0).getCode() == 100) {

                        String widthText = "가로 길이: "+String.format("%.1f",measureArrayList.get(0).getWidth())+"cm";
                        String heightText = "세로 길이: "+String.format("%.1f",measureArrayList.get(0).getHeight())+"cm";
                        tv_height.setText(heightText);
                        tv_width.setText(widthText);
                        progressOFF();
                        confirmDialog.setMessage("길이 측정 완료");
                        confirmDialog.show();

                        for (int i = 0; i < measureArrayList.size(); i++) {

                            adapter.addItem(new SliderItem(ImageManager.getInstance().getFullImageString(measureArrayList.get(i).getImage())));
                        }

                    }
                    else if (measureArrayList.get(0).getCode() == 101) {
                        Log.d("마커 인식", "실패");
                        progressOFF();
                        confirmDialog.setMessage(measureArrayList.get(0).getMsg());
                        confirmDialog.show();

                    }
                    else {
                        Log.d("길이 측정", "실패");
                        progressOFF();
                        confirmDialog.setMessage(measureArrayList.get(0).getMsg());  //딥러닝에서 결과가 안나왔을때 //빈 리스트 //서버에서 주는 것이 아무것도 없음
                        confirmDialog.show();
                    }
                } else {
                    progressOFF();
                    Log.d("길이 측정", "실패");
                    confirmDialog.setMessage("길이 측정 실패"); //서버에 이미지 전송 실패
                    confirmDialog.show();
                }

            }

            @Override
            public void onFailure(Call<MeasureLengthResponseDTO> call, Throwable t) {
                progressOFF();
                System.out.println(t.getMessage());
                Log.d("이미지 업로드", "실패");
                confirmDialog.setMessage("이미지 업로드 실패"); //서버에 이미지 전송 실패
                confirmDialog.show();
            }
        });

    }

    public void measure_fee(String searchWord) {

        progressON("수수료 측정 중입니다");
        String token = "Token " + AppManager.getInstance().getUser().getToken();
        measureFeeDTO = new MeasureFeeDTO(searchWord, height, width);

        mRestApiUtil = new RestApiUtil();
        mRestApiUtil.getApi().measure_fee(token, measureFeeDTO).enqueue(new Callback<MeasureFeeResponseDTO>() {
            @Override
            public void onResponse(Call<MeasureFeeResponseDTO> call, Response<MeasureFeeResponseDTO> response) {
                System.out.println("response.isSuccessful : " + response.isSuccessful());

                if(response.isSuccessful()) {
                    progressOFF();
                    MeasureFeeResponseDTO measureFeeResponseDTO = response.body();
                    MeasureFee measureFee = measureFeeResponseDTO.getFee();
                    if(measureFee.getCode()==100) {
                        measureFeeIntent.putExtra("data","수수료는 "+measureFee.getItem_fee()+"입니다");
                    }
                    else if(measureFee.getCode() == 101) {
                        measureFeeIntent.putExtra("data",measureFee.getMsg());
                    }
                }
                else {
                    progressOFF();
                    measureFeeIntent.putExtra("data","품목명 잘못 입력 또는 길이에 따른\n 수수료가 없는 품목입니다.");
                }
                measureFeeIntent.putExtra("title","폐기물 수수료 측정");
                startActivity(measureFeeIntent);
            }

            @Override
            public void onFailure(Call<MeasureFeeResponseDTO> call, Throwable t) {
                progressOFF();
                System.out.println(t.getMessage());
                measureFeeIntent.putExtra("data","품목명 잘못 입력 또는 길이에 따른\n 수수료가 없는 품목입니다.");
                measureFeeIntent.putExtra("title","폐기물 수수료 측정");
                startActivity(measureFeeIntent);
            }
        });

    }


    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

}