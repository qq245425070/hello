package cn.gietv.mlive.modules.usercenter.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.bean.OrderBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by houde on 2016/1/31.
 */
public interface OrderModel {
    @GET("/pay/createorder.action")
    void createOrder(@Query("userid") String userid, @Query("nickname") String nickname, @Query("payuserid") String payuserid, @Query("paynickname") String paynickname, @Query("goodsid") String goodsid, @Query("goodsnum") int goodsnum, DefaultLiveHttpCallBack<OrderBean> callBack);
}
