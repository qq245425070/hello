package cn.gietv.mlive.modules.video.bean;

/**
 * author：steven
 * datetime：15/10/21 12:06
 *
 */
public class MqttMessageBean {
//    public String uid;
//    public String nickname;
//    public String avatar;
//    public String news;
     public String topic;
     public String clientid;
     public String type;
     public String time;
     public ChatRoom data;

    public static class ChatRoom{
        public String uid;
        public String nickname;
        public String avatar;
        public String news;
        public String date;
        public String behavior;
        public String prop;
        public String acoinImg;
        public int acoinLevel;
        public String jinjiaoImg;
        public int jinjiaoLevel;
        public String propimg;
    }
}
