package cn.gietv.mlive.modules.program.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/17 20:11
 *
 */
public interface ProgramModel {
    @GET("/game/querygamebygameid.action")
    void getProgramByGameId(@Query("gameid") String gameId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET(UrlConstants.Programs.URL_GET_PROGRAM_LIST)
    void getProgramList(@Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, @Query("protype") int protype, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET("/program/querybyproid.action")
    void getProgramById(@Query("proid") String proId, DefaultLiveHttpCallBack<ProgramBean.ProgramEntity> callBack);

}
