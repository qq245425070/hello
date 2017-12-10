package cn.gietv.mlive.modules.category.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.gietv.mlive.modules.category.fragment.CategoryFragment;

/**
 * Created by houde on 2016/5/27.
 */
public class CategoryPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> titles;
    private String mID;
    public CategoryPagerAdapter(FragmentManager fm,List<String> titles,String id,int type ) {
        super(fm);
        this.titles = titles;
        this.mID = id;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Fragment getItem(int position) {
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
            return CategoryFragment.getInstance(mID, type,"default");
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    private List<Object> data;
    public void setData(List<Object> list) {
        this.data = list;
    }
}
