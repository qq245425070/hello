package cn.gietv.mlive.modules.game.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.views.PagerTab2;

public class GameListFragment extends AbsBaseFragment{
    private View mCurrentView;
    private PagerTab2 mPagerTab;
    private ViewPager mPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.game_list_layout, null, false);
        mPager = (ViewPager) mCurrentView.findViewById(R.id.viewpager);
        mPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mPagerTab = (PagerTab2) mCurrentView.findViewById(R.id.pager_tag);
        mPagerTab.setTextColor(R.color.tab_text_color_category);
        mPagerTab.setViewPager(mPager);
        mPagerTab.setCurrentPosition(0);
        return mCurrentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart("一级编目页");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd("一级编目页");
    }

    class MyPagerAdapter extends FragmentPagerAdapter{
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return CategoryFragment.getCategoryFragment("game");
                case 1:
                    return CategoryFragment.getCategoryFragment("vr");
                case 2:
                    return CategoryFragment.getCategoryFragment("3d");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "游戏";
                case 1:
                    return "VR";
                case 2:
                    return "3D";
            }
            return super.getPageTitle(position);
        }
    }
}
