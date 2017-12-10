package cn.gietv.mlive.modules.video.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by houde on 2015/12/28.
 */
public class BlackListBean implements Serializable{
    public String roomid;
    public ArrayList<BlackName> blacklist;
    public ArrayList<Contribution> contributionlist;
    public Contribution mycontribution_coin;
    public Contribution mycontribution_jinjiao;
    public static class BlackName implements Serializable{
        public String uid;
        public long gagtime;
    }
}
