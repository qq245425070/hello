package cn.gietv.mlive.modules.author.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.gietv.mlive.modules.author.Fragment.AuthorFragment;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;

/**
 * 作者：houde on 2016/11/22 15:22
 * 邮箱：yangzhonghao@gietv.com
 */
public class AuthorPagerAdapter extends FragmentStatePagerAdapter {
    private List<GameInfoBean.GameInfoEntity> mGameList;
    public AuthorPagerAdapter(FragmentManager fm, List<GameInfoBean.GameInfoEntity> games) {
        super(fm);
        mGameList = games;
    }

    @Override
    public Fragment getItem(int position) {
        return AuthorFragment.getAuthorFragment(mGameList.get(position)._id);
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
