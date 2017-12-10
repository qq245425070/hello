package cn.gietv.mlive.modules.game.bean;

import java.io.Serializable;
import java.util.List;

import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * author：steven
 * datetime：15/9/17 14:25
 */
public class GameInfoBean implements Serializable{
    public int cnt;
    public List<GameInfoEntity> games;
    public List<UserCenterBean.UserinfoEntity> users;
    public List<String> tags;
    public static class GameInfoEntity implements Serializable {
        private static final long serialVersionUID = 1L;
        public String name;
        public String desc;
        public String img;
        public String spic;
        public String url_android;
        public int download;
        public int follows;
        public int isfollow;
        public int programnums;
        public String _id;
        public String package_name;
        public int type;
        public String gametypename;
        public String size;
        public int onlines;
        public List<String> tags;
        public UserCenterBean.UserinfoEntity author;
        public float score;
        public int score_cnt;
        public String version;
        public List<String> screenshot_imgs;
    }
}
