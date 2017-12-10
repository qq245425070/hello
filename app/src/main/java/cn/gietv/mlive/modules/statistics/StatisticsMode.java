package cn.gietv.mlive.modules.statistics;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/11/10 22:25
 * email：liuyingwen@kalading.com
 */
public interface StatisticsMode {
    @GET("/program/play.action")
    void playSta(@Query("proid") String proId, @Query("handle") int action, DefaultLiveHttpCallBack<String> callback);

    @GET("/datacollect/game.action")
    void gameAction(@Query("gameid") String gameId, @Query("handle") int action, DefaultLiveHttpCallBack<String> callBack);

    @GET("/datacollect/device.action")
    void sendDevice(DefaultLiveHttpCallBack<String> callBack);

    @GET("/datacollect/program.action")
    void sendProgramTime(@Query("proid") String proid, @Query("duration") long duration, @Query("name") String name, @Query("type") int type, @Query("starttime") long starttime, @Query("endtime") long endtime, DefaultLiveHttpCallBack<String> callBack);

    @GET("/datacollect/share.action")
    void sendShareData(@Query("resourceid") String resourceid, @Query("resourcetype") int resourcetype, @Query("sharetype") int sharetype, DefaultLiveHttpCallBack<String> callBack);

    @GET("/datacollect/cache.action")
    void sendDownloadData(@Query("resourceid") String resourceid, @Query("resourcetype") int resourcetype, DefaultLiveHttpCallBack<String> callBack);
    //向服务器发送umeng的token
    @GET("/datacollect/umengdevicetoken.action")
    void sendUmengDevice(@Query("divice_token") String deviceToken, DefaultLiveHttpCallBack<String> callBack);
}
