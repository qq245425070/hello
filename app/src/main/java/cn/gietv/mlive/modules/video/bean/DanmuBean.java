package cn.gietv.mlive.modules.video.bean;

import java.util.List;

/**
 * Created by houde on 2016/7/26.
 */
public class DanmuBean {
    public int cnt;
    public List<DanmuEntity> messages;
    public static class DanmuEntity{
        public String _id;
        public String avatar;
        public String date;
        public String nickname;
        public String resourceid;
        public String message;
        public String uid;
        public long videotimestamp;
    }
}
