package cn.gietv.mlive.modules.ranking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.gietv.mlive.modules.ranking.fragment.RankingCategoryFragment;

/**
 * 每个游戏下分类的Adapter
 * 作者：houde on 2016/11/23 15:42
 * 邮箱：yangzhonghao@gietv.com
 */
public class RankingCategoryPagerAdapter extends FragmentStatePagerAdapter{
    private String[] titles = {"播放数","收藏数","弹幕数","评论","上升最快"};
    private String mID;
    public RankingCategoryPagerAdapter(FragmentManager fm, String id) {
        super(fm);
        this.mID = id;
    }

    @Override
    public Fragment getItem(int position) {
        String ctype = "";
        switch (position){
            case 0:
                ctype = "play";
                break;
            case 1:
                ctype = "collection";
                break;
            case 2:
                ctype = "danmu";
                break;
            case 3:
                ctype = "comment";
                break;
            case 4:
                ctype = "fast";
                break;

        }
        return RankingCategoryFragment.getRankingCategoryFragment(mID,ctype);
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
