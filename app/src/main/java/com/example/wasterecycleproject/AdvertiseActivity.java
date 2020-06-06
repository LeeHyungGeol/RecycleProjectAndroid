package com.example.wasterecycleproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.AdvertisementResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.text.DecimalFormat;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertiseActivity extends Activity {

    private TextView advertiseText;
    private ImageView advertiseImage;
    private RestApiUtil mRestApiUtil;
    private TextView advertisePrice;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advertise);
        init();
        setAdvertise();
    }

    private void init() {
        advertiseText = findViewById(R.id.advertiseTitle);
        advertiseImage = findViewById(R.id.advertiseImage);
        advertisePrice = findViewById(R.id.advertisePrice);
        mRestApiUtil = new RestApiUtil();
    }

    private void setAdvertise() {

        mRestApiUtil.getApi().advertisement("Token "+ UserToken.getToken()).enqueue(new Callback<AdvertisementResponseDTO>() {
            @Override
            public void onResponse(Call<AdvertisementResponseDTO> call, Response<AdvertisementResponseDTO> response) {
                if(response.isSuccessful()){
                    final AdvertisementResponseDTO advertisementResponseDTO = response.body();
                    Random rnd = new Random();
                    pos = rnd.nextInt(advertisementResponseDTO.getAdvertisement().size());
                    advertiseText.setText(advertisementResponseDTO.getAdvertisement().get(pos).getTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""));
                    int price = Integer.parseInt(advertisementResponseDTO.getAdvertisement().get(pos).getLprice());
                    DecimalFormat dc = new DecimalFormat("###,###,###,###");
                    advertisePrice.setText(dc.format(price)+"원");
                    String url = advertisementResponseDTO.getAdvertisement().get(pos).getImage();
                    ImageManager.getInstance().GlideWithContext(getApplicationContext(),advertiseImage,url);

                    advertiseImage.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertisementResponseDTO.getAdvertisement().get(pos).getLink())); startActivity(intent);

                        }
                    });


                }else{
                    Log.d("response ","실패");
                }

            }

            @Override
            public void onFailure(Call<AdvertisementResponseDTO> call, Throwable t) {
                Log.d("통신 ","실패");
            }
        });


    }





    //취소 버튼 클릭
    public void mOnClose(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
