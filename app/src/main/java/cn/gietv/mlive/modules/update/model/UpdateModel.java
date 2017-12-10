package cn.gietv.mlive.modules.update.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.update.bean.UpdateBean;

import retrofit.http.GET;

/**
 * author：steven
 * datetime：15/11/12 20:32
 * email：liuyingwen@kalading.com
 */
public interface UpdateModel {
    @GET("/system/checkversion.action")
    void checkUpdate(DefaultLiveHttpCallBack<UpdateBean> callBack);
}
