package com.example.wasterecycleproject.model;

public class Message {
    private int idx;
    private String sender_id;
    private String receiver_id;
    private String send_date;
    private String content;
    private int recv_chk;
    private int recv_idx;
    private int send_idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRecv_chk() {
        return recv_chk;
    }

    public void setRecv_chk(int recv_chk) {
        this.recv_chk = recv_chk;
    }

    public int getRecv_idx() {
        return recv_idx;
    }

    public void setRecv_idx(int recv_idx) {
        this.recv_idx = recv_idx;
    }

    public int getSend_idx() {
        return send_idx;
    }

    public void setSend_idx(int send_idx) {
        this.send_idx = send_idx;
    }
}
