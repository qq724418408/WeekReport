package com.liu.forms.demo.bean;

import java.util.List;

public class ProjectBean extends WorkReportBean {

    private List<WorkContentBean> contentList;

    public List<WorkContentBean> getContentList() {
        return contentList;
    }

    public void setContentList(List<WorkContentBean> contentList) {
        this.contentList = contentList;
    }
}
