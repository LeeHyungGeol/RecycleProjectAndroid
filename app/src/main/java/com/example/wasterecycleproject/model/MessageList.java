package com.example.wasterecycleproject.model;

import java.util.ArrayList;

public class MessageList {
    private ArrayList<Message> send_message;
    private ArrayList<Message> recv_message;

    public ArrayList<Message> getSend_message() {
        return send_message;
    }

    public void setSend_message(ArrayList<Message> send_message) {
        this.send_message = send_message;
    }

    public ArrayList<Message> getRecv_message() {
        return recv_message;
    }

    public void setRecv_message(ArrayList<Message> recv_message) {
        this.recv_message = recv_message;
    }
}
