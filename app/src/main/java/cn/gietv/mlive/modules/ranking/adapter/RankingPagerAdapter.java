package cn.gietv.mlive.modules.ranking.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.ranking.fragment.RankingFragment;

/**
 * 排行榜Activity中ViewPager 中的Adapter
 * 作者：houde on 2016/11/23 15:29
 * 邮箱：yangzhonghao@gietv.com
 */
public class RankingPagerAdapter extends FragmentStatePagerAdapter {
    private List<GameInfoBean.GameInfoEntity> mGameList;
    public RankingPagerAdapter(FragmentManager fm, List<GameInfoBean.GameInfoEntity> games) {
        super(fm);
        mGameList = games;
    }

    @Override
    public Fragment getItem(int position) {
        return RankingFragment.getRankingFragment(mGameList.get(position)._id);
    }

    @Override
    public int getCount() {
        return mGameList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mGameList.get(position).name;
    }
}
