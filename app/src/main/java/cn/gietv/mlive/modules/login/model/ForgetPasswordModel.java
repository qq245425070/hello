package cn.gietv.mlive.modules.login.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;

import cn.gietv.mlive.modules.login.bean.UserExist;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/17 14:05
 *
 */
public interface ForgetPasswordModel {
    @GET("/user/pswforgot.action")
    void sendEmail(@Query("userid") String userId, DefaultLiveHttpCallBack<String> callBack);

    @GET("/user/queryuserisexist.action")
    void existUser(@Query("userid") String userid, DefaultLiveHttpCallBack<UserExist> callback);

}
