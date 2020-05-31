package com.example.wasterecycleproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wasterecycleproject.adapter.RecycleExpandableListAdapter;
import com.example.wasterecycleproject.manager.AppManager;
import com.example.wasterecycleproject.manager.ImageManager;
import com.example.wasterecycleproject.model.GpsTracker;
import com.example.wasterecycleproject.model.LocationUpdateDTO;
import com.example.wasterecycleproject.model.LocationUpdateResponseDTO;
import com.example.wasterecycleproject.model.LocationWatseResponseDTO;
import com.example.wasterecycleproject.util.RestApiUtil;
import com.example.wasterecycleproject.util.UserToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment {

    private ExpandableListView expandableListView;
    private RestApiUtil mRestApiUtil;
    private List<String> listGroup;
    private HashMap<String, List<String>> listItem;
    private RecycleExpandableListAdapter recycleExpandableListAdapter;
    private View view;
    private ImageButton voiceBtn;
    private ImageButton searchBtn;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText searchText;
    private Button locationBtn;
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        AppManager.getInstance().setContext(getActivity());
        AppManager.getInstance().setResources(getResources());
        init();
        initListData();
        addListener();
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("음성인식",result.get(0));
                    Intent intent = new Intent(getActivity(), DischargeTipsActivity.class); //음성인식후 화면 전환
                    intent.putExtra("searchWord",result.get(0));
                    startActivity(intent);
                }
                break;
            }

        }
    }

    private void init() {
        expandableListView = view.findViewById(R.id.expandableListView);
        listItem = new HashMap<>();
        listGroup = new ArrayList<>();
        voiceBtn = view.findViewById(R.id.voiceBtn);
        searchBtn = view.findViewById(R.id.searchBtn);
        mRestApiUtil = new RestApiUtil();
        searchText = view.findViewById(R.id.searchText);
        searchText.clearFocus();
        searchText.setText("");
        mRestApiUtil = new RestApiUtil();
        locationBtn = view.findViewById(R.id.locationBtn);
        listGroup.add("생활쓰레기");
        listGroup.add("음식물쓰레기");
        listGroup.add("재활용품");
    }

    private void addListener() { //리스너 추가

        voiceBtn.setOnClickListener(new Button.OnClickListener() { //음성 버튼 리스너

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        searchBtn.setOnClickListener(new Button.OnClickListener() { //검색 버튼 리스너

            @Override
            public void onClick(View v) {
              searchDischargeTips();
            }
        });

        locationBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLocationServicesStatus()) {

                    showDialogForLocationServiceSetting();
                }else {

                    checkRunTimePermission();
                }
                gpsTracker = new GpsTracker(getActivity());
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                String[] adds = address.split(" ");
                for(String add:adds){
                    if(add.contains("동")){
                        address=add;
                        Log.d("위치",address);
                        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO();
                        locationUpdateDTO.setDong(address); //화양동, 구의2동 등등 set
                        mRestApiUtil.getApi().location_update("Token " + UserToken.getToken(),locationUpdateDTO).enqueue(new Callback<LocationUpdateResponseDTO>() {
                            @Override
                            public void onResponse(Call<LocationUpdateResponseDTO> call, Response<LocationUpdateResponseDTO> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(),"지원하지 않는 지역입니다",Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<LocationUpdateResponseDTO> call, Throwable t) {
                                Toast.makeText(getActivity(),"통신 실패",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{

                    }
                }

            }
        });

    }

    private void initListData() { //확장되는 리스트 위젯에 데이터 넣기
        progressON("로딩중입니다");
        listItem.clear();

        mRestApiUtil.getApi().location_waste_information("Token " + UserToken.getToken()).enqueue(new Callback<LocationWatseResponseDTO>() {
            @Override
            public void onResponse(Call<LocationWatseResponseDTO> call, Response<LocationWatseResponseDTO> response) {
                if(response.isSuccessful()){
                    LocationWatseResponseDTO locationWatseResponseDTO = response.body();
                    String[] array;
                    List<String> houseWasteList = new ArrayList<>();
                    array = new String[]{"배출요일: "+locationWatseResponseDTO.getLocation_waste_information().getHouse_day(),
                            "배출방법: "+locationWatseResponseDTO.getLocation_waste_information().getHouse_method(),
                            "배출시작시각: "+locationWatseResponseDTO.getLocation_waste_information().getHouse_start(),
                            "배출종료시각: "+locationWatseResponseDTO.getLocation_waste_information().getHouse_end()}; //생활쓰레기 리스트를 누르면 나오는 내용
                    for (String item : array) {
                        houseWasteList.add(item);
                    }

                    List<String> foodWasteList = new ArrayList<>();
                    array = new String[]{"배출요일: "+locationWatseResponseDTO.getLocation_waste_information().getFood_day(),
                            "배출방법: "+locationWatseResponseDTO.getLocation_waste_information().getFood_method(),
                            "배출시작시각: "+locationWatseResponseDTO.getLocation_waste_information().getFood_start(),
                            "배출종료시각: "+locationWatseResponseDTO.getLocation_waste_information().getFood_end()}; //음식물쓰레기 리스트를 누르면 나오는 내용
                    for (String item : array) {
                        foodWasteList.add(item);
                    }
                    List<String> recylceWasteList = new ArrayList<>();
                    array = new String[]{"배출요일: "+locationWatseResponseDTO.getLocation_waste_information().getRecycle_day(),
                            "배출방법: "+locationWatseResponseDTO.getLocation_waste_information().getRecycle_method(),
                            "배출시작시각: "+locationWatseResponseDTO.getLocation_waste_information().getRecycle_start(),
                            "배출종료시각: "+locationWatseResponseDTO.getLocation_waste_information().getRecycle_end()}; //재활용품 리스트를 누르면 나오는 내용
                    for (String item : array) {
                        recylceWasteList.add(item);
                    }

                    listItem.put(listGroup.get(0), houseWasteList);
                    listItem.put(listGroup.get(1), foodWasteList);
                    listItem.put(listGroup.get(2), recylceWasteList);

                    recycleExpandableListAdapter = new RecycleExpandableListAdapter(getActivity(), listGroup, listItem);
                    expandableListView.setAdapter(recycleExpandableListAdapter);
                    progressOFF();
                }
                else{

                }

            }

            @Override
            public void onFailure(Call<LocationWatseResponseDTO> call, Throwable t) {
                Log.d("HomeFragment","통신오류");

            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() { //음성쪽
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "원하는 품목의 배출요령을 알려드릴께요");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    "지원이 되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void searchDischargeTips(){
        Intent intent = new Intent(getActivity(), DischargeTipsActivity.class);
        intent.putExtra("searchWord",searchText.getText().toString());
        startActivity(intent);
    }


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

//                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
//                    finish();


                }else {
//
//                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "잠시후 다시 시도해주세요", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

}