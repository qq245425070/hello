package cn.gietv.mlive.modules.home.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.bean.TasksBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author: houde
 * Created by houde on 2015/11/23.
 */
public interface NoviceTaskModel {
    @GET(UrlConstants.NoviceTask.URL_SEND_NOVICETASK)
    void sendNoviceTast(@Query("first") String first, DefaultLiveHttpCallBack<String> callBack);

    @GET(UrlConstants.NoviceTask.URL_SEND_DAYTASK)
    void sendDayTask(@Query("behavior") String behavior, DefaultLiveHttpCallBack<String> callback);

    @GET(UrlConstants.NoviceTask.URL_SEND_TASK)
    void sendTask(@Query("behavior") String behavior, DefaultLiveHttpCallBack<String> callback);

    @GET(UrlConstants.NoviceTask.URL_QUERY_TASK)
    void queryTask(DefaultLiveHttpCallBack<TasksBean> callback);

    @GET(UrlConstants.NoviceTask.URL_UPDATE_TASK)
    void updateTask(@Query("behavior") String behavior, @Query("num") int num, DefaultLiveHttpCallBack<String> callback);
}
