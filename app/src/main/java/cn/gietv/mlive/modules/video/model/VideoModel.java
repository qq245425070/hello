package cn.gietv.mlive.modules.video.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.bean.VideoPlayBean;
import cn.kalading.android.retrofit.utils.DefaultHttpCallback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/10/8 20:17
 *
 */
public interface VideoModel {
    @GET(UrlConstants.Video.URL_GET_VIDEO_PLAY_ADDRESS)
    void getPlayUrl(@Query("proid") String proId, DefaultLiveHttpCallBack<String> callBack);
    @GET(UrlConstants.Message.URL_GET_LOGIN)
    void chatLogin(@Query("proid") String proId, @Query("handle") int handle, DefaultHttpCallback<String> callback);

    @GET("/program/querybyproid.action")
    void queryProByProid(@Query("proid") String proId, DefaultLiveHttpCallBack<VideoPlayBean> callBack);

    @GET("/user/report.action")
    void report(@Query("resourceid") String resourceid, @Query("resourcetype") int resourcetype, @Query("reporttype") String reporttype, DefaultLiveHttpCallBack<String> callback);
}
