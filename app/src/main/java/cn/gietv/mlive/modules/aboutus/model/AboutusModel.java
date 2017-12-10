package cn.gietv.mlive.modules.aboutus.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;

import retrofit.http.GET;

/**
 * author：steven
 * datetime：15/11/15 21:46
 * email：liuyingwen@kalading.com
 */
public interface AboutusModel {
    @GET("/system/aboutus.action")
    void getAboutUs(DefaultLiveHttpCallBack<String> callBack);
}
