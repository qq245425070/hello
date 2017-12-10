package cn.gietv.mlive.modules.login.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/16 20:20
 */
public interface LoginModel {
    @GET("/user/login.action")
    void login(@Query("userid") String username, @Query("password") String password, DefaultLiveHttpCallBack<UserInfo> callback);

    @GET("/user/thirdlogin.action")
    void thridLogin(@Query("usertype") int usertype, @Query("userid") String userid, @Query("nickname") String nickname, @Query("avatar") String avatar, @Query("gender") int gender, DefaultLiveHttpCallBack<UserInfo> callback);

    @GET("/user/verifysms.action")
    void verifysms(@Query("userid") String userid, @Query("mobilephone") String mobilephone, @Query("smscode") String smscode, DefaultLiveHttpCallBack<String> callBack);

    @GET("/user/smsmodifypsw.action")
    void smsmodifypsw(@Query("userid") String userid, @Query("password") String password, DefaultLiveHttpCallBack<UserInfo> callBack);
}
