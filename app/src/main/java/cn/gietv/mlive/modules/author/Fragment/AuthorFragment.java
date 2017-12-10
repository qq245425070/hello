package cn.gietv.mlive.modules.author.Fragment;

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
import cn.gietv.mlive.modules.author.adapter.AuthorAdapter;
import cn.gietv.mlive.modules.game.bean.AuthorBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * 作者：houde on 2016/11/22 15:27
 * 邮箱：yangzhonghao@gietv.com
 */
public class AuthorFragment extends AbsBaseFragment {
    private View mRootView;
    private XRecyclerView mRecyclerView;
    private AuthorAdapter mAdapter;
    private GameModel gameModel;
    private int mCurrentPager = 1;
    private List<UserCenterBean.UserinfoEntity> mAuthorList;
    private String mID;
    public static AuthorFragment getAuthorFragment(String id){
        AuthorFragment fragment = new AuthorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_fast_author,null);
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingMoreEnabled(false);
        gameModel = RetrofitUtils.create(GameModel.class);
        mID = getArguments().getString("id");
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

    private void getData() {
        gameModel.getAnchors(mID, "all", CommConstants.COMMON_PAGE_COUNT, CommConstants.DEFAULT_SORT_ONLINE_PERSON, mCurrentPager, new DefaultLiveHttpCallBack<AuthorBean>() {
            @Override
            public void success(AuthorBean authorBean) {
                if(isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    if (authorBean == null || authorBean.userinfolist == null)
                        return;
                    if (NumUtils.getPage(authorBean.cnt) == mCurrentPager) {
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(authorBean.cnt) > mCurrentPager) {
                        mRecyclerView.setLoadingMoreEnabled(true);
                        mCurrentPager++;
                    }
                    mAuthorList = authorBean.userinfolist;
                    mAdapter = new AuthorAdapter(getActivity(), mAuthorList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    ToastUtils.showToastShort(getActivity(), message);
                }
            }
        });
    }

    public void loadMore() {
        gameModel.getAnchors(mID, "all", CommConstants.COMMON_PAGE_COUNT, CommConstants.DEFAULT_SORT_ONLINE_PERSON, mCurrentPager, new DefaultLiveHttpCallBack<AuthorBean>() {
            @Override
            public void success(AuthorBean authorBean) {
                if(isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    if (authorBean == null || authorBean.userinfolist == null)
                        return;
                    if (NumUtils.getPage(authorBean.cnt) == mCurrentPager) {
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else if (NumUtils.getPage(authorBean.cnt) > mCurrentPager) {
                        mRecyclerView.setLoadingMoreEnabled(true);
                        mCurrentPager++;
                    }
                    mAuthorList.addAll(authorBean.userinfolist);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    ToastUtils.showToastShort(getActivity(), message);
                }
            }
        });
    }
}
