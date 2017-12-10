package cn.gietv.mlive.modules.login.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/17 14:06
 *
 */
public interface UserInfoModel {
    @GET("/user/login.action")
    void login(@Query("userid") String userid, @Query("password") String password, DefaultLiveHttpCallBack<UserInfo> callBack);

    @GET("/user/weixinlogin.action")
    void loginWithWeixin(@Query("userid") String userid, @Query("code") String code, DefaultLiveHttpCallBack<UserInfo> callBack);

    @GET("/user/login.action")
    void regist(@Query("userid") String userid, @Query("password") String password, @Query("email") String email, DefaultLiveHttpCallBack<UserInfo> callBack);

    @GET("/user/pwdmodify.action")
    void updatePassword(@Query("userid") String userId, @Query("oldpsw") String oldPassword, @Query("password") String newPassword, DefaultLiveHttpCallBack<UserCenterBean.UserinfoEntity> callBack);
}
