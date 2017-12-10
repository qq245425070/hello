package cn.gietv.mlive.modules.video.bean;

import java.util.List;

/**
 * Created by houde on 2015/12/10.
 */
public class MessageBeanOld {
    public int cnt;
    public List<MessagesEntity> messages;

    public static class MessagesEntity {
        public String nickname;
        public String _id;
        public String avatar;
        public String message;
    }
}
