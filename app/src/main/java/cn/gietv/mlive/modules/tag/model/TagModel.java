package cn.gietv.mlive.modules.tag.model;

import cn.gietv.mlive.constants.UrlConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.search.bean.SearchBean;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/10/13 10:20
 */
public interface TagModel {
    int TAG_MODEL_LIVE = 1;
    int TAG_MODEL_VIDEO = 2;
    int TAG_MODEL_GAME = 3;
    int TAG_MODEL_LIVE_AND_VIDEO = 12;
    int TAG_MODEL_LIVE_VIDEO_GAME = 123;

    @GET("/resource/search.action")
    void queryGameListByTag(@Query("keywords") String tag, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<SearchBean> callBack);

    @GET(UrlConstants.Tag.URL_QUERY_LIST_BY_TAG)
    void queryProgramListByTag(@Query("tag") String tag, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, @Query("resourcetype") int resourcetype, DefaultLiveHttpCallBack<ProgramBean> callBack);
}
