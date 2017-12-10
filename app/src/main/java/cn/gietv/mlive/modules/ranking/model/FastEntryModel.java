package cn.gietv.mlive.modules.ranking.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * 作者：houde on 2016/12/2 14:43
 * 邮箱：yangzhonghao@gietv.com
 */
public interface FastEntryModel {
    @GET("/fastentry/queryProgrambyareaidandtype.action")
    void queryProgrambyareaidandtype(@Query("proid") String proid, @Query("ctype") String ctype, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);
}
