package cn.gietv.mlive.modules.recommend.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.album.activity.AlbumActivity;
import cn.gietv.mlive.modules.author.activity.FastEntryAuthorActivity;
import cn.gietv.mlive.modules.category.activity.CategoryActivity2;
import cn.gietv.mlive.modules.common.OnItemClick;
import cn.gietv.mlive.modules.compere.fragment.CompereInfoFragment;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.ranking.activity.RankingActivity;
import cn.gietv.mlive.modules.recommend.adapter.RecommendListAdapter;
import cn.gietv.mlive.modules.recommend.adapter.RecommendPagerAdapter;
import cn.gietv.mlive.modules.recommend.bean.CirculationTimerTask;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.recommend.model.RecommendModel;
import cn.gietv.mlive.modules.vrgame.activity.VRAllGameActivity;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
import me.relex.circleindicator.CircleIndicator;

/**
 * author：steven
 * datetime：15/9/21 22:00
 *
 */
public class RecommendFragment extends AbsBaseFragment {
    private View mCurrentView;
    private XRecyclerView mListView;
    private RecommendModel mModel;
    private int flag;
    private RecommendListAdapter mListAdapter;
    private View mHeaderView;
    private ViewPager mViewPager;
    private RecommendPagerAdapter mPagerAdapter;
    private CircleIndicator mIndicator;
    private List<Object> list = new ArrayList<>();
    private LinearLayout authorParent ;
    private LinearLayout rankingParent ;
    private LinearLayout matchParent ;
    private LinearLayout welfareParent ;
    private LinearLayout mContentParent;
//    public static RecommendFragment openRecommendFragment(String id){
//        RecommendFragment recommendFragment = new RecommendFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("id",id);
//        recommendFragment.setArguments(bundle);
//        return recommendFragment;
//    }
    private void initViews() {
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_head_layout, null);
        mViewPager = (ViewPager) mHeaderView.findViewById(R.id.recommend_vp_page);
        mListView = (XRecyclerView) mCurrentView.findViewById(R.id.recyclerview);
        mIndicator = (CircleIndicator) mHeaderView.findViewById(R.id.indicator);
        final ViewTreeObserver observer = mViewPager.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mViewPager.getWidth();
                int viewPagerHeight = (int) (width / 1.7777778);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, viewPagerHeight);
                mViewPager.setLayoutParams(params);
                mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        authorParent = (LinearLayout) mHeaderView.findViewById(R.id.author_parent);
        rankingParent = (LinearLayout) mHeaderView.findViewById(R.id.ranking_parent);
        matchParent = (LinearLayout) mHeaderView.findViewById(R.id.match_parent);
        welfareParent = (LinearLayout) mHeaderView.findViewById(R.id.welfare_parent);
        mContentParent = (LinearLayout) mHeaderView.findViewById(R.id.content_parent);
        setClickListener();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        mListView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.SysProgress);
        mListView.setArrowImageView(R.drawable.iconfont_downgrey);
        mListView.setLoadingMoreEnabled(false);
        mListView.addHeaderView(mHeaderView);
        mListView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }
    /**
     * 给入口设置点击事件
     * 作者：houde on 2016/11/22 14:22
     * 邮箱：yangzhonghao@gietv.com
     */
    private void setClickListener() {
        //主播入口
        authorParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getActivity(), FastEntryAuthorActivity.class);
            }
        });
        //赛事入口
        matchParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameInfoBean.GameInfoEntity bean = new GameInfoBean.GameInfoEntity();
                bean._id = "57764e2fdc91fb1e37ebc69e";
                bean.name = "游戏赛事";
                bean.spic = "http://download.mlive.gietv.cn/liveservice/area/media/spic/游戏赛事2.jpg";
                CategoryActivity2.openActivity(getActivity(),bean);
            }
        });
        //福利入口
        welfareParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getActivity(), VRAllGameActivity.class);
            }
        });
        //排行榜入口
        rankingParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openActivity(getActivity(), RankingActivity.class);
            }
        });
    }
    private String mID;
    public void getData() {
        String userid = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
        String token = CacheUtils.getCache().getAsString(CacheConstants.CACHE_TOKEN);
        mID = getArguments().getString("id");
        Log.e("ceshi","RecommendFragment     onCreate     " + mID);
        if("all".equals(mID)) {
            mContentParent.setVisibility(View.VISIBLE);
            mModel.getRecommendData(userid, token, new DefaultLiveHttpCallBack<RecommendBean>() {
                @Override
                public void success(RecommendBean recommendBean) {
                    if (isNotFinish()) {
                        list.clear();
                        loadDataSuccess(recommendBean);
                        mListView.refreshComplete();
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        showDialog();
                    }
                }
            });
        }else{
            mModel.getCategoryRecommendData(mID, new DefaultLiveHttpCallBack<RecommendBean>() {
                @Override
                public void success(RecommendBean recommendBean) {
                    if(isNotFinish()) {
                        list.clear();
                        loadDataSuccess(recommendBean);
                        mListView.refreshComplete();
                    }
                }

                @Override
                public void failure(String message) {
                    if(isNotFinish()){
                        showDialog();
                    }
                }
            });
        }
    }

    private void showDialog() {
        mListView.refreshComplete();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.icon);
        builder.setTitle("提示");
        builder.setMessage("网络连接异常，请检查网络或联系客服人员");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public boolean onBack() {
        if (mCompereInfoFragment != null) {
            FragmentManager fm = getChildFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.remove(mCompereInfoFragment);
            ft.commit();
            mCompereInfoFragment = null;
            return false;
        }
        return true;
    }

    private CompereInfoFragment mCompereInfoFragment;

    private void loadDataSuccess(RecommendBean recommendBean) {
        if (isNotFinish()) {
            if(recommendBean == null){
                return;
            }
            if(recommendBean.carousels != null && recommendBean.carousels.size() > 0) {
                mPagerAdapter = new RecommendPagerAdapter(getChildFragmentManager(), recommendBean.carousels);
                mViewPager.setAdapter(mPagerAdapter);
                mIndicator.setViewPager(mViewPager);
                mHasLoadData = true;
            }
            if(recommendBean.programs != null && recommendBean.programs.size() > 1){
                list.add(recommendBean.programs);
            }
            if (recommendBean.programsbygameid != null && recommendBean.programsbygameid.size() > 1) {
                GameInfoBean gameInfoBean = new GameInfoBean();
                gameInfoBean.games = new ArrayList<>();
                GameInfoBean areaInfoBean = new GameInfoBean();
                areaInfoBean.games = new ArrayList<>();
                list.addAll(recommendBean.programsbygameid);
                flag = list.size();
            }
            if("all".equals(mID)) {
                if (recommendBean.follow != null && recommendBean.follow.size() > 1) {
                    list.add(recommendBean.follow);
                }
            }
            if (mListAdapter == null){
                mListAdapter = new RecommendListAdapter(getActivity(), list,flag);
                mListAdapter.setMoreClick(new OnItemClick<GameInfoBean.GameInfoEntity>() {
                    @Override
                    public void onClick(GameInfoBean.GameInfoEntity gameInfoEntity) {
                        if(gameInfoEntity.type == CommConstants.CAROUSEL_TYPE_AREA)
                            CategoryActivity2.openActivity(getActivity(), gameInfoEntity);
                        else if (gameInfoEntity.type == CommConstants.CAROUSEL_TYPE_ALBUM)
                            AlbumActivity.openAlbumActivity(gameInfoEntity.name,gameInfoEntity._id,getActivity());
                    }
                });
                mListView.setAdapter(mListAdapter);
            }else {
                mListAdapter.notifyDataSetChanged();
            }
            initCirculationPage();
        }
    }


    @Override
    public void onDestroy() {
//        RefWatcher refWatcher = MainApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
        if(mTimer != null)
            mTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCurrentView = inflater.inflate(R.layout.recommend_layout, container, false);
        initViews();
        mModel = RetrofitUtils.create(RecommendModel.class);
        getData();
        return mCurrentView;
    }
    private void initCirculationPage() {
        if (!mHasLoadData) {
            return;
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimerTask = new CirculationTimerTask(mHandler,mViewPager,mPagerAdapter);
        mTimer.schedule(mTimerTask, 0, 5000);
    }
    private CirculationTimerTask mTimerTask;
    private boolean mHasLoadData = false;
    private Timer mTimer;
    private Handler mHandler = new Handler();
}
