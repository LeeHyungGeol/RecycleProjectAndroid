package com.example.wasterecycleproject.model;

public class MeasureFeeDTO {

    private String cg_name;
    private float width;
    private float height;

    public MeasureFeeDTO() {
    }

    public MeasureFeeDTO(String cg_name, float width, float height) {
        this.cg_name = cg_name;
        this.width = width;
        this.height = height;
    }

    public String getCg_name() {
        return cg_name;
    }

    public void setCg_name(String cg_name) {
        this.cg_name = cg_name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
