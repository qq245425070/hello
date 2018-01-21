package com.houde.competitive.lagua.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.contrart.recommend.SatinsContract;
import com.houde.competitive.lagua.model.bean.SatinsBean;
import com.houde.competitive.lagua.persenter.SatinsPresenter;
import com.houde.competitive.lagua.ui.adapter.SatinsAdapter;
import com.houde.competitive.lagua.widget.RecommendDecoration;
import com.zyw.horrarndoo.sdk.base.BasePresenter;
import com.zyw.horrarndoo.sdk.base.fragment.BaseMVPCompatFragment;
import com.zyw.horrarndoo.sdk.base.fragment.BaseRecycleFragment;

import java.util.ArrayList;
import java.util.List;

public class SatinsFragment extends BaseRecycleFragment<SatinsPresenter,SatinsContract.ISatinsModel> implements SatinsContract.ISatinsView {

    private RecyclerView mRecyclerView;
    private List<SatinsBean> mBeanList;
    private SatinsAdapter mAdapter;
    public static SatinsFragment newInstances(){
        SatinsFragment fragment =  new SatinsFragment();
        return fragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeanList = new ArrayList<>();
        mAdapter = new SatinsAdapter(mBeanList,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecommendDecoration(getContext()));
        mPresenter.getData();
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return new SatinsPresenter();
    }

    @Override
    public void updateContentList(List<SatinsBean> list) {
        mBeanList = list;
        mAdapter = new SatinsAdapter(mBeanList,this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void showNetworkError() {
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    public void showLoadMoreError() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void showNoMoreData() {
        mAdapter.setEnableLoadMore(true);
    }

    @Override
    protected void onErrorViewClick(View view) {
        mPresenter.getData();
    }

    @Override
    protected void showLoading() {
        mAdapter.setEmptyView(loadingView);
    }
}
