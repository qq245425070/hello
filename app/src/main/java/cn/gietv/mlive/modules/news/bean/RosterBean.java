package cn.gietv.mlive.modules.news.bean;

import java.io.Serializable;

/**
 * Created by houde on 2015/12/19.
 */
public class RosterBean implements Serializable {
    public String userId;
    public String nickname;
    public String avatar;
    public long time;
    public String message;
    public int count;
    public String read;
    public String owner;
}
