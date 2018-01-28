package com.houde.competitive.lagua.persenter;

import android.content.Context;

import com.houde.competitive.lagua.contrart.recommend.RecommendContract;
import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.houde.competitive.lagua.model.recommend.RecommendModel;
import com.houde.competitive.lagua.ui.activity.ArticleDetailActivity;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class RecommendPresenter extends RecommendContract.RecommendPresenter {
    @Override
    public RecommendContract.IRecommendModel getModel() {
        return RecommendModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void getData() {
        mIView.setAdapter(mIModel.getData());
    }

    @Override
    public void onItemClick(int position, RecommendBean bean, Context context) {
        ArticleDetailActivity.openActivity(context);
    }
}
