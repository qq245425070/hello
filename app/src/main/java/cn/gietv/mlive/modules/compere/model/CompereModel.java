package cn.gietv.mlive.modules.compere.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.compere.bean.AlbumBean;
import cn.gietv.mlive.modules.compere.bean.CompereListBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/25 10:53
 *
 */
public interface CompereModel {
    @GET(UrlConstants.Compere.URL_GET_COMPERE_BY_USER_ID)
    void getCompereByUserId(@Query("userinfoid") String userinfoId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<UserCenterBean> callBack);

    @GET("/user/queryfollowuserbyuserid.action")
    void getAttentionCompere(@Query("userinfoid") String userinfoId, @Query("nums") int nums, @Query("page") int page, @Query("usertype") int usertype, @Query("sort") int sort, DefaultLiveHttpCallBack<CompereListBean> callBack);

    @GET("/catalogue/queryalbumlistbyauthorid")
    void getAlbumList(@Query("authorid") String userinfoId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AlbumBean> callBack);
}
