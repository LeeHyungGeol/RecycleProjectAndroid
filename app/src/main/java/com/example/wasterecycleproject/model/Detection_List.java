package com.example.wasterecycleproject.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Detection_List implements Serializable {
    private int idx;
    private String cg_name;
    private ArrayList<Regulation> regulation;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getCg_name() {
        return cg_name;
    }

    public void setCg_name(String cg_name) {
        this.cg_name = cg_name;
    }

    public ArrayList<Regulation> getRegulation() {
        return regulation;
    }

    public void setRegulation(ArrayList<Regulation> regulation) {
        this.regulation = regulation;
    }

}