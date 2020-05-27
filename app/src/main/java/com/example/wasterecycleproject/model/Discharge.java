package com.example.wasterecycleproject.model;

public class Discharge {
    private int idx;
    private int category_m_idx;
    private String content;
    private String item_corresponding;
    private String item_discorresponding;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getCategory_m_idx() {
        return category_m_idx;
    }

    public void setCategory_m_idx(int category_m_idx) {
        this.category_m_idx = category_m_idx;
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
