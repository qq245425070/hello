package cn.gietv.mlive.modules.recommend.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.recommend.fragment.RecommendFragment;

/**
 * 
 * 作者：houde on 2016/11/22 10:41
 * 邮箱：yangzhonghao@gietv.com
 */
public class ReCommendHomePagerAdapter extends FragmentStatePagerAdapter {
    private List<GameInfoBean.GameInfoEntity> mGames;
    public ReCommendHomePagerAdapter(FragmentManager fm, List<GameInfoBean.GameInfoEntity> games) {
        super(fm);
        this.mGames = games;
    }

    @Override
    public Fragment getItem(int position) {
        RecommendFragment recommendFragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("id",mGames.get(position)._id);
        recommendFragment.setArguments(bundle);
        return recommendFragment;
    }

    @Override
    public int getCount() {
        return mGames.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mGames.get(position).name;
    }
}
