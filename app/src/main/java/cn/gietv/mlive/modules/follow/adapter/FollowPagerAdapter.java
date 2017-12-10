package cn.gietv.mlive.modules.follow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.gietv.mlive.modules.follow.fragment.FollowAreaFragment;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;

/**
 * Created by houde on 2016/8/24.
 */
public class FollowPagerAdapter extends FragmentStatePagerAdapter {
    private List<GameInfoBean.GameInfoEntity> gameList;
    public FollowPagerAdapter(FragmentManager fm, List<GameInfoBean.GameInfoEntity> games) {
        super(fm);
        this.gameList = games;
    }

    @Override
    public Fragment getItem(int position) {
        return FollowAreaFragment.getInstance(gameList.get(position)._id, "video");
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return gameList.get(position).name;
    }


}
