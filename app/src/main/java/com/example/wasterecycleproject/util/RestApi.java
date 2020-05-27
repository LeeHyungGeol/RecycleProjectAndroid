package com.example.wasterecycleproject.util;

import com.example.wasterecycleproject.model.LoginDTO;
import com.example.wasterecycleproject.model.LoginResponseDTO;
import com.example.wasterecycleproject.model.RegisterResponseDTO;
import com.example.wasterecycleproject.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApi {
    String BASE_URL = "http://7163ab62.ngrok.io/";


    @POST("userApp/auth/register/")
    Call<RegisterResponseDTO> register(@Body User user);

    @POST("userApp/auth/login/")
    Call<LoginResponseDTO> login(@Body LoginDTO loginDTO);



}
