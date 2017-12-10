package cn.gietv.mlive.modules.news.bean;

import java.io.Serializable;

/**
 * Created by houde on 2015/11/27.
 */
public class NewsBean implements Serializable{
    public String userId;
    public String avatar;
    public String nickname;
    public long time;
    public String message;
    public int from_me;
    public int read;
    public String toOrFrom;
    public String attribute;
    public String type;
    public String title;
    public String content;
    public Attach attach;
    public String desc;
    public String owner;
    public static class Attach{
        public String proid;
    }
}
