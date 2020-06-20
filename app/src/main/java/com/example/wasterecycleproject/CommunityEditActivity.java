package com.example.wasterecycleproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.RegisterBoardResponseDTO;
import com.example.wasterecycleproject.util.ConfirmDialog;
import com.example.wasterecycleproject.util.RestApi;
import com.example.wasterecycleproject.util.RestApiUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CommunityEditActivity extends FragmentActivity {
    private ConfirmDialog confirmDialog;
    private File imgFile;
    private EditText communityContext; // 게시글 내용
    private TextView contextLimit;
    private ImageView imageView;       // 게시글 이미지
    private Image image;
    private LayoutInflater inflater;
    private Button boardBtn;           //게시글 등록 버튼
    private LinearLayout gallery; //이미지 보여주기 위한 좌우스크롤 레이아웃
    private ImagePicker imagePicker;
    private EditText et_title;   //게시글 제목
    private int communityidx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_community_edit);
        init();
        addListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            image = ImagePicker.getFirstImageOrNull(data);
            imagePick(image);
        }
    }

    public void init(){
        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());
        Intent intent =getIntent();
        communityidx = intent.getIntExtra("position",1);
        imageView= findViewById(R.id.imageView);
        imagePicker = ImagePicker.create(this)
                .single();
        gallery = findViewById(R.id.gallery);
        inflater= LayoutInflater.from(this); //동적 이미지 스크롤을 위한 inflater
        communityContext = findViewById(R.id.communityContext);
        contextLimit = findViewById(R.id.contextLimit);
        boardBtn = findViewById(R.id.boardBtn);
        et_title = findViewById(R.id.editText);
    }

    public void imagePick(Image image){ //이미지 선택 메소드
        View view = inflater.inflate(R.layout.gallery_item, gallery, false);
        imgFile = new File(image.getPath());
        imageView.setImageBitmap(ImageManager.getInstance().getRotatedBitmap(imgFile.getAbsolutePath())); //새로 추가한 코드 : 회전시킨 이미지
        gallery.addView(view); //이미지 레이아웃에 동적 추가
    }

    public void addListener(){
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imagePicker.start();

            }
        });

        communityContext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = communityContext.getText().toString();
                contextLimit.setText(input.length()+" /200 글자 수");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boardBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    edit_board();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void edit_board() {
        progressON("게시글 수정 중입니다...");
        String token = "Token " + AppManager.getInstance().getUser().getToken();
        Retrofit mRetrofit = RestApiUtil.getRetrofitClient(this);
        RestApi restApi = mRetrofit.create(RestApi.class);

        Map<String, RequestBody> boardMap = new HashMap<>();


        try{
            if(et_title.getText().toString().length()!=0){
                RequestBody requestTitle = RequestBody.create(MediaType.parse("text/plain"), et_title.getText().toString());
                boardMap.put("title", requestTitle);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(communityContext.getText().toString().length()!=0){
                RequestBody requestContent = RequestBody.create(MediaType.parse("text/plain"), communityContext.getText().toString());
                boardMap.put("content", requestContent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(imgFile.exists()){
                RequestBody requestImage = RequestBody.create(MediaType.parse("image/*"), imgFile);
                boardMap.put("image\"; filename=\"" + imgFile.getName(), requestImage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Call<RegisterBoardResponseDTO> call = restApi.edit_board(token, boardMap,communityidx);
        call.enqueue(new Callback<RegisterBoardResponseDTO>() {
            @Override
            public void onResponse(Call<RegisterBoardResponseDTO> call, Response<RegisterBoardResponseDTO> response) {
                System.out.println("response.isSuccessful : " + response.isSuccessful());
                if(response.isSuccessful()) {
                    progressOFF();
                    setResult(Activity.RESULT_OK);
                    finish();
                    Intent intent = new Intent(CommunityEditActivity.this,AdvertiseActivity.class);
                    startActivity(intent);
                }
                else {
                    progressOFF();
                    confirmDialog.setMessage("게시글 수정 실패");
                    confirmDialog.show();
                }
            }

            @Override
            public void onFailure(Call<RegisterBoardResponseDTO> call, Throwable t) {
                progressOFF();
                confirmDialog.setMessage("통신 실패");
                confirmDialog.show();
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
