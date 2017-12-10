package cn.gietv.mlive.modules.follow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.adapter.AreaAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ToastUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/8/24.
 */
public class FollowFragment extends AbsBaseFragment {
    private View mRootView;
    private LinearLayout mLoginParent;
    private TextView mLoginText;
    private XRecyclerView mRecyclerView;
    private GameModel mGameModel;
    private AreaAdapter mAdapter;
    private List<GameInfoBean.GameInfoEntity> mGameList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_follow,container,false);
//        mPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
//        magicIndicator2 = (PagerTab) mRootView.findViewById(R.id.tab_indicator);
        HeadViewController.initSearchHeadWithoutReturn(mRootView, getActivity(), "关注");
        mLoginParent = (LinearLayout) mRootView.findViewById(R.id.no_login_parent);
        mRecyclerView = (XRecyclerView) mRootView.findViewById(R.id.recyclerview);
        mLoginText = (TextView) mRootView.findViewById(R.id.login_btn);
        mGameModel = RetrofitUtils.create(GameModel.class);
        mAdapter = new AreaAdapter(getActivity(),mGameList);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mLoginParent.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mGameModel.getAlbums("game", CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean infoBean) {
                if (isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    if(infoBean == null)
                        return;
                    mRecyclerView.setLoadingMoreEnabled(false);
                    mGameList.clear();
                    mGameList.addAll(infoBean.games);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    mRecyclerView.refreshComplete();
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment,new FollowContentFragment());
        ft.commitAllowingStateLoss();
    }

}
