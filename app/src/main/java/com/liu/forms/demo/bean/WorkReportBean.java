package com.liu.forms.demo.bean;

public class WorkReportBean {

    public static final int TYPE_PROJECT = 1;
    public static final int TYPE_WORK_CONTENT = 2;

    private String title;
    private int itemType;
    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
