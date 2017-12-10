package cn.gietv.mlive.modules.video.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author：steven
 * datetime：15/10/8 22:13
 *
 */
public class MessageBean {

    public int cnt;
    public List<MessagesEntity> messages;
    public Scoreratio scoreratio;

    public static class Scoreratio implements Serializable{
        public String _id;
        public int score_1;
        public int score_2;
        public int score_3;
        public int score_4;
        public int score_5;
        public float score;
        public int score_cnt;

    }
    public static class MessagesEntity implements Serializable{
        public String _id;
        public String uid;
        public String nickname;
        public String avatar;
        public String message;
        public String date;
        public String prop;
        public String topic;
        public String clientid;
        public String type;
        public String behavior;
        public String acoinImg;
        public int acoinLevel;
        public String jinjiaoImg;
        public int jinjiaoLevel;
        public String propimg;
        public int videotimestamp;
        public float score;
        public String isreply;
        public String mid;
        public ReplyUser replyuser;
        public int islikes;
        public int likes_cnt;
        public int isyouku;
    }
    public static class ReplyUser implements Serializable{
        public String _id;
        public String nickname;
    }

}
