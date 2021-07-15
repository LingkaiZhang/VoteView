package com.lingkai.library.vote;

import android.text.TextUtils;

public class OptionMenu {
    private String sort;
    private String content;
    private String percentage;
    private int number;

    public OptionMenu(String sort, String content, String percentage, int number) {
        this.sort = sort;
        this.content = content;
        if (TextUtils.isEmpty(percentage)){
            this.percentage = "0";
        } else {
            this.percentage = percentage;
        }
        this.number = number;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
