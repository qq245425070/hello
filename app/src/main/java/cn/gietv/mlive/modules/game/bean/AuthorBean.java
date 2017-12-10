package cn.gietv.mlive.modules.game.bean;

import java.util.List;

import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * Created by houde on 2016/5/18.
 */
public class AuthorBean {
    public UserCenterBean.UserinfoEntity authorinfo;
    public int cnt;
    public List<String> taglist;
    public List<UserCenterBean.UserinfoEntity> userinfolist;
    public List<UserCenterBean.UserinfoEntity> authorlist;
}
