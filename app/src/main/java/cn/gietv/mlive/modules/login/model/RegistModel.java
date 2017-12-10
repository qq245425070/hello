package cn.gietv.mlive.modules.login.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.bean.UserInfo;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/16 20:20
 *
 */
public interface RegistModel {
    @GET("/user/register.action")
    void regist(@Query("nickname") String nickname, @Query("userid") String userId, @Query("password") String password, @Query("email") String email, DefaultLiveHttpCallBack<UserInfo> callback);

    @GET("/user/smsregister.action")
    void smsRegister(@Query("userid") String userid, @Query("password") String password, @Query("mobilephone") String mobile, @Query("nickname") String nickname, DefaultLiveHttpCallBack<UserInfo> callBack);


}
