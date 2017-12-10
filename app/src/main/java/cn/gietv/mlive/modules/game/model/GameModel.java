package cn.gietv.mlive.modules.game.model;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.game.bean.AlbumBean;
import cn.gietv.mlive.modules.game.bean.AuthorBean;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.game.bean.GameProgramBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.user.bean.UserBean;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * author：steven
 * datetime：15/9/17 14:23
 *
 */
public interface GameModel {
    @GET("/game/querygamelist.action")
    void getGameList(@Query("gametype") String gametype, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/game/querygamebygameid.action")
    void getGameInfo(@Query("gameid") String gameId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameProgramBean> callBack);

    @GET("/user/queryfollowgamebyuserid.action")
    void getAttentionGameList(@Query("userinfoid") String userId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/game/queryareabyareaid.action")
    void getArea(@Query("areaid") String areaid, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameProgramBean> callBack);

    @GET("/user/queryfollowareabyuserid.action")
    void getAttentionAreaList(@Query("userinfoid") String userid, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/game/queryarealist.action")
    void getAreaList(@Query("areatype") String areatype, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/user/queryanchorlist.action")
    void getAnchorList(@Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<UserBean> callBack);

    @GET("/catalogue/queryalbumlistbyareaid")
    void getAlbumList(@Query("areaid") String areaid, @Query("tagkey") String tagkey, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AlbumBean> callBack);

    @GET("/catalogue/queryauthorlistbyareaid")
    void getAnchors(@Query("areaid") String areaid, @Query("tagkey") String tagkey, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AuthorBean> callBack);

    @GET("/catalogue/queryarealist")
    void getAlbums(@Query("areatype") String areatype, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

    @GET("/catalogue/queryprogramlistbyalbumid")
    void getAlbumLists(@Query("albumid") String areaid, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET("/catalogue/queryprogramlistbyareaid")
    void queryprogramlistbyareaid(@Query("areaid") String areaid, @Query("tagkey") String tagkey, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET("/catalogue/queryprogramlistbyareaidlist")
    void queryprogramlistbyareaidList(@Query("areaid") String areaid, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET("/catalogue/querytypelistbytag")
    void queryProgramTypeListByTag(@Query("tag") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<ProgramBean> callBack);

    @GET("/catalogue/querytypelistbytag")
    void queryAlbumTypeListByTag(@Query("tag") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AlbumBean> callBack);

    @GET("/catalogue/querytypelistbytag")
    void queryAuthorTypeListByTag(@Query("tag") String tag, @Query("type") int type, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<AuthorBean> callBack);
//    @GET("/catalogue/queryalbumlistbyauthorid")
//    void getAlbumList(@Query("authorid") String userinfoId, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort,DefaultLiveHttpCallBack<cn.gietv.mvr.modules.compere.bean.AlbumBean> callBack);

    @GET("/user/queryfollowareabyuserid_noempty.action")
    void getAttentionAreaListNoEmpty(@Query("userinfoid") String userid, @Query("nums") int nums, @Query("page") int page, @Query("sort") int sort, DefaultLiveHttpCallBack<GameInfoBean> callBack);

}
