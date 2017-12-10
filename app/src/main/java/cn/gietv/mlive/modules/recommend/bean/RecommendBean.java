package cn.gietv.mlive.modules.recommend.bean;

import java.io.Serializable;
import java.util.List;

import cn.gietv.mlive.modules.game.bean.GameInfoBean;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * author：steven
 * datetime：15/9/21 10:41
 *
 */
public class RecommendBean implements Serializable {
    public List<RecommendCarouseEntity> carousels;
    public List<RecommendGameProgramsEntity> programsbygameid;
    public List<ProgramBean.ProgramEntity> programs;
    public List<RecommendAnchorEntity> users;
    public List<ProgramBean.ProgramEntity> follow;

    public static class RecommendCarouseEntity implements Serializable {
        public int index;
        public int carouseltype;
        public String carouselname;
        public String carouselimg;
        public String resourceid;
    }

    public static class RecommendGameProgramsEntity implements Serializable {
        public int index;
        public GameInfoBean.GameInfoEntity game;
        public List<ProgramBean.ProgramEntity> programs;
    }

    public static class RecommendAnchorEntity implements Serializable {
        public int index;
        public String gamename;
        public UserCenterBean.UserinfoEntity userinfo;
    }

}
