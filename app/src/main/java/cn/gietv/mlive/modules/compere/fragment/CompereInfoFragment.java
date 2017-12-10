package cn.gietv.mlive.modules.compere.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.gietv.mlive.modules.compere.adapter.CompereAdapter;
import cn.gietv.mlive.modules.compere.model.CompereModel;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/12 13:01
 */
public class CompereInfoFragment extends AbsBaseFragment implements  View.OnClickListener {
    public static final String EXTRA_USER_ID = "extra_user_id";

    private View mCurrentView;
    private XRecyclerView mListView;

    private CompereAdapter mAdapter;
    private List<Object> mObjectList;
    private CompereModel mCompereModel;

    private int mCurrentPage = 1;
    private String mUserId;
    private View.OnClickListener mOnClickListener;
    private UserCenterBean.UserinfoEntity mUserinfo;

    public static CompereInfoFragment getInstence(String userid) {
        CompereInfoFragment fragment = new CompereInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.compere_layout, container, false);
        mListView = (XRecyclerView) mCurrentView.findViewById(R.id.user_center_lv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mCompereModel = RetrofitUtils.create(CompereModel.class);
        mUserId = getArguments().getString(EXTRA_USER_ID);
        mObjectList = new ArrayList<>();
        mAdapter = new CompereAdapter(getActivity(), mObjectList);

        if (mOnClickListener != null) {
            mCurrentView.findViewById(R.id.head_ib_exit).setOnClickListener(mOnClickListener);
        }
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnabled(false);
        onPullDownToRefresh();
        return mCurrentView;
    }

    @Override
    public void onClick(View view) {

    }


    public void onPullDownToRefresh() {
        mCurrentPage = 1;
        mCompereModel.getCompereByUserId(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<UserCenterBean>() {
            @Override
            public void success(UserCenterBean userCenterBean) {
                if (isNotFinish()) {
                    if (userCenterBean == null) {
                        ToastUtils.showToast(getActivity(), "用户不存在");
                        getActivity().finish();
                        return;
                    }
                    mListView.refreshComplete();
                    if (NumUtils.getPage(userCenterBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (userCenterBean.cnt > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    //HeadViewController.initHeadWithoutSearch(mCurrentView, getActivity(), userCenterBean.userinfo.nickname + "的主页");
                    if (mOnClickListener != null) {
                        mCurrentView.findViewById(R.id.head_ib_exit).setOnClickListener(mOnClickListener);
                    }
                    mObjectList.clear();
                    mObjectList.add(userCenterBean.userinfo);
                    if (userCenterBean.programs != null) {
//                        mAdapter.setData(userCenterBean.programs);
                        mObjectList.addAll(userCenterBean.programs);
                    }
                    mAdapter.notifyDataSetChanged();
                    mUserinfo = userCenterBean.userinfo;
                    HeadViewController.initHeadWithoutSearch(mCurrentView, getActivity(), mUserinfo.nickname);
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.refreshComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }

    public void onPullUpToRefresh() {
        mCompereModel.getCompereByUserId(mUserId, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<UserCenterBean>() {
            @Override
            public void success(UserCenterBean userCenterBean) {
                if (isNotFinish()) {
                    if (NumUtils.getPage(userCenterBean.cnt) == mCurrentPage) {
                        mListView.setLoadingMoreEnabled(false);
                    } else if (userCenterBean.cnt > mCurrentPage) {
                        mListView.setLoadingMoreEnabled(true);
                        mCurrentPage++;
                    }
                    if (userCenterBean.programs != null) {
                        mObjectList.addAll(userCenterBean.programs);
                        mAdapter.notifyDataSetChanged();
                    }
                    mListView.loadMoreComplete();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mListView.loadMoreComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }
}
