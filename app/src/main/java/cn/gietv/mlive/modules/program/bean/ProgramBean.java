package cn.gietv.mlive.modules.program.bean;

import java.io.Serializable;
import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * author：steven
 * datetime：15/9/17 20:09
 *
 */
public class ProgramBean {
    public int cnt;
    public List<ProgramEntity> programs;
    public List<String> taglist;
    public GameInfoBean.GameInfoEntity albuminfo;
    public List<GameInfoBean.GameInfoEntity> albumlist_taglist;
    public int isfollow;

    public static class ProgramEntity implements Serializable {
        public String _id;
        public int type;
        public String name;
        public String desc;
        public String spic;
        public String img;
        public String url;
        public int follows;
        public int onlines;
        public int status;
        public int position=0;
        public int message_cnt;
        public List<String> tags;
        public int collect;
        public int isfollow;
        public String shareurl;
        public UserCenterBean.UserinfoEntity userinfo;
        public String fanstitle;
        public boolean check;
        public String urlfrom;
    }
}
