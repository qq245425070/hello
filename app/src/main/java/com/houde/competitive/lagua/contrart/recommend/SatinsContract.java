package com.houde.competitive.lagua.contrart.recommend;

import com.houde.competitive.lagua.model.bean.SatinsBean;
import com.zyw.horrarndoo.sdk.base.BasePresenter;
import com.zyw.horrarndoo.sdk.base.IBaseModel;
import com.zyw.horrarndoo.sdk.base.IBaseView;

import java.util.List;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public interface SatinsContract {
    abstract class BaseSatinsPersenter extends BasePresenter<ISatinsModel,ISatinsView>{
        protected abstract void getData();
    }
    interface ISatinsModel extends IBaseModel{
        List<SatinsBean> getData();
    }
    interface ISatinsView extends IBaseView{
        /**
         * 更新界面list
         *
         * @param list list
         */
        void updateContentList(List<SatinsBean> list);

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
