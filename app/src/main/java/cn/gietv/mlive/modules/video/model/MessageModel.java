package cn.gietv.mlive.modules.video.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.bean.BlackListBean;
import cn.gietv.mlive.modules.video.bean.DanmuBean;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/10/8 22:13
 *
 */
public interface MessageModel {
    @GET(UrlConstants.Message.URL_GET_MESSAGE_BY_PRO_ID)
    void getMessageByProId(@Query("proid") String proId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<MessageBean> callBack);

    @GET(UrlConstants.Message.URL_SEND_MESSAGE)
    void sendMessage(@Query("proid") String proId, @Query("message") String message, @Query("type") int type, @Query("isreply") String isreply, @Query("mid") String mid, @Query("replyuid") String replyuid, DefaultLiveHttpCallBack<MessageBean.MessagesEntity> callBack);

    @GET(UrlConstants.Message.URL_GET_TOPIC)
    void getMqttTopic(@Query("proid") String programId, DefaultLiveHttpCallBack<BlackListBean> callBack);

    @GET("/message/querydialogue")// 查询对话列表
    void getDialogList(@Query("proid") String proid, @Query("_id") String _id, DefaultLiveHttpCallBack<MessageBean> callback);

    @GET("/user/likes.action") //评论点赞
    void likeComment(@Query("resourceid") String resourse, @Query("type") int type, @Query("handle") int handle, DefaultLiveHttpCallBack<String> callBack);

    @GET("/danmuku/querybyproid")
    void getDanmuByProId(@Query("proid") String proid, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<DanmuBean> callBack);

    @GET("/danmuku/sendbyproid")
    void sendDanmu(@Query("proid") String proid, @Query("message") String message, @Query("videotimestamp") int videotimestamp, DefaultLiveHttpCallBack<DanmuBean.DanmuEntity> callback);
}
