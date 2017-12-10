package cn.gietv.mlive.modules.usercenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.modules.usercenter.fragment.UserSubscribeFragment;

/**
 * Created by houde on 2016/9/23.
 */
public class UserSubscribePagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = {"专区","专辑","作者","标签"};
    private String mUserID;
    public UserSubscribePagerAdapter(FragmentManager fm,String id,int isMine) {
        super(fm);
        mUserID = id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
//                return MyConcernFragment.getInstance(mUserID, CommConstants.CAROUSEL_TYPE_AREA);
                return UserSubscribeFragment.getInstance(mUserID, CommConstants.CAROUSEL_TYPE_AREA);
            case 1:
                return UserSubscribeFragment.getInstance(mUserID, CommConstants.CAROUSEL_TYPE_ALBUM);
            case 2:
                return UserSubscribeFragment.getInstance(mUserID, CommConstants.CAROUSEL_TYPE_ANCHOR);
            case 3:
                return UserSubscribeFragment.getInstance(mUserID, CommConstants.TYPE_TAG);
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
