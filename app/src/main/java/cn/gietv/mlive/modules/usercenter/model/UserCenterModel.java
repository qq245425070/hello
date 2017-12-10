package cn.gietv.mlive.modules.usercenter.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.bean.ProductBeanList;
import cn.gietv.mlive.modules.usercenter.bean.RechargeRecordBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * author：steven
 * datetime：15/10/1 21:20
 *
 */
public interface UserCenterModel {
    @GET(UrlConstants.UserInfo.URL_GET_USER_INFO_PROGRAM)
    void getUserInfo(@Query("userinfoid") String userId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<UserCenterBean> callBack);

    @Multipart
    @POST("/upload/userhead.action")
    void changeHeadImage(@Part("upfile") TypedFile typedFile, DefaultLiveHttpCallBack<String> callBack);

    @GET("/user/modifyuserinfo.action")
    void updateUserInfo(@Query("nickname") String nickName, @Query("desc") String desc, @Query("gender") int gender, @Query("email") String email, DefaultLiveHttpCallBack<UserCenterBean> callBack);

    @GET("/user/sendsms.action")
    void sendSMS(@Query("userid") String userid, @Query("mobilephone") String mobile, DefaultLiveHttpCallBack<String> callBack);

    @GET("/user/bindphone.action")
    void bindPhone(@Query("userid") String userid, @Query("mobilephone") String mobile, @Query("smscode") String smscode, DefaultLiveHttpCallBack<String> callBack);

    @GET("/pay/queryrechargelist.action")
    void queryChargeList(DefaultLiveHttpCallBack<ProductBeanList> callBack);

    @GET("/pay/querymyrechargelist.action")
    void rechargeRecord(@Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<RechargeRecordBean> callBack);

    @GET("/user/bindphonestatus.action")
    void getBindPhoneStatus(@Query("mobilephone") String mobilephone, DefaultLiveHttpCallBack<String> callBack);

    @GET("/resource/queryhistoryofvideo")
    void getHistoryVideo(@Query("nums") int pageNums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callback);
    //清空历史记录
    @GET("/resource/clearhistory.action")
    void clearHistory(DefaultLiveHttpCallBack<String> callBack);

    @GET("/program/querylistbyuserid_noempty.action")
    void getAllVideo(@Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);
}
