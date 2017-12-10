package cn.gietv.mlive.modules.follow.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.user.bean.UserBean;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * <p>
 * datetime：15/9/25 10:45
 */
public interface FollowModel {
    @GET(UrlConstants.Follow.URL_FOLLOW_BY_CONTENT)
    void followByContent(@Query("resourceid") String contentId, @Query("resourcetype") int resourcetype, @Query("handle") int action, DefaultLiveHttpCallBack<String> callBack);

    @GET(UrlConstants.Follow.URL_FOLLOW_BY_PROGRAM)
    void followByProgram(@Query("proid") String programId, @Query("handle") int action, DefaultLiveHttpCallBack<String> callBack);

    @GET(UrlConstants.Follow.URL_FOLLOW_BY_USRE)
    void followByUser(@Query("followid") String userId, @Query("handle") int action, DefaultLiveHttpCallBack<String> callBack);

    @GET(UrlConstants.Follow.URL_GET_BE_FOLLOW_USER_BY_USERID)
    void getFollowUserByUserId(@Query("userinfoid") String userinfoId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<UserBean> callBack);

    @GET(UrlConstants.Follow.URL_GET_FOLLOW_USER_BY_USERID)
    void getBeFollowUserByUserId(@Query("userinfoid") String userinfoId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<UserBean> callBack);

    @GET("/resource/queryfollowsbyresourceid.action")
    void getFollowUserByGameId(@Query("resourceid") String gameId, @Query("resourcetype") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<UserBean> callBack);
    //@Query("resourcetyp") int type,

    @GET("/resource/queryfollowresourcebyuserid.action")
    void getFollowsByUserId(@Query("userinfoid") String id, @Query("resourcetype") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/user/queryfollowresourcebyuserid.action")
    void getFollowsByUserId2(@Query("userinfoid") String id, @Query("resourcetype") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/user/follow.action")
    void follow(@Query("followid") String followid, @Query("handle") int action, @Query("type") int type, DefaultLiveHttpCallBack<String> callBack);

    @GET("/user/followlist.action")
    void followList(@Query("followidlist") String followidlist, @Query("type") int type, @Query("handle") int handle, DefaultLiveHttpCallBack<String> callBack);

}
