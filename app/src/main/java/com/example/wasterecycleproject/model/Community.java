package com.example.wasterecycleproject.model;

public class Community {
    private int idx;
    private String title;
    private String date;
    private int share_complete;
    private String user_id;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShare_complete() {
        return share_complete;
    }

    public void setShare_complete(int share_complete) {
        this.share_complete = share_complete;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
