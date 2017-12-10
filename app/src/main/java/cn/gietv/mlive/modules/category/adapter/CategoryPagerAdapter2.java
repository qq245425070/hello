package cn.gietv.mlive.modules.category.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import cn.gietv.mlive.modules.category.fragment.CategoryFragment2;
import cn.gietv.mlive.modules.category.fragment.RecommendFragment;
import cn.gietv.mlive.parallaxheaderviewpager.ParallaxFragmentPagerAdapter;

/**
 * Created by houde on 2016/8/19.
 */
public class CategoryPagerAdapter2 extends ParallaxFragmentPagerAdapter {
    private List<String> mTitles;
    private String mID;
    public CategoryPagerAdapter2(FragmentManager fm,List<String> titles,String id) {
        super(fm, titles.size());
        this.mTitles = titles;
        this.mID = id;
    }

    @Override
    public Fragment getItem(int position) {
        String category = null;
        if (mTitles.size() == 4) {
            switch (position) {
                case 0:
                    RecommendFragment fragment = new RecommendFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    fragment.setArguments(bundle);
                    fragment.setData(mData);
                    return fragment;
                case 1:
                    category = "video";
                    break;
                case 2:
                    category = "album";
                    break;
                case 3:
                    category = "anchor";
                    break;
            }
            return CategoryFragment2.getInstance(mID, category,position);
        } else if (mTitles.size() == 3) {
            switch (position) {
                case 0:
                    category = "video";
                    break;
                case 1:
                    category = "album";
                    break;
                case 2:
                    category = "anchor";
                    break;
            }
            return CategoryFragment2.getInstance(mID, category,position);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    private List<Object> mData;
    public void setData(List<Object> list) {
        this.mData = list;
    }
}
