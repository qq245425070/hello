package cn.gietv.mlive.modules.search.bean;

import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * Created by houde on 2016/5/31.
 */
public class TagSearchBean {
    public List<GameInfoBean.GameInfoEntity> albumlist;
    public List<ProgramBean.ProgramEntity> programs;
    public List<UserCenterBean.UserinfoEntity> authorlist;
}
