package cn.gietv.mlive.modules.usercenter.fragment;

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
import cn.gietv.mlive.modules.follow.model.FollowModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.usercenter.adapter.UserSubscribeAdapter;
import cn.gietv.mlive.utils.NumUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/9/23.
 */
public class UserSubscribeFragment extends AbsBaseFragment {
    private View mRootView;
    private XRecyclerView mRecyclerVew;
    private FollowModel follow;
    private String mId;
    private int mType;
    private int mCurrentPage = 1;
//    private List<GameInfoBean.GameInfoEntity> mGameData = new ArrayList<>();
//    private List<UserCenterBean.UserinfoEntity> mUserData = new ArrayList<>();
    List<Object> objects = new ArrayList<>();
//    private List<String> mTagData = new ArrayList<>();
    private UserSubscribeAdapter mAdapter;
    public static UserSubscribeFragment getInstance(String id,int type){
        UserSubscribeFragment fragment = new UserSubscribeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category2,null);
        mRecyclerVew = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        mId = getArguments().getString("id");
        mType = getArguments().getInt("type");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerVew.setLayoutManager(layoutManager);
        mRecyclerVew.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerVew.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerVew.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerVew.setPullRefreshEnabled(false);
        follow = RetrofitUtils.create(FollowModel.class);
        mRecyclerVew.setLoadingListener(new XRecyclerView.LoadingListener() {
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
    private boolean flag = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(flag)
                getData();
        } else {
            onPause();
        }
    }

    private void setPaging(int count){
        mRecyclerVew.refreshComplete();
        if (NumUtils.getPage(count) == mCurrentPage) {
            mRecyclerVew.setLoadingMoreEnabled(false);
        } else if (NumUtils.getPage(count) < mCurrentPage) {
            mRecyclerVew.setLoadingMoreEnabled(true);
            mCurrentPage++;
        }
    }
    private void getData(){
        flag = true;
        follow.getFollowsByUserId2(mId, mType, CommConstants.COMMON_PAGE_COUNT, mCurrentPage, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {

                switch (mType){
                    case CommConstants.CAROUSEL_TYPE_ALBUM:
                    case CommConstants.CAROUSEL_TYPE_AREA:
                        if(isNotFinish()) {
                            if (gameInfoBean == null)
                                return;
                            if(gameInfoBean.games == null)
                                return;
                            setPaging(gameInfoBean.cnt);
                            if(mAdapter == null) {
                                objects.clear();
                                objects.addAll(gameInfoBean.games);
                                mAdapter = new UserSubscribeAdapter(getContext(), objects);
                                mRecyclerVew.setAdapter(mAdapter);
                            }else{
                                objects.clear();
                                objects.addAll(gameInfoBean.games);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case CommConstants.CAROUSEL_TYPE_ANCHOR:
                        setPaging(gameInfoBean.cnt);
                        if (gameInfoBean == null || gameInfoBean.users == null)
                            return;
                        if(mAdapter == null) {
                            objects.clear();
                            objects.addAll(gameInfoBean.users);
                            mAdapter = new UserSubscribeAdapter(getContext(),objects);
                            mRecyclerVew.setAdapter(mAdapter);
                        }else {
                            objects.clear();
                            objects.addAll(gameInfoBean.users);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case CommConstants.TYPE_TAG:
                        setPaging(gameInfoBean.cnt);
                        if (gameInfoBean == null || gameInfoBean.tags == null)
                            return;
                        if(mAdapter == null) {
                            objects.clear();
                            objects.addAll(gameInfoBean.tags);
                            mAdapter = new UserSubscribeAdapter(getContext(), objects);
                            mRecyclerVew.setAdapter(mAdapter);
                        }else{
                            objects.clear();
                            objects.addAll(gameInfoBean.tags);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    ToastUtils.showToastShort(getActivity(),message);
                }
            }
        });
    }
}
