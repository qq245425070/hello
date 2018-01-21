package com.houde.competitive.lagua.persenter;

import com.houde.competitive.lagua.contrart.recommend.SatinsContract;
import com.houde.competitive.lagua.model.bean.SatinsBean;
import com.houde.competitive.lagua.model.recommend.SatinsModel;
import com.zyw.horrarndoo.sdk.base.IBaseModel;

import java.util.List;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class SatinsPresenter extends SatinsContract.BaseSatinsPersenter {
    @Override
    public SatinsContract.ISatinsModel getModel() {
        return SatinsModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void getData() {
         mIView.updateContentList(mIModel.getData());
    }
}
