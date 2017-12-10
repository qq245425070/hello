package cn.gietv.mlive.modules.vrgame.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by houde on 2016/7/6.
 */
public interface VRGameModel {
    @GET("/game/querymessagebyproid")
    void getMessagesByProId(@Query("proid") String proId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<MessageBean> callBack);

    @GET("/game/messagebyproid")
    void commitComment(@Query("proid") String proid, @Query("message") String message, @Query("type") int type, @Query("score") int score, DefaultLiveHttpCallBack<MessageBean.MessagesEntity> callback);
}
