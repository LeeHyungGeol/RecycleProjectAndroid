package com.example.wasterecycleproject.model;

public class Discharge {
    private int idx;
    private String name;
    private String category_m_name;
    private String content;
    private String item_corresponding;
    private String item_discorresponding;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getCategory_m_name() {
        return category_m_name;
    }

    public void setCategory_m_name(String category_m_name) {
        this.category_m_name = category_m_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getItem_corresponding() {
        return item_corresponding;
    }

    public void setItem_corresponding(String item_corresponding) {
        this.item_corresponding = item_corresponding;
    }

    public String getItem_discorresponding() {
        return item_discorresponding;
    }

    public void setItem_discorresponding(String item_discorresponding) {
        this.item_discorresponding = item_discorresponding;
    }
}
