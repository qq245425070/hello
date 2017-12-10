package cn.gietv.mlive.modules.ranking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.modules.ranking.adapter.RankingCategoryPagerAdapter;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;

/**
 * 作者：houde on 2016/11/23 15:32
 * 邮箱：yangzhonghao@gietv.com
 */
public class RankingFragment extends AbsBaseFragment {
    private View mRootView ;
    private SlidingTabLayout mTab;
    private ViewPager mPager;
    private String mID;
    private RankingCategoryPagerAdapter mAdapter;
    public static RankingFragment getRankingFragment(String id){
        RankingFragment fragment = new RankingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mID = getArguments().getString("id");
        mRootView = inflater.inflate(R.layout.fragment_ranking,null);
        mTab = (SlidingTabLayout) mRootView.findViewById(R.id.navig_tab);
        mPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        mAdapter = new RankingCategoryPagerAdapter(getChildFragmentManager(),mID);
        mPager.setAdapter(mAdapter);
        mTab.setViewPager(mPager);
        return mRootView;

    }
}
