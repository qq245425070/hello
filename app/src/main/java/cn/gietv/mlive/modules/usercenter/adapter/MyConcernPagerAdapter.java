package cn.gietv.mlive.modules.usercenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by houde on 2016/7/5.
 */
public class MyConcernPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;
    private String mID;
    public MyConcernPagerAdapter(FragmentManager fm,String[] titles,String id) {
        super(fm);
        this.mTitles = titles;
        this.mID = id;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];

    }

    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return MyConcernFragment.getInstance(mID,4);
//            case 1:
//                return MyConcernFragment.getInstance(mID,5);
//            case 2:
//                return MyConcernFragment.getInstance(mID,7);
//            case 3:
//                return UserSubscribeFragment.getInstance(mID, CommConstants.TYPE_TAG);
//        }
        return null;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}