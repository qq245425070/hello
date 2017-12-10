package cn.gietv.mlive.modules.ranking.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.author.model.FastEntryAuthorModel;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.ranking.adapter.RankingPagerAdapter;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * 排行榜的Activity
 * 作者：houde on 2016/11/23 15:09
 * 邮箱：yangzhonghao@gietv.com
 */
public class RankingActivity extends AbsBaseActivity{
    private SlidingTabLayout mTab;
    private ViewPager mPager;
    private RankingPagerAdapter mAdapter;
    private FastEntryAuthorModel model ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        HeadViewController.initFastEntryHead(this);
        mTab = (SlidingTabLayout) findViewById(R.id.navig_tab);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        model = RetrofitUtils.create(FastEntryAuthorModel.class);
        model.queryallarealistforuserid("game", CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<GameInfoBean>() {
            @Override
            public void success(GameInfoBean gameInfoBean) {
                if(isNotFinish()){
                    if(gameInfoBean == null || gameInfoBean.games == null)
                        return;
                    mAdapter = new RankingPagerAdapter(getSupportFragmentManager(),gameInfoBean.games);
                    mPager.setAdapter(mAdapter);
                    mTab.setViewPager(mPager);
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish())
                    ToastUtils.showToastShort(RankingActivity.this,message);
            }
        });

    }
}
