package cn.gietv.mlive.modules.match.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.gietv.mlive.modules.match.fragment.MatchFragment;

/**
 * 赛事Activity中ViewPager 中的Adapter
 * 作者：houde on 2016/11/23 15:29
 * 邮箱：yangzhonghao@gietv.com
 */
public class MatchPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = {"赛事日程","我的世界","英雄联盟","守望先锋"};
    private String[] ids = {"573d4fb65c7151481012d465","573d4fb55c7151481012d460","573d4fb65c7151481012d464","577cf5ebdc91fb71adc643ab"};
    public MatchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MatchFragment.getMatchFragment(ids[position]);
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
