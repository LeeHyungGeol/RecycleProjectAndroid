package com.example.wasterecycleproject.model;

public class RegisterBoardResponseDTO {

    private Community community;

    public RegisterBoardResponseDTO() {
    }

    public RegisterBoardResponseDTO(Community community) {
        this.community = community;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
