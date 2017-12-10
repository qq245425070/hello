package cn.gietv.mlive.modules.usercenter.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.adapter.RechargeRecordAdapter;
import cn.gietv.mlive.modules.usercenter.bean.RechargeRecordBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

public class RechargeRecordActivity extends AbsBaseActivity {

    private UserCenterModel model;
    private XRecyclerView mListView;
    private RechargeRecordAdapter mAdapter;
    private int mCurrentPage;
    private TextView mEmpyText;
    private List<RechargeRecordBean.RecordBean> mRechargeRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        HeadViewController.initHeadWithoutSearch(this, "充值记录");
        mListView = (XRecyclerView) findViewById(R.id.recyclerview);
        mEmpyText = (TextView) findViewById(R.id.empty_text);
        model = RetrofitUtils.create(UserCenterModel.class);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(manager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        onPullDownToRefresh();
        mRechargeRecordList = new ArrayList<>();
        mAdapter = new RechargeRecordAdapter(RechargeRecordActivity.this,mRechargeRecordList);
        mListView.setAdapter(mAdapter);
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                onPullDownToRefresh();
            }

            @Override
            public void onLoadMore() {
                onPullUpToRefresh();
            }
        });
    }

    public void onPullDownToRefresh() {
        mCurrentPage = 1;
        model.rechargeRecord(CommConstants.COMMON_PAGE_COUNT,mCurrentPage,CommConstants.DEFAULT_SORT_ONLINE_PERSON,new DefaultLiveHttpCallBack<RechargeRecordBean>() {
            @Override
            public void success(RechargeRecordBean bean) {
                if(bean.cnt == 0 ){
                    mEmpyText.setVisibility(View.VISIBLE);
                }
                if(bean == null)
                    return;
                if(isNotFinish()){
                    if (NumUtils.getPage(bean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(bean.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    mRechargeRecordList.clear();
                    mRechargeRecordList.addAll(bean.recharges);
                    mAdapter.notifyDataSetChanged();
                    mListView.refreshComplete();
                }


            }

            @Override
            public void failure(String message) {
                if(isNotFinish()) {
                    ToastUtils.showToast(RechargeRecordActivity.this, message);
                    mListView.refreshComplete();
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        model.rechargeRecord(CommConstants.COMMON_PAGE_COUNT,mCurrentPage,CommConstants.DEFAULT_SORT_ONLINE_PERSON,new DefaultLiveHttpCallBack<RechargeRecordBean>() {
            @Override
            public void success(RechargeRecordBean bean) {
                if(isNotFinish()){
                    if (NumUtils.getPage(bean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(bean.cnt) > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    mRechargeRecordList.addAll(bean.recharges);
                    mAdapter.notifyDataSetChanged();
                    mListView.loadMoreComplete();
                }


            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    ToastUtils.showToast(RechargeRecordActivity.this, message);
                }
            }
        });
    }
}
