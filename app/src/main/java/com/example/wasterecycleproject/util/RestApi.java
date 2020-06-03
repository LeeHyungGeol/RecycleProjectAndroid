package com.example.wasterecycleproject.util;

import com.example.wasterecycleproject.model.AllCommunityResponseDTO;
import com.example.wasterecycleproject.model.AllNoteResponseDTO;
import com.example.wasterecycleproject.model.CommunityDetailResponseDTO;
import com.example.wasterecycleproject.model.DetectionCleanResponseDTO;
import com.example.wasterecycleproject.model.DetectionResponseDTO;
import com.example.wasterecycleproject.model.LocationUpdateDTO;
import com.example.wasterecycleproject.model.LocationUpdateResponseDTO;
import com.example.wasterecycleproject.model.LocationWatseResponseDTO;
import com.example.wasterecycleproject.model.LoginDTO;
import com.example.wasterecycleproject.model.LoginResponseDTO;
import com.example.wasterecycleproject.model.LogoutResponseDTO;
import com.example.wasterecycleproject.model.MeasureFeeDTO;
import com.example.wasterecycleproject.model.MeasureFeeResponseDTO;
import com.example.wasterecycleproject.model.MeasureLengthResponseDTO;
import com.example.wasterecycleproject.model.RegisterBoardResponseDTO;
import com.example.wasterecycleproject.model.RegisterResponseDTO;
import com.example.wasterecycleproject.model.SearchWordDTO;
import com.example.wasterecycleproject.model.SearchWordResponseDTO;
import com.example.wasterecycleproject.model.SendNoteDTO;
import com.example.wasterecycleproject.model.SendNoteResponseDTO;
import com.example.wasterecycleproject.model.User;
import com.example.wasterecycleproject.model.UserCommunityResponseDTO;
import com.example.wasterecycleproject.model.UserProfileResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface RestApi {
    String BASE_URL = "http://de4087a5d582.ngrok.io/";


    @POST("userApp/auth/register/")
    Call<RegisterResponseDTO> register(@Body User user);

    @POST("userApp/auth/login/")
    Call<LoginResponseDTO> login(@Body LoginDTO loginDTO);

    @POST("dischargeTipsApp/textVoiceDischargeTips/")
    Call<SearchWordResponseDTO> search_word(@Header("Authorization") String token, @Body SearchWordDTO searchWordDTO);

    @GET("communityApp/community/")
    Call<AllCommunityResponseDTO> all_community(@Header("Authorization") String token);

    @GET("communityApp/community/detail/{idx}/")
    Call<CommunityDetailResponseDTO> detail_community(@Header("Authorization") String token, @Path("idx") int index);

    @GET("communityApp/community/{user_id}/")
    Call<UserCommunityResponseDTO> user_community(@Header("Authorization") String token, @Path("user_id") String id);

    @POST("messageApp/message/{user_id}/")
    Call<SendNoteResponseDTO> send_note(@Header("Authorization") String token, @Body SendNoteDTO sendNoteDTO, @Path("user_id") String id);

    @GET("messageApp/message/")
    Call<AllNoteResponseDTO> all_note(@Header("Authorization") String token);

    @Multipart
    @POST("detectionApp/detection/")
    Call<DetectionResponseDTO> detection_category(@Header("Authorization") String token, @Part MultipartBody.Part image);

    @Multipart
    @POST("measureApp/measure/")
    Call<MeasureLengthResponseDTO> measure_length(@Header("Authorization") String token, @Part MultipartBody.Part image);

    @Multipart
    @POST("detectionApp/clean/")
    Call<DetectionCleanResponseDTO> detection_clean(@Header("Authorization") String token, @Part MultipartBody.Part image);

    @POST("/measureApp/fee/")
    Call<MeasureFeeResponseDTO> measure_fee(@Header("Authorization") String token, @Body MeasureFeeDTO measureFeeDTO);

    @Multipart
    @POST("communityApp/community/")
    Call<RegisterBoardResponseDTO> register_board(@Header("Authorization") String token, @PartMap Map<String, RequestBody> boardMap);

    @GET("locationApp/location_waste_information/")
    Call<LocationWatseResponseDTO> location_waste_information(@Header("Authorization") String token);

    @GET("userApp/userprofile/")
    Call<UserProfileResponse> user_profile(@Header("Authorization") String token);

    @POST("userApp/auth/logout/")
    Call<LogoutResponseDTO>logout(@Header("Authorization") String token);

    @POST("locationApp/update_location/")
    Call<LocationUpdateResponseDTO>location_update(@Header("Authorization") String token, @Body LocationUpdateDTO locationUpdateDTO);


}
