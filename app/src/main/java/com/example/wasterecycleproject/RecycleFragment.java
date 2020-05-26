package com.example.wasterecycleproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.io.File;


public class RecycleFragment extends Fragment {

    private View view;
    public Image image;
    private ImageView imageView; //정중앙 이미지 뷰
    private Button categoryChkBtn;  //품목확인 버튼
    private Button dimensionChkBtn; //길이 측정 버튼
    private Button propRecycleBtn; //올바른 분리배출 버튼
    private Button helpBtn; //도움말 버튼
    private ImagePicker imagePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recycle, container, false);
        init();
        addListener();
        return view;
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

    private void imagePick(Image image) {
        File imgFile = new File(image.getPath());
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
    }

    private void init() {
        imageView = view.findViewById(R.id.ImageView);
        categoryChkBtn = view.findViewById(R.id.categoryChkBtn);
        dimensionChkBtn = view.findViewById(R.id.dimensionChkBtn);
        propRecycleBtn = view.findViewById(R.id.propRecycleBtn);
        helpBtn = view.findViewById(R.id.helpBtn);
        imagePicker = ImagePicker.create(this)
                .single();
    }

    private void addListener() {
        imageView.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                imagePicker.start();
            }
        });

        categoryChkBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) { //품목 측정버튼 리스너
                Intent intent=new Intent(getActivity(),RecycleConfiguartionActivity.class);
                startActivity(intent);
            }
        });

        dimensionChkBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) { //규격 측정 버튼 리스너
                Intent intent=new Intent(getActivity(),MeasureActivity.class);
                startActivity(intent);
            }
        });

        propRecycleBtn.setOnClickListener(new Button.OnClickListener(){ //올바른 분리배출 버튼 리스너

            @Override
            public void onClick(View v) {
                boolean status =true; //올바른 배출인지 아닌지 이거는 알아서 입맛대로
                if(status){ //올바른 분리배출일때
                    Intent intent=new Intent(getActivity(),PopupProperRecycleActivity.class);
                    intent.putExtra("title","올바른 분리배출 확인");
                    intent.putExtra("data","올바른 분리배출입니다.");
                    startActivity(intent);
                }
                else //올바른 배출이 아닐때
                {

                    Intent intent=new Intent(getActivity(),PopupProperRecycleActivity.class);
                    intent.putExtra("title","올바른 분리배출 확인");
                    intent.putExtra("data","올바른 분리배출이 아닙니다.");
                    startActivity(intent);
                }


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

}
