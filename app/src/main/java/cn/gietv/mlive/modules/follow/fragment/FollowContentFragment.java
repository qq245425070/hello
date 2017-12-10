package cn.gietv.mlive.modules.follow.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.adapter.FollowPagerAdapter;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.model.GameModel;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.FollowTitlePopuWindow;
import cn.gietv.mlive.views.SubscribeGuidePopuWindow;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/8/31.
 */
public class FollowContentFragment extends AbsBaseFragment {
    private View mRootView;
    private ViewPager mPager;
    private SlidingTabLayout magicIndicator2;
    private GameModel mGameModel;
    private List<GameInfoBean.GameInfoEntity> mGameList;
    private ImageView mImageView;
    private int[] location = new int[2];
    private FollowTitlePopuWindow mPopuWindow;
    private LinearLayout mLoginParent;
    private TextView mLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_follow_content,null);
        Log.e("ceshi","FollowContentFragment:onCreateView");
        mLoginParent = (LinearLayout)mRootView.findViewById(R.id.login_parent);
        mLogin = (TextView) mRootView.findViewById(R.id.login_Text);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getActivity(), LoginActivity.class);
            }
        });
        HeadViewController.initSearchHeadWithoutReturn(mRootView, getActivity(), "订阅");
        mPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        magicIndicator2 = (SlidingTabLayout) mRootView.findViewById(R.id.tab_indicator);
        magicIndicator2.setDistributeEvenly(false);
        mImageView = (ImageView) mRootView.findViewById(R.id.game_more);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPopuWindow == null){
                    mPager.getLocationInWindow(location);
                    mPopuWindow = new FollowTitlePopuWindow(getActivity(),mGameList);
                    mPopuWindow.setOnItemClickListener(new FollowTitlePopuWindow.ItemClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            mPager.setCurrentItem(position+1);
                        }
                    });
                    mPopuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    mPopuWindow.setHeight(DensityUtil.dip2px(getActivity(),360));
                    mPopuWindow.setFocusable(true);
                    mPopuWindow.setOutsideTouchable(true);
                    mPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                mPopuWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, location[0], location[1]);
            }
        });
        mGameModel = RetrofitUtils.create(GameModel.class);
        getData();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SharedPreferenceUtils.getBoolean(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_FOLLOW_PAGE,true)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGuideWindow();
                }
            }, 100);
        }
    }

    private void showGuideWindow(){
        SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_FOLLOW_PAGE,false);
        SubscribeGuidePopuWindow popuWindow = new SubscribeGuidePopuWindow(getActivity());
        popuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setOutsideTouchable(true);
        popuWindow.setFocusable(true);
        popuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popuWindow.showAtLocation(mRootView,Gravity.NO_GRAVITY,0,0);
    }
    public void getData() {
        if(UserUtils.isNotLogin()){
            if(SharedPreferenceUtils.getBoolean(ConfigUtils.FIRST_SEE_FOLLOW_PAGE,true)) {
                mLoginParent.setVisibility(View.GONE);
                List<GameInfoBean.GameInfoEntity> games = DBUtils.getInstance(getActivity()).getAllArea();
                if (games == null) {
                    noLoginNoFollow();
                } else {
                    mGameList = games;
                    GameInfoBean.GameInfoEntity game = new GameInfoBean.GameInfoEntity();
                    game._id = "all";
                    game.name = "全部";
                    games.add(0, game);
                    mPager.setAdapter(new FollowPagerAdapter(getChildFragmentManager(), games));
                    magicIndicator2.setViewPager(mPager);
                    mPager.setCurrentItem(0);
                }
                SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_SEE_FOLLOW_PAGE,false);
            }else {
                mLoginParent.setVisibility(View.VISIBLE);
            }
        }else{
            mLoginParent.setVisibility(View.GONE);
            mGameModel.getAttentionAreaListNoEmpty(CacheUtils.getCacheUserInfo()._id,100,1,CommConstants.DEFAULT_SORT_ONLINE_PERSON,new DefaultLiveHttpCallBack<GameInfoBean>(){
                @Override
                public void success(GameInfoBean gameInfoBean) {
                    if(isNotFinish()){
                        if(gameInfoBean == null)
                            return;
                        mGameList = gameInfoBean.games;
                        setAdapter();
                    }
                }

                @Override
                public void failure(String message) {
                    if(isNotFinish())
                        ToastUtils.showToast(getActivity(), message);
                }
            });
        }
    }

    private void setAdapter() {
        GameInfoBean.GameInfoEntity game = new GameInfoBean.GameInfoEntity();
        game._id = "all";
        game.name = "全部";
        mGameList.add(0,game);
        mPager.setAdapter(new FollowPagerAdapter(getChildFragmentManager(),mGameList));
        magicIndicator2.setViewPager(mPager);
        mPager.setCurrentItem(0);
    }

    public void noLoginNoFollow() {
        mGameModel.getAlbums("game", CommConstants.COMMON_PAGE_COUNT, 1, 100, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean infoBean) {
                if (isNotFinish()) {
                    if(infoBean == null)
                        return;
                    mGameList = infoBean.games;
                   setAdapter();
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(getActivity(), message);
                }
            }
        });
    }
}
