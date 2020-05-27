package com.example.wasterecycleproject.util;

import com.example.wasterecycleproject.model.AllCommunityResponseDTO;
import com.example.wasterecycleproject.model.LoginDTO;
import com.example.wasterecycleproject.model.LoginResponseDTO;
import com.example.wasterecycleproject.model.RegisterResponseDTO;
import com.example.wasterecycleproject.model.SearchWordDTO;
import com.example.wasterecycleproject.model.SearchWordResponseDTO;
import com.example.wasterecycleproject.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestApi {
    String BASE_URL = "http://9ac6bc80.ngrok.io/";


    @POST("userApp/auth/register/")
    Call<RegisterResponseDTO> register(@Body User user);

    @POST("userApp/auth/login/")
    Call<LoginResponseDTO> login(@Body LoginDTO loginDTO);

    @POST("dischargeTipsApp/textVoiceDischargeTips/")
    Call<SearchWordResponseDTO> search_word(@Header("Authorization") String token, @Body SearchWordDTO searchWordDTO);

    @GET("communityApp/community/")
    Call<AllCommunityResponseDTO> all_community(@Header("Authorization") String token);




}
