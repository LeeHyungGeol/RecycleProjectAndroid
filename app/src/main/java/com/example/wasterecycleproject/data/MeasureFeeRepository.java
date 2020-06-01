package com.example.wasterecycleproject.data;

import com.example.wasterecycleproject.model.MeasureFeeResponseDTO;

import retrofit2.Callback;

public interface MeasureFeeRepository {
    void checkCode();
    void measureFeeData(Callback<MeasureFeeResponseDTO> callback);
}
