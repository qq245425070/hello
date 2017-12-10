package cn.gietv.mlive.modules.compere.bean;

import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * Created by houde on 2016/5/18.
 */
public class AlbumBean {
    public int cnt;
    public List<GameInfoBean.GameInfoEntity> albumlist;
    public UserCenterBean.UserinfoEntity authorinfo;
    public List<String> taglist;
    public List<UserCenterBean.UserinfoEntity> authorlist_taglist;
}
