package cn.gietv.mlive.modules.author.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * 作者：houde on 2016/12/1 17:01
 * 邮箱：yangzhonghao@gietv.com
 */
public interface FastEntryAuthorModel {
    @GET("/fastentry/queryallarealistforuserid.action")
    void queryallarealistforuserid(@Query("areatype") String areatype, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort , DefaultLiveHttpCallBack<GameInfoBean> callBack);
}
