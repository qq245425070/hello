package cn.gietv.mlive.modules.recommend.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import cn.gietv.mlive.modules.recommend.fragment.RecommendPagerFragment;

/**
 * author：steven
 * datetime：15/9/21 19:51
 *
 */
public class RecommendPagerAdapter extends FragmentStatePagerAdapter {
    private List<RecommendBean.RecommendCarouseEntity> mCarouseEntities;

    public RecommendPagerAdapter(FragmentManager fm, List<RecommendBean.RecommendCarouseEntity> carouseEntities) {
        super(fm);
        mCarouseEntities = carouseEntities;
    }

    @Override
    public Fragment getItem(int position) {
        return RecommendPagerFragment.getInstence(mCarouseEntities.get(position));
    }

    @Override
    public int getCount() {
        return mCarouseEntities.size();
    }
}
