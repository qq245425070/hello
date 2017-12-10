package cn.gietv.mlive.modules.category.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.category.adapter.RecommendAdapter;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.recommend.model.RecommendModel;
import cn.gietv.mlive.parallaxheaderviewpager.RecyclerViewFragment;
import cn.gietv.mlive.utils.CacheUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/8/17.
 */
public class RecommendFragment extends RecyclerViewFragment {
    private View mRootView;
    private List<Object> list;
    private RecommendAdapter mListAdapter;
    private RecommendModel mModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("ceshi","RecommendFragment:RecommendFragment");
        mRootView = inflater.inflate(R.layout.fragment_category2,container, false);
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        mPosition = getArguments().getInt(ARG_POSITION);
        mLayoutMgr = new LinearLayoutManager(getActivity());
        mLayoutMgr.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutMgr);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mModel = RetrofitUtils.create(RecommendModel.class);
        if(list == null) {
            list = new ArrayList<>();
        }
        if (mListAdapter == null) {
            mListAdapter = new RecommendAdapter(getActivity(), list);
            mRecyclerView.setAdapter(mListAdapter);
            setRecyclerViewOnScrollListener();
        } else {
            mListAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        return mRootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mListAdapter!=null){
            mListAdapter.i=0;

        }
    }

    private void getData() {
        String userid = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
        String token = CacheUtils.getCache().getAsString(CacheConstants.CACHE_TOKEN);
        mModel.getRecommendData(userid, token, new DefaultLiveHttpCallBack<RecommendBean>() {
            @Override
            public void success(RecommendBean recommendBean) {
                if (isNotFinish()) {
                    list.clear();
                    loadDataSuccess(recommendBean);
                    mRecyclerView.refreshComplete();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mRecyclerView.refreshComplete();
                }
            }
        });

    }
    private void loadDataSuccess(RecommendBean recommendBean) {
        if (isNotFinish()) {
            if (recommendBean == null) {
                return;
            }
            if (recommendBean.carousels != null && recommendBean.carousels.size() > 0) {
                list.add(recommendBean.carousels);
            }
            if (recommendBean.programsbygameid != null && recommendBean.programsbygameid.size() != 0) {
                list.addAll(recommendBean.programsbygameid);
            }
            if (recommendBean.follow != null && recommendBean.follow.size() != 0) {
                list.addAll(recommendBean.follow);
            }
            mListAdapter.notifyDataSetChanged();
            setRecyclerViewOnScrollListener();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("ceshi","onPause");
    }

    public void setData(List<Object> data) {
        list = data;
    }

    private LinearLayoutManager mLayoutMgr ;
    @Override
    protected void setScrollOnLayoutManager(int scrollY) {
        mLayoutMgr.scrollToPositionWithOffset(0, -scrollY);

    }
}
