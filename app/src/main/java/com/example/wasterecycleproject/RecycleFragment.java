package com.example.wasterecycleproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.DetectionClean;
import com.example.wasterecycleproject.model.DetectionCleanResponseDTO;
import com.example.wasterecycleproject.model.MeasureLengthResponseDTO;
import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.util.RestApi;
import com.example.wasterecycleproject.util.RestApiUtil;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RecycleFragment extends Fragment {

    private String imgPath;
    private ConfirmDialog confirmDialog;
    public File imgFile;
    public Image image;
    private Intent intent;
    private final static int DETECTION_CATEGORY = 1;
    private final static int MEASURE_LEGTH = 2;
    private DetectionClean detectionClean; //올바른 분리배출
    private View view;
    private ImageView imageView; //정중앙 이미지 뷰
    private Button categoryChkBtn;  //품목확인 버튼
    private Button dimensionChkBtn; //길이 측정 버튼
    private Button propRecycleBtn; //올바른 분리배출 버튼
    private Button helpBtn; //도움말 버튼
    private ImagePicker imagePicker;

    public RecycleFragment() {}

    public static RecycleFragment newInstance() {
        RecycleFragment fragment = new RecycleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recycle, container, false);
        Intent intent=new Intent(getActivity(),PopupHelpActivity.class);
        startActivity(intent);
        init();
        addListener();
        return view;
    }


    public void init() {
        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext()); //confirmDialog 초기화
        imageView = view.findViewById(R.id.ImageView);
        imageView.setImageResource(R.drawable.ic_camera_icon);
        categoryChkBtn = view.findViewById(R.id.categoryChkBtn);
        dimensionChkBtn = view.findViewById(R.id.dimensionChkBtn);
        propRecycleBtn = view.findViewById(R.id.propRecycleBtn);
        helpBtn = view.findViewById(R.id.helpBtn);
        imagePicker = ImagePicker.create(this)
                .single();
    }

    public void addListener() {
        imageView.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                imagePicker.start();
            }
        });

        categoryChkBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) { //품목 측정 버튼 리스너
                checkCase(DETECTION_CATEGORY);
            }
        });

        dimensionChkBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) { //규격 측정 버튼 리스너
                checkCase(MEASURE_LEGTH);
            }
        });

        propRecycleBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) { //올바른 분리 배출 버튼 리스너
                startDetectionClean();
            }
        });

        helpBtn.setOnClickListener(new Button.OnClickListener(){ //도움말 리스너

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),PopupHelpActivity.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            image = ImagePicker.getFirstImageOrNull(data);
            imagePick(image);
        }
    }

    public void imagePick(Image image) {
        imgFile = new File(image.getPath());
        imgPath = image.getPath();

        imageView.setBackgroundColor(Color.WHITE);
        imageView.setImageBitmap(ImageManager.getInstance().getRotatedBitmap(imgPath));

//        imageView.setImageBitmap(ImageManager.getInstance().rotate(bitmap, exifDegree));

//        imgFile = new File(image.getPath());
//        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//        imageView.setImageBitmap(bitmap);
    }

    public void checkCase(int activityCase) {

        switch(activityCase) {
            case DETECTION_CATEGORY: //품목 측정
                intent = new Intent(AppManager.getInstance().getContext(), RecycleConfiguartionActivity.class);
                break;
            case MEASURE_LEGTH: //규격 측정
                intent= new Intent(AppManager.getInstance().getContext(), MeasureActivity.class);
                break;
        }
        try {
            Log.d("imgFilePath : ", imgFile.getAbsolutePath());
            intent.putExtra("imgFilePath",imgFile.getAbsolutePath());
            startActivity(intent);
        }
        catch (Exception e) {
            confirmDialog.setMessage("사진을 입력해주세요.");
            confirmDialog.show();
        }
    }

    public void startDetectionClean() {
        intent = new Intent(AppManager.getInstance().getContext(), PopupProperRecycleActivity.class);
        try {
            Log.d("imgFilePath : ", imgFile.getAbsolutePath());
            intent.putExtra("imgFilePath",imgFile.getAbsolutePath());
            detection_clean();
        }
        catch (Exception e) {
            confirmDialog.setMessage("사진을 입력해주세요.");
            confirmDialog.show();
        }
    }

    public void detection_clean() {
        try {
            String token = "Token " + AppManager.getInstance().getUser().getToken();
            Retrofit mRetrofit = RestApiUtil.getRetrofitClient(AppManager.getInstance().getContext());
            RestApi restApi = mRetrofit.create(RestApi.class);

            RequestBody imgFileReqBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", imgFile.getName(), imgFileReqBody);

            progressON("올바른 분리 배출 분석 중입니다...");
            Call<DetectionCleanResponseDTO> call = restApi.detection_clean(token, image);
            call.enqueue(new Callback<DetectionCleanResponseDTO>() {
                @Override
                public void onResponse(Call<DetectionCleanResponseDTO> call, Response<DetectionCleanResponseDTO> response) {

                    System.out.println("response.isSuccessful : " + response.isSuccessful());
                    if (response.isSuccessful()) {
                        DetectionCleanResponseDTO detectionCleanResponseDTO = response.body();
                        detectionClean = detectionCleanResponseDTO.getClean_detection();
                        checkDetectionCleanCase();
                        progressOFF();
                    }
                    else {
                        Log.d("올바른 분리 배출 X", "품목 분류에 실패했습니다.");
                        System.out.println("올바른 분리 배출 X : 품목 분류에 실패했습니다.");

                        progressOFF();
                        intent.putExtra("data", "품목 분류에 실패했습니다.");

                    }
                    intent.putExtra("title","올바른 분리배출 확인");
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<DetectionCleanResponseDTO> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Log.d("올바른 분리 배출 X", "품목 분류에 실패했습니다.");
                    System.out.println("올바른 분리 배출 X : 품목 분류에 실패했습니다.");

                    progressOFF();
                    intent.putExtra("data", "품목 분류에 실패했습니다.");
                    intent.putExtra("title","올바른 분리배출 확인");
                    startActivity(intent);
                }
            });

        }
        catch (Exception e) {
            return;
        }

    }

    public void checkDetectionCleanCase() {   // 올바른 분리 배출 확인
        switch ( detectionClean.getCode()) {
            case 100:
                Log.d("올바른 분리 배출 O", detectionClean.getMsg());
                intent.putExtra("data",detectionClean.getDescription() + "입니다\n"+detectionClean.getValue()+"포인트를 획득하셨습니다");
                break;
            case 101:
            case 102:
            case 103:
                Log.d("올바른 분리 배출 X", detectionClean.getMsg());
                intent.putExtra("data",detectionClean.getMsg());
                break;
        }
    }

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

}