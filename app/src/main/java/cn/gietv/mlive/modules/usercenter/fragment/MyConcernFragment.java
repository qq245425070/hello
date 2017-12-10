package cn.gietv.mlive.modules.usercenter.fragment;

import android.graphics.Color;
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
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.user.bean.UserBean;
import cn.gietv.mlive.modules.usercenter.adapter.MyFriendsAddapter;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
/**
 * Created by houde on 2016/7/5.
 */
public class MyConcernFragment extends AbsBaseFragment{
    public static final int FLAG_FANS = 0;
    public static final int FLAG_FOLLOW = 1;
    public static MyConcernFragment getInstance(String id,int type,int flag,int isMine){
        MyConcernFragment fragment = new MyConcernFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putInt("type", type);
        bundle.putInt("flag",flag);
        bundle.putInt("isMine",isMine);
        fragment.setArguments(bundle);
        return fragment;
    }
    private XRecyclerView mListView;
    private View mRootView;
    private String mId;
    private int mType;
    private FollowModel follow;
    private int mCurrentPage = 1;
    private int isMine;
    private MyFriendsAddapter mAdapter;
    private List<UserCenterBean.UserinfoEntity> mUserData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2,null);
        mRootView.setBackgroundColor(Color.WHITE);
        mListView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mId = getArguments().getString("id");
        mType = getArguments().getInt("type");
        isMine = getArguments().getInt("isMine");
        follow = RetrofitUtils.create(FollowModel.class);
        mUserData = new ArrayList<>();
        getData();
        mListView.setPullRefreshEnabled(false);
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        Log.e("ceshi","getData");
        if(getArguments().getInt("flag") == FLAG_FOLLOW) {
            follow.getBeFollowUserByUserId(mId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<UserBean>() {
                @Override
                public void success(UserBean userBean) {
                    Log.e("ceshi","getData   FLAG_FOLLOW");
                    if (isNotFinish()) {
                        mListView.loadMoreComplete();
                        if (userBean == null || userBean.users == null)
                            return;
                        if (NumUtils.getPage(userBean.cnt) == mCurrentPage) {
                            mListView.setLoadingMoreEnabled(false);
                        } else if (NumUtils.getPage(userBean.cnt) > mCurrentPage) {
                            mListView.setLoadingMoreEnabled(true);
                            mCurrentPage++;
                        }
                        mUserData.clear();
                        mUserData.addAll(userBean.users);
                        if (mAdapter == null) {
                            mAdapter = new MyFriendsAddapter(getActivity(), mUserData,isMine);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        mListView.loadMoreComplete();
                        ToastUtils.showToastShort(getActivity(), message);

                    }
                }
            });
        }else{
            follow.getFollowUserByUserId(mId, CommConstants.COMMON_PAGE_COUNT, CommConstants.DEFAULT_SORT_ONLINE_PERSON, mCurrentPage, new DefaultLiveHttpCallBack<UserBean>() {
                @Override
                public void success(UserBean userBean) {
                    Log.e("ceshi","getData   . ...");
                    if (isNotFinish()) {
                        mListView.loadMoreComplete();
                        if (NumUtils.getPage(userBean.cnt) == mCurrentPage) {
                            mListView.setLoadingMoreEnabled(false);
                        } else if (NumUtils.getPage(userBean.cnt) > mCurrentPage) {
                            mListView.setLoadingMoreEnabled(true);
                            mCurrentPage++;
                        }
                        if(userBean == null || userBean.users == null){
                            return;
                        }
                        mUserData.clear();
                        mUserData.addAll(userBean.users);
                        if (mAdapter == null) {
                            mAdapter = new MyFriendsAddapter(getActivity(),mUserData,isMine);
                            mListView.setAdapter(mAdapter);
                        } else {
                            Log.e("ceshi","mAdapter.notifyDataSetChanged");
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        mListView.loadMoreComplete();
                        ToastUtils.showToastShort(getActivity(), message);
                    }
                }
            });
        }

    }

}
