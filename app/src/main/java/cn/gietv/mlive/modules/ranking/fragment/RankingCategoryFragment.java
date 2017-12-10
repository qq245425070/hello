package cn.gietv.mlive.modules.ranking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.ranking.adapter.RankingCategoryAdapter;
import cn.gietv.mlive.modules.ranking.model.FastEntryModel;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * 作者：houde on 2016/11/23 15:46
 * 邮箱：yangzhonghao@gietv.com
 */
public class RankingCategoryFragment extends AbsBaseFragment {
    private View mRootView;
    private XRecyclerView mRecyclerView;
    private RankingCategoryAdapter mAdatper;
    private List<ProgramBean.ProgramEntity> mProgramList;
    private String mID;
    private FastEntryModel model;
    private int mCurrentPager;
    private String ctype ;
    public static RankingCategoryFragment getRankingCategoryFragment(String id,String ctype){
        RankingCategoryFragment fragment = new RankingCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("ctype",ctype);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2,null);
        mID = getArguments().getString("id");
        model = RetrofitUtils.create(FastEntryModel.class);
        ctype = getArguments().getString("ctype");
        if(TextUtils.isEmpty(ctype)){
            ctype = "play";
        }
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        getData();
        return mRootView;
    }

    private void loadMore() {

        model.queryProgrambyareaidandtype(mID, ctype, CommConstants.COMMON_PAGE_COUNT, mCurrentPager, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
            @Override
            public void success(ProgramBean programBean) {
                if(isNotFinish()) {
                    mRecyclerView.loadMoreComplete();
                    if (programBean == null || programBean.programs == null)
                        return;
                    if(NumUtils.getPage(programBean.cnt,10) > mCurrentPager){
                        mRecyclerView.setLoadingMoreEnabled(true);
                        mCurrentPager ++;
                    }else if(NumUtils.getPage(programBean.cnt,10) <= mCurrentPager){
                        mRecyclerView.setLoadingMoreEnabled(false);
                    }
                    mProgramList.addAll(programBean.programs);
                    mAdatper.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    mRecyclerView.loadMoreComplete();
                    ToastUtils.showToastShort(getActivity(),message);
                }
            }
        });
    }

    public void getData() {
        mCurrentPager = 1;
        model.queryProgrambyareaidandtype(mID, ctype, CommConstants.COMMON_PAGE_COUNT, mCurrentPager, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
            @Override
            public void success(ProgramBean programBean) {
                if(isNotFinish()) {
                    mRecyclerView.loadMoreComplete();
                    if (programBean == null || programBean.programs == null)
                        return;
                    if(NumUtils.getPage(programBean.cnt,10) > mCurrentPager){
                        mRecyclerView.setLoadingMoreEnabled(true);
                        mCurrentPager ++;
                    }else if(NumUtils.getPage(programBean.cnt,10) <= mCurrentPager){
                        mRecyclerView.setLoadingMoreEnabled(false);
                    }
                    mProgramList = programBean.programs;
                    mAdatper = new RankingCategoryAdapter(getActivity(),mProgramList);
                    mRecyclerView.setAdapter(mAdatper);
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    mRecyclerView.loadMoreComplete();
                    ToastUtils.showToastShort(getActivity(),message);
                }
            }
        });
    }
}
