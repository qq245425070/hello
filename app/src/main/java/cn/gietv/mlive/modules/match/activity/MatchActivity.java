package cn.gietv.mlive.modules.match.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.match.adapter.MatchPagerAdapter;
import cn.gietv.mlive.utils.HeadViewController;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;

/**
 * 赛事的Activity
 * 作者：houde on 2016/11/23 15:09
 * 邮箱：yangzhonghao@gietv.com
 */
public class MatchActivity extends AbsBaseActivity{
    private SlidingTabLayout mTab;
    private ViewPager mPager;
    private MatchPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        HeadViewController.initFastEntryHead(this);
        mTab = (SlidingTabLayout) findViewById(R.id.navig_tab);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new MatchPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTab.setViewPager(mPager);
    }
}
