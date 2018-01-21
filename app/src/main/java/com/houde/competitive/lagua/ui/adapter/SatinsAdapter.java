package com.houde.competitive.lagua.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.houde.competitive.lagua.model.bean.SatinsBean;
import com.houde.competitive.lagua.ui.fragment.ViewPagerFragment;
import com.zyw.horrarndoo.sdk.adapter.FragmentAdapter;
import com.zyw.horrarndoo.sdk.base.fragment.BaseCompatFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class SatinsAdapter extends BaseMultiItemQuickAdapter<SatinsBean,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseCompatFragment mContext;
    public SatinsAdapter(List<SatinsBean> data, BaseCompatFragment fragment) {
        super(data);
        this.mContext = fragment;
        addItemType(SatinsBean.TYPE_CONTENT, R.layout.item_satins_content);
        addItemType(SatinsBean.TYPE_HEADER,R.layout.item_recomm_header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SatinsBean item) {
        switch (item.getItemType()){
            case SatinsBean.TYPE_CONTENT:
                helper.setText(R.id.zan_tv,String.valueOf(item.upvoteCount));
                helper.setText(R.id.content_tv,item.content);
                helper.setText(R.id.bishi_tv,String.valueOf(item.caiCount));
                helper.setText(R.id.share_tv,String.valueOf(item.shareCount));
                break;
            case SatinsBean.TYPE_HEADER:
                ViewPager viewPager = helper.getView(R.id.view_pager);
                List<Fragment> fragments = new ArrayList<>();
                fragments.add(ViewPagerFragment.newInstance());
                fragments.add(ViewPagerFragment.newInstance());
                fragments.add(ViewPagerFragment.newInstance());
                fragments.add(ViewPagerFragment.newInstance());
                viewPager.setAdapter(new FragmentAdapter(mContext.getChildFragmentManager(),fragments));
                break;
            default:
                break;
        }
    }
}
