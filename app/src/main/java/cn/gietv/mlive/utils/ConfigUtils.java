package cn.gietv.mlive.utils;

/**
 * Created by houde on 2015/11/23.
 */
public class ConfigUtils {
    //赠送游戏币的语言
    public final static String SEND_MSG_GIFT = "赠送给主播";

    //********************************************************************
    //聊天类型
    //进入退出聊天室
    public final static String CHAT_ROOM_IN_OUT = "A1";
    // 用户聊天
    public final static String USER_CHAT = "A2";
    //  关注聊天室
    public final static String CHAT_ROOM_CONCERN = "A3";
    //  关注主播
    public final static String CHAT_ROOM_LIVE = "A4";
    //  赠送礼物
    public final static String CHAT_ROOM_SEND_GIFT = "A5";

    /********************************************************************************/
    //视频是全屏
    public final static String FULL_SCREEN = "1";
    //视频不是全屏
    public final static String SMALL_SCREEN = "0";

    //*******************************************************************************

    public final static String SERVICE_START_FLAG = "service_flag";

    public final static int FROM_ME = 1;

    public final static int FROM_OTHER = 0;

    public final static int MESSAGE_READ = 1;

    public final static int MESSAGE_NOT_READ = 0;
    //*************************************************
    //数据库的版本号
    public final static int DATABASE_VERSION_CODE = 20;
    //U盟统计的事件ID
    public final static String UMENG_LIVE_TIME = "live_time";
    //专区页默认的排序方式
    public final static int DEFAULT_SORT_TYPE = 1;

    //进入房间的topic
    public final static String TOPIC_ENTER_ROOM = "-liveservice-enter-";
    //退出房间的topic
    public final static String TOPIC_LEFT_ROOM = "-liveservice-left-";
    //历史聊天记录的topic
    public final static String TOPIC_CHAT_HISTORY = "-liveservice-chathistory-";
    //聊天的topic
    public final static String TOPIC_CHAT = "-liveservice-activity-chat-";
    //黑名单的topic
    public final static String TOPIC_BLACK_LIST = "-liveservice-activity-blacklist-";
    //私聊的topic
    public final static String PRIVATE_CHAT = "-liveservice-private-chat-";
    //赠送金币的topic
    public final static String TOPIC_SEND_CION = "-liveservice-activity-coin-";
    //赠送人民币道具的topic
    public final static String TOPIC_SEND_RMB = "-liveservice-activity-RMB-";
    //系统消息的topic
    public final static String TOPIC_SYSTEM_MESSAGE = "-liveservice-system";
    //系统消息的topic
    public final static String TOPIC_SYSTEM_MESSAGE_TEST = "-liveservice-system_dev";
    //未读消息
    public final static String HOME_MESSAGE_NOT_READ = "message_not_read";
    //系统消息的userID
    public final static String SYSTEM_MESSAGE_USERID = "dujiaoshipin";
    //订阅订单的topic头
    public final static String TOPIC_ORDER = "/liveservice/pay/orderid/";
    //公告跟新的topic
    public final static String TOPIC_NOTICE = "-liveservice-activity-notice-";
    //录播留言的topic
    public final static String TOPIC_LUBO= "-liveservice-video-";

    //---------------APPID  APPKEY   VR----------------------------------------------------
/*    public final  static String WEIXIN_APPID = "wxca5dec4a0ef6e21b";
    public final  static String WEIXIN_APPKEY = "0ec22bcffa448cdf6dbbed9c088a863d";
    //微信支付商户ID
    public static final String MCH_ID = "1304846901";
    //微信支付API-KEY
    public final static String WEIXIN_API_KEY = "568506323feb69180874d8766776dc0d";

    public final static String QQ_APPID = "1105260091";
    public final static String QQ_APPKEY = "A56v05eD8Alky6c2";

    //充值的物品ID 默认是jinjiao
    public static final String PAY_GOODS_ID = "jinjiao";*/
    //---------------APPID  APPKEY--------------TEA--------------------------------------
    public final  static String WEIXIN_APPID = "wx7f47a7576e59f78d";
    public final  static String WEIXIN_APPKEY = "d4624c36b6795d1d99dcf0547af5443d";
    //微信支付商户ID
    public static final String MCH_ID = "1304846901";
    //微信支付API-KEY
    public final static String WEIXIN_API_KEY = "568506323feb69180874d8766776dc0d";

    public final static String QQ_APPID = "1104878445";
    public final static String QQ_APPKEY = "YR5QEXWBLhyzWdaI";
    //充值的物品ID 默认是jinjiao
    public static final String PAY_GOODS_ID = "jinjiao";
    //----------------sina--------------------------------------------
    public final static String SINA_APPID = "1706948898";
    public final static String SINA_APPKEY = "05edab9dc7d484a92a940540e0ee7ef0";

    //新版任务
    public static final String TASK_NEW_IMG = "NEWIMG";//修改头像
    public static final String TASK_NEW_PHONE = "NEWPHONE";//绑定手机号
    public static final String TASK_DAY_LOGIN = "DAYLOGIN";//每日登陆
    public static final String TASK_DAY_LOOK = "DAYLOOKLIVE";//每日观看直播
    public static final String TASK_DAY_LOOK_TIME_10 = "DAYLOOKTIME10";//每日观看直播10分钟
    public static final String TASK_DAY_LOOK_TIME_20 = "DAYLOOKTIME20";//每日观看直播20分钟
    public static final String TASK_DAY_LOOK_TIME_40 = "DAYLOOKTIME40";//每日观看直播40分钟
    public static final String TASK_DAY_LOOK_TIME_90 = "DAYLOOKTIME90";//每日观看直播40分钟
    public static final String TASK_DAY_FOLLOW_LIVE = "DAYFOLLOWLIVE";//每日关注主播
    public static final String TASK_DAY_SEND_MSG = "DAYSENDMSG";//每日发送弹幕
    public static final String TASK_DAY_GIVE_COIN_66 = "DAYGIVECOIN66";//每日赠送金币66
    public static final String TASK_DAY_GIVE_COIN_1000 = "DAYGIVECOIN1000";//每日赠送金币1000
    public static final String TASK_DAY_GIVE_JINJIAO_5 = "GIVEJINJIAO5";//赠送御用毛刷
    public static final String TASK_DAY_GIVE_JINJIAO_49 = "GIVEJINJIAO49";//赠送独角
    public static final String TASK_DAY_GIVE_JINJIAO_99 = "GIVEJINJIAO99";//赠送24K黄金盔甲
    public static final String TASK_DAY_GIVE_JINJIAO_199 = "GIVEJINJIAO199";//赠送独角兽宝宝
    public static final String TASK_SHARE_LIVE = "SHARELIVE";//分享视频到第三方
    public static final String TASK_FRIENDS_REGISTER = "FRIENDSREGISTER";//邀请好友注册
    public static final String TASK_DOWNLOAD_GAME = "DOWNLOADGAME";//下载游戏
    public static final String TASK_SHOP_PINGJIA = "SHOPPINGJIA";//应用商店评价
    public static final String TASK_JINJIAO = "GIVEJINJIAO";//统一的送金角任务

    //系统通知的几种类型
    public static final String SYSTEM_MSG_VIDEO = "video";
    public static final String SYSTEM_MSG_ACTIVITY = "activity";
    public static final String SYSTEM_MSG_OPEN = "open";
    public static final String SYSTEM_MSG_LIVE = "live";
    public static final String SYSTEM_MSG_TRAILER = "trailer";

    //判断是否为第一次登陆
    public static final String IS_FIRSTER_LOGIN = "is_first_login";

    //查询关注主播标记
    public static final int QUERY_ATTENTION_COMPERE  = 3;
    //查询关注用户
    public static final int QUERY_ATTENTION_USER = 13;
    //下载的状态值
    public static final String STATUS_PAUSE = "pause";
    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_SUCCESS ="success";
    //服务器地址
    public static final String CHAT_SERVER = "chat.gietv.cn";
    //第一次启动应用程序
    public static final String IS_FIRST_LUNCHER = "first_luncher";
    //xmpp登录密码秘钥
    public static final String KEY = "bj123!@#gie";
    public static final String COME_SOURSE = "android";
    //关注后消息内容
    public static final String COMPERE_MESSAGE = "交个朋友";

    public static final int DANMU_TEXT_SIZE = 12;
    //token默认值
    public static final String DEFAULE_TOKEN = "visitortoken";
    //第一次进关注页
    public static final String FIRST_SEE_FOLLOW_PAGE = "first_see_follow_page";
    //第一次进入订阅页
    public static final String FIRST_IN_FOLLOW_PAGE = "first_in_follow";
    //第一次进入发现页
    public static final String FIRST_IN_FIND_PAGE = "first_in_find_page";
    //第一次进入小屏播放页
    public static final String FIRST_IN_SMALL_VIDEO_PAGE = "first_in_small_video_page";
    //第一次进入作者主页
    public static final String FIRST_IN_AUTHOR_PAGE= "first_in_author_page";
    //第一次进入标签搜索页
    public static final String FIRST_IN_TAG_PAGE= "first_in_TAG_page";
    //第一次引导页的版本号
    public static final String FIRST_VERSION = "0.9.0";
}
