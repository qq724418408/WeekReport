package com.liu.forms.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liu.forms.demo.bean.WorkContentBean;
import com.liu.forms.demo.bean.WorkReportBean;
import com.liu.forms.demo.bean.ProjectBean;
import com.liu.forms.demo.helper.ParseHelper;
import com.liu.forms.demo.utils.ToastUtil;
import com.liu.forms.nestingrecyclerviewdemo.R;

import java.util.List;

public class WeekReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WorkReportBean> list;
    private Context mContext;

    public WeekReportAdapter(Context context, List<WorkReportBean> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case WorkReportBean.TYPE_PROJECT:
                View v1 = mInflater.inflate(R.layout.item_project_list, parent, false);
                holder = new GroupViewHolder(v1);
                break;
            case WorkReportBean.TYPE_WORK_CONTENT:
                View v2 = mInflater.inflate(R.layout.item_work_content_list, parent, false);
                holder = new ChildViewHolder(v2);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GroupViewHolder) {
            GroupViewHolder gHolder = (GroupViewHolder) holder;
            final ProjectBean bean = (ProjectBean) list.get(position);
            gHolder.bindData(bean);
            gHolder.tvGroup.setText(list.get(position).getTitle());
            gHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int contentSize = 1;
                    for (WorkReportBean b : list) {
                        if (b.getItemType() == WorkReportBean.TYPE_WORK_CONTENT) {
                            if (bean.getItemId() == b.getItemId()) {
                                contentSize++;
                            }
                        }
                    }
                    ToastUtil.showToast("项目编号:" + bean.getItemId() + ",size:" + contentSize);
                    // 添加内容
                    addChild(position + contentSize, bean.getItemId());
                }
            });
        } else if (holder instanceof ChildViewHolder) {
            ChildViewHolder cHolder = (ChildViewHolder) holder;
            final WorkContentBean bean = (WorkContentBean) list.get(position);
            cHolder.bindData(bean);
            cHolder.tvChild.setText(list.get(position).getTitle());
            cHolder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int projectSize = 0;
                    ProjectBean project = null;
                    int itemId = bean.getItemId();
                    for (WorkReportBean b : list) {
                        if (b.getItemType() == WorkReportBean.TYPE_PROJECT) {
                            // 找出所有项目，记录个数
                            projectSize++;
                            if (itemId == b.getItemId()) {
                                // 找到对应的项目
                                project = (ProjectBean) b;
                            }
                        }
                    }
                    if (null == project) {
                        return;
                    }
                    int contentSize = 0;
                    for (WorkReportBean b : list) {
                        if (itemId == b.getItemId()) {
                            if (b.getItemType() == WorkReportBean.TYPE_WORK_CONTENT) {
                                contentSize++;
                            }
                        }
                    }
                    Log.e(TAG, "onClick: 项目编号" + itemId + "的内容条数：" + contentSize);
                    // 如果内容条数大于1，正常删除内容
                    if (contentSize > 1) {
                        // 删除内容
                        removeSelected(bean);
                        Log.e(TAG, "onClick: 正常删除内容");
                    } else {
                        // 如果内容小于等于1，如果项目个数大于1，正常删除内容，且删除项目
                        if (projectSize > 1) {
                            Log.e(TAG, "onClick: 所剩项目个数大于1，删除最后一条内容，删除项目");
                            // 删除内容
                            removeSelected(bean);
                            // 删除项目
                            removeSelected(project);
                        } else {
                            Log.e(TAG, "onClick: 项目个数小于等于1，不常删除内容，保留最后一个项目和一条内容");
                            // 如果内容小于等于1，如果项目个数小于等于1，不常删除内容，保留最后一个项目和一条内容
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * 删除选中item
     */
    public void removeSelected(WorkReportBean bean) {
        list.remove(bean);
        notifyDataSetChanged();
    }

    /**
     * 添加项目
     */
    public void addProjectAndContent() {
        int addPosition = list.size();
        int itemId = 0;
        if (isHaveGroup()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItemType() == WorkReportBean.TYPE_PROJECT) {
                    itemId = list.get(i).getItemId() + 1;
                }
            }
        }
        list.add(addPosition, ParseHelper.newGroupItem(itemId));
        WorkContentBean content = new WorkContentBean();
        content.setTitle("项目编号: " + itemId + " 内容编号: " + 0);
        content.setItemType(WorkReportBean.TYPE_WORK_CONTENT);
        content.setGroupId(itemId);//child的groupId对应当前
        content.setItemId(itemId);//child的itemId和其父group的itemId一致
        list.add(content);
        notifyItemInserted(addPosition);//通知演示插入动画
        notifyDataSetChanged();
        //notifyItemRangeChanged(addPosition, list.size() - addPosition);//通知数据与界面重新绑定
    }

    private static final String TAG = "WeekReportAdapter";

    /**
     * 添加内容
     */
    public void addChild(int addPosition, int itemId) {
        int childId = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemId() == itemId && list.get(i).getItemType() == WorkReportBean.TYPE_WORK_CONTENT) {
                childId++;
            }
        }
        Log.e(TAG, "addChild: 项目编号：" + itemId);
        list.add(addPosition, ParseHelper.newChildItem(list, itemId, childId));
        notifyItemInserted(addPosition);//通知演示插入动画
        notifyDataSetChanged();
        //notifyItemRangeChanged(addPosition, list.size() - addPosition);//通知数据与界面重新绑定
    }

    /**
     * 当前list是否含有group
     *
     * @return 当前list是否含有group
     */
    private boolean isHaveGroup() {
        boolean isHaveGroup = false;
        //当前列表是否包含group
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemType() == WorkReportBean.TYPE_PROJECT) {
                isHaveGroup = true;
                break;
            }
        }
        return isHaveGroup;
    }


    /**
     * 获取最后一个item的position
     *
     * @return 最后一个item的position
     */
    public int getLastItemPosition() {
        return list.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private ProjectBean bean;
        public TextView tvGroup;
        public TextView ivAdd;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tvGroup = (TextView) itemView.findViewById(R.id.tv_group);
            ivAdd = (TextView) itemView.findViewById(R.id.ivAdd);
        }

        /**
         * 绑定item数据
         *
         * @param bean item数据
         */
        public void bindData(ProjectBean bean) {
            this.bean = bean;
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private WorkContentBean bean;
        public TextView tvChild;
        public TextView ivDel;

        public ChildViewHolder(View itemView) {
            super(itemView);
            tvChild = (TextView) itemView.findViewById(R.id.tv_child);
            ivDel = (TextView) itemView.findViewById(R.id.ivDel);
        }

        /**
         * 绑定item数据
         *
         * @param bean item数据
         */
        public void bindData(WorkContentBean bean) {
            this.bean = bean;
        }
    }
}
