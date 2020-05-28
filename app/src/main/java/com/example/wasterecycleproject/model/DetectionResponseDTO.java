package com.example.wasterecycleproject.model;

import java.util.ArrayList;

public class DetectionResponseDTO {
    private ArrayList<Detection_List> detection_list;

    public ArrayList<Detection_List> getDetection_list() {
        return detection_list;
    }

    public void setDetection_list(ArrayList<Detection_List> detection_list) {
        this.detection_list = detection_list;
    }
}