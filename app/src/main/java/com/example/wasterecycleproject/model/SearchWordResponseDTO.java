package com.example.wasterecycleproject.model;

import java.util.ArrayList;

public class SearchWordResponseDTO{
    private ArrayList<Discharge> textVoiceDischargeTips;
    private ArrayList<MatchingName> matching_name;

    public ArrayList<Discharge> getTextVoiceDischargeTips() {
        return textVoiceDischargeTips;
    }

    public void setTextVoiceDischargeTips(ArrayList<Discharge> textVoiceDischargeInfo) {
        this.textVoiceDischargeTips = textVoiceDischargeInfo;
    }

    public ArrayList<MatchingName> getMatching_name() {
        return matching_name;
    }

    public void setMatching_name(ArrayList<MatchingName> matching_name) {
        this.matching_name = matching_name;
    }
}
