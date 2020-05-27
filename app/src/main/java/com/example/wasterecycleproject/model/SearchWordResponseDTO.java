package com.example.wasterecycleproject.model;

import java.util.ArrayList;

public class SearchWordResponseDTO{
    private ArrayList<Discharge> textVoiceDischargeTips;

    public ArrayList<Discharge> getTextVoiceDischargeTips() {
        return textVoiceDischargeTips;
    }

    public void setTextVoiceDischargeTips(ArrayList<Discharge> textVoiceDischargeInfo) {
        this.textVoiceDischargeTips = textVoiceDischargeInfo;
    }



}
