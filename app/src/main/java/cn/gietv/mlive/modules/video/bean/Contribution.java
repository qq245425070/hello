package cn.gietv.mlive.modules.video.bean;

import java.io.Serializable;

import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * Created by houde on 2016/8/22.
 */
public class Contribution implements Serializable {
    private static final long serialVersionUID = 1L;
    public String _id;
    public int contribution;
    public int level;
    public String levelimg;
    public int ranking;
    public String coin;
    public UserCenterBean.UserinfoEntity userinfo;
}
