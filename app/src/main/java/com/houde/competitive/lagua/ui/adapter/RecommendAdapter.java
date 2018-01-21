package com.houde.competitive.lagua.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.houde.competitive.lagua.R;
import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.houde.competitive.lagua.ui.fragment.ViewPagerFragment;
import com.zyw.horrarndoo.sdk.adapter.FragmentAdapter;
import com.zyw.horrarndoo.sdk.base.fragment.BaseCompatFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : houde
 *         Date : 18-1-20
 *         Desc :
 */

public class RecommendAdapter extends BaseMultiItemQuickAdapter<RecommendBean,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseCompatFragment mContext;
    public RecommendAdapter(List<RecommendBean> data, BaseCompatFragment fragment) {
        super(data);
        this.mContext =fragment;
        addItemType(RecommendBean.TYPE_IMAGE, R.layout.item_recomm_image);
        addItemType(RecommendBean.TYPE_ARTICLE,R.layout.item_recomm_article);
        addItemType(RecommendBean.TYPE_HEADER,R.layout.item_recomm_header);
        addItemType(RecommendBean.TYPE_SATINS,R.layout.item_recomm_satins);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendBean item) {
        switch (item.getItemType()){
            case RecommendBean.TYPE_IMAGE:
                helper.setText(R.id.title_tv,item.title);
                helper.setText(R.id.read_count_tv,item.readCount+"阅读");
//                GlideUtils.display(mContext,item.imagePaths.get(0), (ImageView) helper.getView(R.id.image1_iv));
//                GlideUtils.display(mContext,item.imagePaths.get(1), (ImageView) helper.getView(R.id.image2_iv));
                break;
            case RecommendBean.TYPE_ARTICLE:
                helper.setText(R.id.title_tv,item.title);
                helper.setText(R.id.read_count_tv,item.readCount+"阅读");
//                GlideUtils.display(mContext,item.imagePath, (ImageView) helper.getView(R.id.image1_iv));

                break;
            case RecommendBean.TYPE_SATINS:
                helper.setText(R.id.title_tv,item.title);
                helper.setText(R.id.read_count_tv,item.readCount+"阅读");
//                GlideUtils.display(mContext,item.imagePaths.get(0),(ImageView)helper.getView(R.id.image1_iv));
//                GlideUtils.display(mContext,item.imagePaths.get(1),(ImageView)helper.getView(R.id.image2_iv));
//                GlideUtils.display(mContext,item.imagePaths.get(2),(ImageView)helper.getView(R.id.image3_iv));
                break;
            case RecommendBean.TYPE_HEADER:
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
