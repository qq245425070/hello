package cn.gietv.mlive.modules.setting.activity.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by houde on 2016/3/23.
 */
public interface SettingModel {
    @GET("/system/feedback.action")
    void submitTucao(@Query("content") String content, DefaultLiveHttpCallBack<String> callBack);
}
