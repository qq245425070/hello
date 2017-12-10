package cn.gietv.mlive.modules.search.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.AlbumBean;
import cn.gietv.mlive.modules.game.bean.AuthorBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.search.bean.TagSearchBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by houde on 2016/5/31.
 */
public interface SearchModel {
    @GET("/catalogue/querytypelistbytag")
    void getListByTag(@Query("tag") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<TagSearchBean> callBack);

    @GET("/resource/querytypelistbykeywords")
    void queryProgramTypeListByTag(@Query("keywords") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET("/resource/querytypelistbykeywords")
    void queryAlbumTypeListByTag(@Query("keywords") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AlbumBean> callBack);

    @GET("/resource/querytypelistbykeywords")
    void queryAuthorTypeListByTag(@Query("keywords") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AuthorBean> callBack);
}
