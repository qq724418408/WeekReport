package com.liu.forms.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.liu.forms.demo.adapter.WeekReportAdapter;
import com.liu.forms.demo.bean.WorkReportBean;
import com.liu.forms.nestingrecyclerviewdemo.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeekReportAdapter adapter;
    private List<WorkReportBean> list;
    private Button tvAddProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        list = WeekReportAdapter.getWorkReportList();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvAddProject = (Button) findViewById(R.id.tvAddProject);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeekReportAdapter(this, list);
        recyclerView.setAdapter(adapter);
        tvAddProject.setOnClickListener(new OnClickListener());
        adapter.setListener(new WeekReportAdapter.Listener() {
            @Override
            public void addContentListener(int addPosition, int itemId) {
                adapter.addChild(addPosition, itemId);
                recyclerView.smoothScrollToPosition(addPosition);
            }
        });
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAddProject:
                    adapter.addProjectAndContent();
                    recyclerView.smoothScrollToPosition(adapter.getLastItemPosition());
                    break;
                default:
                    break;
            }
        }
    }
}
