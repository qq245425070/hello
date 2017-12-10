package cn.gietv.mlive.modules.recommend.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.recommend.bean.RecommendBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/21 22:05
 *
 */
public interface RecommendModel {
    @GET("/page/queryhomepage.action")
    void getRecommendData(@Query("userid") String userId, @Query("token") String token, DefaultLiveHttpCallBack<RecommendBean> callBack);
    @GET("/page/homepage.action")
    void getCategoryRecommendData(@Query("moduleid") String id, DefaultLiveHttpCallBack<RecommendBean> callBack);
    @GET("/catalogue/queryarealistforuserid.action")
    void getAreaList(@Query("areatype") String areatype, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);
}
