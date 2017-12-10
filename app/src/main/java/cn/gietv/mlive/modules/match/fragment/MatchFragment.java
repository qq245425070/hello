package cn.gietv.mlive.modules.match.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.match.adapter.MatchAdapter;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 *
 * 作者：houde on 2016/11/23 15:46
 * 邮箱：yangzhonghao@gietv.com
 */
public class MatchFragment extends AbsBaseFragment {
    private View mRootView;
    private XRecyclerView mRecyclerView;
    private MatchAdapter mAdatper;
    private List<ProgramBean.ProgramEntity> mProgramList;
    private String mID;
    private GameModel model;
    private int mCurrentPager = 1;
    public static MatchFragment getMatchFragment(String id){
        MatchFragment fragment = new MatchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2,null);
        mID = getArguments().getString("id");
        model = RetrofitUtils.create(GameModel.class);
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
        model.queryprogramlistbyareaid(mID, "all", 10, mCurrentPager, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
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
        model.queryprogramlistbyareaid(mID, "all", 10, mCurrentPager, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<ProgramBean>() {
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
                    mAdatper = new MatchAdapter(getActivity(),mProgramList);
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
