package cn.gietv.mlive.modules.recommend.bean;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.TimerTask;

/**
 * 作者：houde on 2016/11/28 18:23
 * 邮箱：yangzhonghao@gietv.com
 */
public class CirculationTimerTask extends TimerTask {
    private Handler mHandler;
    private ViewPager mViewPager;
    private int mCurrentPage = 0;
    private PagerAdapter mPagerAdapter;

    public CirculationTimerTask(Handler handler, ViewPager viewPager, PagerAdapter pagerAdapter) {
        this.mHandler = handler;
        this.mViewPager = viewPager;
        this.mPagerAdapter = pagerAdapter;
    }

    @Override
    public void run() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mCurrentPage);
                if (mCurrentPage < mPagerAdapter.getCount() - 1) {
                    mCurrentPage++;
                } else {
                    mCurrentPage = 0;
                }
            }
        });
    }
}