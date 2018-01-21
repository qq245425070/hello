package com.houde.competitive.lagua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.contrart.recommend.RecommendContract;
import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.houde.competitive.lagua.persenter.RecommendPersenter;
import com.houde.competitive.lagua.ui.adapter.RecommendAdapter;
import com.houde.competitive.lagua.widget.RecommendDecoration;
import com.zyw.horrarndoo.sdk.base.BasePresenter;
import com.zyw.horrarndoo.sdk.base.fragment.BaseMVPCompatFragment;
import com.zyw.horrarndoo.sdk.base.fragment.BaseRecycleFragment;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends BaseRecycleFragment<RecommendContract.RecommendPersenter,RecommendContract.IRecommendModel> implements RecommendContract.IRecommendView{

    public static ArticlesFragment newInstance() {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private RecommendAdapter mAdapter;
    private List<RecommendBean> mBeanList;
    private RecyclerView mRecyclerView;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeanList = new ArrayList<>();
        mAdapter = new RecommendAdapter(mBeanList,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecommendDecoration(getContext()));
        mPresenter.getData();
    }


    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return new RecommendPersenter();
    }

    @Override
    protected void onErrorViewClick(View view) {
        mPresenter.getData();
    }

    @Override
    protected void showLoading() {
        mAdapter.setEmptyView(loadingView);
    }

    @Override
    public void setAdapter(List<RecommendBean> beanList) {
        mBeanList = beanList;
        mAdapter = new RecommendAdapter(beanList,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showErrorView() {
        mAdapter.setEmptyView(errorView);
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
}
