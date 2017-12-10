package cn.gietv.mlive.modules.search.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.gietv.mlive.modules.search.fragment.CategoryFragment;

/**
 * Created by houde on 2016/7/22.
 */
public class SearchResultPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles;
    private String mID;

    public SearchResultPagerAdapter(FragmentManager fm, String[] titles, String id) {
        super(fm);
        this.titles = titles;
        this.mID = id;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        String category = null;
        int type = 0;
            switch (position) {
                case 0:
                    type = 2;
                    break;
                case 1:
                    type = 7;
                    break;
                case 2:
                    type = 8;
                    break;
            }
            return CategoryFragment.getInstance(mID, type, "default");
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}