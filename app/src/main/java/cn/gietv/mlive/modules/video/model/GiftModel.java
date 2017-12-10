package cn.gietv.mlive.modules.video.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.video.bean.PropAcoinBean;
import cn.gietv.mlive.modules.video.bean.PropBeanList;
import cn.gietv.mlive.modules.video.bean.PropJinjiaoBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by houde on 2015/11/23.
 */
public interface GiftModel {
    @GET(UrlConstants.Gift.URL_SEND_GIFT)//赠送金币礼物
    void sendGift(@Query("cptype") String cptype, @Query("cpnum") long cpnum, @Query("coinnum") long coinnum, @Query("intoid") String intoid, DefaultLiveHttpCallBack<PropAcoinBean> callBack);

    @GET("/prop/queryprops.action")//查询道具
    void queryProps(DefaultLiveHttpCallBack<PropBeanList> callBack);

    @GET("/prop/sendrmbprop.action")//赠送金角礼物
    void sendRMBProp(@Query("propid") String propid, @Query("pnum") int pnum, @Query("total") int total, @Query("intoid") String intoid, DefaultLiveHttpCallBack<PropJinjiaoBean> callBack);
}
