package com.houde.competitive.lagua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.contrart.recommend.RecommendContract;
import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.houde.competitive.lagua.ui.adapter.RecommendAdapter;
import com.houde.competitive.lagua.widget.RecommendDecoration;
import com.zyw.horrarndoo.sdk.base.BasePresenter;
import com.zyw.horrarndoo.sdk.base.fragment.BaseRecycleFragment;

import java.util.List;

import butterknife.BindView;


public class RecommendFragment extends BaseRecycleFragment<RecommendContract.RecommendPresenter,RecommendContract.IRecommendModel> implements RecommendContract.IRecommendView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<RecommendBean> mBeanList;
    private RecommendAdapter mAdapter;
    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecommendAdapter(mBeanList,this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecommendDecoration(getContext()));
        mPresenter.getData();
    }


    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return new com.houde.competitive.lagua.persenter.RecommendPresenter();
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void setAdapter( List<RecommendBean> beanList) {
        mBeanList = beanList;
        mAdapter = new RecommendAdapter(beanList,this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.onItemClick(position,mBeanList.get(position),getContext());
            }
        });
    }

    @Override
    public void showErrorView() {
        if(mAdapter != null){
            mAdapter.setEmptyView(emptyView);
        }
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
        mAdapter.loadMoreEnd();
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
