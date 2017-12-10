package cn.gietv.mlive.modules.recommend.bean;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

import java.io.Serializable;
import java.util.List;

/**
 * author：steven
 * datetime：15/9/17 20:09
 *
 */
public class LiveHotBean implements Serializable{
        public String _id;
        public int type;
        public String name;
        public String desc;
        public String spic;
        public String url;
        public int follows;
        public int onlines;
        public int status;
        public List<String> tags;
        public List<String> category_list;
        public int collect;
        public int isfollow;
        public String shareurl;
        public UserCenterBean.UserinfoEntity userinfo;
        public GameInfoBean.GameInfoEntity game;

        @Override
        public String toString() {
            return name;
        }
}
