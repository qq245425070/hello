package cn.gietv.mlive.modules.video.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.gietv.mlive.modules.video.fragment.CommentFragment;
import cn.gietv.mlive.modules.video.fragment.VideoPlayDescFragment;

/**
 * Created by houde on 2016/5/29.
 */
public class VideoPlayListPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private String mId;
    private CommentFragment mFragment;
    public VideoPlayListPagerAdapter(FragmentManager fm,String[] titles,String id,CommentFragment fragment) {
        super(fm);
        this.titles = titles;
        mId = id;
        mFragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return VideoPlayDescFragment.getInstance(mId);
            case 1:
                return mFragment;
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
