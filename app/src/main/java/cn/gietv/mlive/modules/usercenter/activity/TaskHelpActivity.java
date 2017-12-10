package cn.gietv.mlive.modules.usercenter.activity;

import android.os.Bundle;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.utils.HeadViewController;

public class TaskHelpActivity extends AbsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_help);
        HeadViewController.initHeadWithoutSearch(this,"帮助");
    }
}
