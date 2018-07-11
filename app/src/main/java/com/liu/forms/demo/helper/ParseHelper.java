package com.liu.forms.demo.helper;

import com.liu.forms.demo.bean.WorkContentBean;
import com.liu.forms.demo.bean.WorkReportBean;
import com.liu.forms.demo.bean.ProjectBean;

import java.util.ArrayList;
import java.util.List;

public class ParseHelper {

    private static List<ProjectBean> getGroupDatas() {
        List<ProjectBean> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            List<WorkContentBean> childList = new ArrayList<>();
            ProjectBean bean = new ProjectBean();
            bean.setItemId(i);
            bean.setItemType(WorkReportBean.TYPE_PROJECT);
            bean.setTitle("项目编号: " + i);
            for (int j = 0; j < 1; j++) {
                WorkContentBean bean1 = new WorkContentBean();
                bean1.setTitle("项目编号: " + i + " 内容编号: " + j);
                bean1.setItemType(WorkReportBean.TYPE_WORK_CONTENT);
                bean1.setGroupId(i);//child的groupId对应当前
                bean1.setItemId(bean.getItemId());//child的itemId和其父group的itemId一致
                childList.add(bean1);
            }
            bean.setContentList(childList);
            list.add(bean);
        }
        return list;
    }

    public static List<WorkReportBean> getParseDatas() {
        List<WorkReportBean> list = new ArrayList<>();
        for (ProjectBean bean : getGroupDatas()) {
            list.add(bean);//group
            for (WorkContentBean bean1 : bean.getContentList()) {
                list.add(bean1);//child
            }
        }
        return list;
    }

    public static List<WorkReportBean> addProjectAndContent(List<WorkReportBean> reportList) {
        int projectSize = 0;
        for (WorkReportBean b : reportList) {
            if (b.getItemType() == WorkReportBean.TYPE_PROJECT) {
                projectSize++;
            }
        }
        List<ProjectBean> projectList = new ArrayList<>();
        List<WorkContentBean> childList = new ArrayList<>();
        ProjectBean project = new ProjectBean();
        project.setItemId(projectSize + 1);
        project.setItemType(WorkReportBean.TYPE_PROJECT);
        project.setTitle("项目编号: " + projectSize + 1);
        WorkContentBean content = new WorkContentBean();
        content.setTitle("项目编号: " + project.getItemId() + " 内容编号: " + 0);
        content.setItemType(WorkReportBean.TYPE_WORK_CONTENT);
        content.setGroupId(project.getItemId());//child的groupId对应当前
        content.setItemId(project.getItemId());//child的itemId和其父group的itemId一致
        childList.add(content);
        project.setContentList(childList);
        projectList.add(project);
        for (ProjectBean bean : projectList) {
            reportList.add(bean);//group
            for (WorkContentBean bean1 : bean.getContentList()) {
                reportList.add(bean1);//child
            }
        }
        return reportList;
    }

    /**
     * 获取group下的child list
     *
     * @param beans    整个数据list
     * @param position 当前group的position
     */
    public static List<WorkContentBean> getChildList(List<WorkReportBean> beans, int position) {
        List<WorkContentBean> childList = new ArrayList<>();
        for (WorkReportBean bean : beans) {
            //item id不相同直接跳过
            if (bean.getItemId() != beans.get(position).getItemId())
                continue;
            if (bean.getItemType() == WorkReportBean.TYPE_WORK_CONTENT) {
                childList.add((WorkContentBean) bean);
            }
        }
        return childList;
    }

    /**
     * 取出list中的groupBean
     *
     * @param beans
     * @param itemId
     * @return
     */
    public static ProjectBean getGroupBean(List<WorkReportBean> beans, int itemId) {
        for (WorkReportBean bean : beans) {
            if (bean.getItemType() == WorkReportBean.TYPE_PROJECT && bean.getItemId() == itemId)
                return (ProjectBean) bean;
        }
        return null;
    }

    /**
     * 根据itemId获取child所在的group的position
     *
     * @param beans  整个数据list
     * @param itemId child的itemId
     * @return group的position
     */
    public static int getGroupPosition(List<WorkReportBean> beans, int itemId) {
        for (int i = 0; i < beans.size(); i++) {
            if (beans.get(i).getItemType() == WorkReportBean.TYPE_PROJECT
                    && beans.get(i).getItemId() == itemId)
                return i;
        }
        return 0;
    }

    public static ProjectBean newGroupItem(int itemId) {
        List<WorkContentBean> list = new ArrayList<>();
        ProjectBean bean = new ProjectBean();
        bean.setItemId(itemId);
        bean.setItemType(WorkReportBean.TYPE_PROJECT);
        bean.setTitle("项目编号: " + itemId);
        bean.setContentList(list);
        return bean;
    }

    /**
     * 添加工作内容
     *
     * @param beans
     * @param itemId
     * @param childId
     * @return
     */
    public static WorkContentBean newChildItem(List<WorkReportBean> beans, int itemId, int childId) {
        ProjectBean projectBean = getGroupBean(beans, itemId);
        WorkContentBean bean = new WorkContentBean();
        bean.setGroupId(itemId);
        bean.setItemId(itemId);
        bean.setItemType(WorkReportBean.TYPE_WORK_CONTENT);
        bean.setTitle("项目编号: " + itemId + " 内容编号: " + childId);
        if (projectBean != null) {
            projectBean.getContentList().add(bean);
        }
        return bean;
    }
}
