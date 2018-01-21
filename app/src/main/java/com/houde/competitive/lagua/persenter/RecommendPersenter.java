package com.houde.competitive.lagua.persenter;

import com.houde.competitive.lagua.contrart.recommend.RecommendContract;
import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.houde.competitive.lagua.model.recommend.RecommendModel;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class RecommendPersenter extends RecommendContract.RecommendPersenter {
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
    public void onItemClick(int position, RecommendBean bean) {

    }
}
