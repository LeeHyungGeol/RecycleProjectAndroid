package com.example.wasterecycleproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegisterBoardActivity extends FragmentActivity { //나눔 게시글 작성시 화면

    private EditText communityContext;
    private TextView contextLimit;
    private ImageView imageView;
    private List<Image> images;
    private LayoutInflater inflater;
    private Button boardBtn;
    private LinearLayout gallery; //이미지 보여주기 위한 좌우스크롤 레이아웃
    private ImagePicker imagePicker;
    private int imageCnt;
    private ArrayList<Uri> fileUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_board);
        init();
        addListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images

            images = ImagePicker.getImages(data);
            imageCnt+= images.size();
            if(imageCnt<=5) //이미지 최대 5개 등록 제한을 위함
            {
                imagePick(images, data.getClipData());
            }
            else
            {
                Toast.makeText(this, "이미지는 최대 5개까지 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
                imageCnt-= images.size();
            }
        }
    }

    private void init(){

        imageView= findViewById(R.id.imageView);
        imagePicker = ImagePicker.create(this)
                .limit(5);
        gallery = findViewById(R.id.gallery);
        inflater= LayoutInflater.from(this); //동적 이미지 스크롤을 위한 inflater
        communityContext = findViewById(R.id.communityContext);
        contextLimit=findViewById(R.id.contextLimit);
        boardBtn=findViewById(R.id.boardBtn);
        imageCnt=0;
        fileUris = new ArrayList<>();
    }

    private void addListener() {
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

            }
        });
    }

    private void imagePick(List<Image> images, ClipData clipData){ //이미지 선택 메소드

        for(int i=0;i<images.size();i++){
            View view = inflater.inflate(R.layout.gallery_item,gallery,false);
            ImageView itemView = view.findViewById(R.id.itemImageView);
            File imgFile = new File(images.get(i).getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            itemView.setImageBitmap(bitmap);
            gallery.addView(view); //이미지 레이아웃에 동적 추가
//            ClipData.Item clipItem = clipData.getItemAt(i);
//            Uri uri = clipItem.getUri();
//            fileUris.add(uri);
        }

    }

}
