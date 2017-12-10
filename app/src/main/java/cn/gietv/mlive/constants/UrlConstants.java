package cn.gietv.mlive.constants;

/**
 * author：steven
 * datetime：15/9/16 20:21
 */
public interface UrlConstants {
    //    boolean IS_DEVELOP_MODEL = BuildConfig.DEBUG;
    boolean IS_DEVELOP_MODEL = Boolean.TRUE;

    public static interface Login {
        String URL_LOGIN = "";
    }

    public static interface Recommend {
        String URL_GET_RECOMMEND = "/page/queryhomepage.action";
    }

    public static interface Follow {
        String URL_FOLLOW_BY_CONTENT = "/user/followbyresourceid.action";
        String URL_FOLLOW_BY_PROGRAM = "/user/collectbyproid.action";
        String URL_FOLLOW_BY_USRE = "/user/followbyuserid.action";

        String URL_GET_FOLLOW_USER_BY_USERID = "/user/queryfollowuserbyuserid.action";
        String URL_GET_BE_FOLLOW_USER_BY_USERID = "/user/queryfollowsbyuserid.action";
    }

    public static interface Compere {
        String URL_GET_COMPERE_BY_USER_ID = "/user/querybyuserid.action";
    }

    public static interface Programs {
        String URL_GET_PROGRAM_LIST = "/program/querylist.action";
    }

    public static interface Game {

    }

    public static interface UserInfo {
        String URL_GET_USER_INFO_PROGRAM = "/user/querybyuserid.action";
    }

    public static interface Video {
        String URL_GET_VIDEO_PLAY_ADDRESS = "/program/requestrtmpurl.action";
    }

    public static interface Message {
        String URL_GET_MESSAGE_BY_PRO_ID = "/message/querybyproid.action";//老接口
//        String URL_GET_MESSAGE_BY_PRO_ID = "/danmuku/querybyproid.action";
        String URL_SEND_MESSAGE = "/message/messagebyproid.action";//老接口
//        String URL_SEND_MESSAGE = "/danmuku/sendbyproid.action";
        String URL_GET_TOPIC = "/chatroom/querybyproid.action";
        String URL_GET_LOGIN = "/chatroom/login.action";
    }

    public static interface Mqtt {
        //        String URL_MQTT_HOST = "tcp://portal.tvgame.cibntv.net:1883";
        //String URL_MQTT_HOST = "tcp://121.40.165.247:1883";
         String URL_MQTT_HOST = "tcp://mqtt.mlive.gietv.cn:1883";
    }

    public static interface Tag {
        String URL_QUERY_LIST_BY_TAG = "/program/querylistbytag.action";
    }
    interface Gift{
        String URL_SEND_GIFT = "/prop/sendcoinprop.action";
    }
    interface NoviceTask{
        //新手任务的URL
        String URL_SEND_NOVICETASK = "/task/novicetask.action";
        //每日任务的URL
        String URL_SEND_DAYTASK = "/task/daytask.action";
        //
        String URL_SEND_TASK = "/task/task.action";
        //任务资格查询
        String URL_QUERY_TASK = "/task/task_qualifications_query.action";
        //任务资格更新
        String URL_UPDATE_TASK ="/task/task_qualifications_update.action";

    }
}
