package com.houde.competitive.lagua.contrart.recommend;

import com.houde.competitive.lagua.model.bean.RecommendBean;
import com.zyw.horrarndoo.sdk.base.BasePresenter;
import com.zyw.horrarndoo.sdk.base.IBaseFragment;
import com.zyw.horrarndoo.sdk.base.IBaseModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public interface RecommendContract {
    abstract class RecommendPersenter extends BasePresenter<IRecommendModel,IRecommendView>{
        public abstract void getData();
        public abstract void onItemClick(int position, RecommendBean bean);
    }
    interface IRecommendModel extends IBaseModel{
        List<RecommendBean> getData();
    }
    interface IRecommendView extends IBaseFragment{
        /**
         * 得到数据，设置给RecyclerView
         * @param beanList 数据
         */
        void setAdapter(List<RecommendBean> beanList);

        /**
         * 请求数据错误
         */
        void showErrorView();

        /**
         * 显示网络错误
         */
        void showNetworkError();

        /**
         * 显示加载更多错误
         */
        void showLoadMoreError();

        /**
         * 显示没有更多数据
         */
        void showNoMoreData();

    }
}
