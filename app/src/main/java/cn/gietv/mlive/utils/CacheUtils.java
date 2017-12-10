package cn.gietv.mlive.utils;

import java.util.ArrayList;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.cache.ACache;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.constants.HttpConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.login.bean.UserInfo;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.video.bean.BlackListBean;
import cn.gietv.mlive.modules.video.bean.Contribution;
import cn.gietv.mlive.modules.video.bean.NoticeBean;
import cn.gietv.mlive.modules.video.bean.PropBeanList;

/**
 * author：steven
 * datetime：15/9/16 20:59
 */
public class CacheUtils {
    private static ACache mACache;

    public static void initCache() {
        mACache = ACache.get(MainApplication.getInstance());
        UserInfo userInfo = DBUtils.getInstance(MainApplication.getInstance()).getUser();
        if(userInfo != null && userInfo.userinfo != null){
            mACache.put(CacheConstants.CACHE_USERID,userInfo.userinfo._id);
            mACache.put(CacheConstants.CACHE_TOKEN,userInfo.token);
            CacheUtils.saveUserInfo(userInfo.userinfo);
        }
        String userid = mACache.getAsString(CacheConstants.CACHE_USERID);
        if (StringUtils.isEmpty(userid)) {
            mACache.put(CacheConstants.CACHE_USERID, HttpConstants.USER_ID_NONE);
        }
        String token = mACache.getAsString(CacheConstants.CACHE_TOKEN);
        if (StringUtils.isEmpty(token)) {
            mACache.put(CacheConstants.CACHE_TOKEN, HttpConstants.TOKEN_NONE);
        }
        String theme = mACache.getAsString(CacheConstants.CACHE_THEME_COLOR);
        if (StringUtils.isEmpty(theme)) {
            mACache.put(CacheConstants.CACHE_THEME_COLOR, String.valueOf(CommConstants.THEME_GREEN));
        }
    }

    public static ACache getCache() {
        return mACache;
    }

    public static UserCenterBean.UserinfoEntity getCacheUserInfo() {
        return (UserCenterBean.UserinfoEntity) mACache.getAsObject(CacheConstants.CACHE_USER_INFO);
    }

    public static void saveUserInfo(UserCenterBean.UserinfoEntity bean) {
        mACache.put(CacheConstants.CACHE_USER_INFO, bean);
    }
    public static void saveBlackList(String roomId,BlackListBean blackListBean){//保存黑名单
        mACache.put(CacheConstants.CACHE_BLACK_LIST + roomId , blackListBean.blacklist);
    }
    public static ArrayList<BlackListBean.BlackName> getCacheBlackList(String roomId){//获得黑名单
        return (ArrayList<BlackListBean.BlackName>)mACache.getAsObject(CacheConstants.CACHE_BLACK_LIST + roomId);
    }
    public static PropBeanList getPropList(){//获取全部道具
        return (PropBeanList) mACache.getAsObject(CacheConstants.CACHE_PROPS);
    }
    public static void savePropList(PropBeanList propBeanList){//保存全部道具
        mACache.put(CacheConstants.CACHE_PROPS,propBeanList);
    }

    public static void saveMyContributionJinjiao(Contribution mycontribution_jinjiao,String roomid) {//保存当前用户的金角等级
        mACache.put(CacheConstants.CACHE_JINJIAO + roomid,mycontribution_jinjiao);
    }
    public static Contribution getMyContributionJinjiao(String roomId){//获取当前用户的金角等级
        return (Contribution) mACache.getAsObject(CacheConstants.CACHE_JINJIAO + roomId);
    }

    public static void saveMyContributionCoin(Contribution mycontribution_coin,String roomid) {//保存当前用户的金币等级
        mACache.put(CacheConstants.CACHE_COIN + roomid,mycontribution_coin);
    }
    public static Contribution getMyContributionCoin(String roomid){//获取当前用户的金币等级
        return (Contribution) mACache.getAsObject(CacheConstants.CACHE_COIN + roomid);
    }
    public static void saveRecommendAnchor(String users){
        mACache.put(CacheConstants.CACHE_USERS,users);
    }
    public static String getRecommendAnchor(){
        return mACache.getAsString(CacheConstants.CACHE_USERS);
    }
    public static void saveNotice(NoticeBean bean,String roomid){
        mACache.put(CacheConstants.CACHE_NOTICE + roomid,bean);
    }
    public static NoticeBean getNotice(String roomid){
        return (NoticeBean) mACache.getAsObject(CacheConstants.CACHE_NOTICE + roomid);
    }
    public static void saveRecommendGame(GameInfoBean gameInfoBean){//保存推荐游戏
        mACache.put(CacheConstants.CACHE_RECOMMEND_GAME,gameInfoBean);
    }
    public static GameInfoBean getRecommendGame(){//获取保存的推荐游戏
        return (GameInfoBean) mACache.getAsObject(CacheConstants.CACHE_RECOMMEND_GAME);
    }
    public static void saveRecommendArea(GameInfoBean gameInfoBean){//保存推荐专区
        mACache.put(CacheConstants.CACHE_RECOMMEND_AREA,gameInfoBean);
    }
    public static GameInfoBean getRecommendArea(){//获取保存的推荐专区
        return (GameInfoBean) mACache.getAsObject(CacheConstants.CACHE_RECOMMEND_AREA);
    }
}
