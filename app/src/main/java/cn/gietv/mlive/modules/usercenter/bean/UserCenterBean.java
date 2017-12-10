package cn.gietv.mlive.modules.usercenter.bean;

import java.io.Serializable;
import java.util.List;

import cn.gietv.mlive.modules.program.bean.ProgramBean;

/**
 * author：steven
 * datetime：15/10/8 13:42
 *
 */

public class UserCenterBean {
    public int cnt;
    public List<ProgramBean.ProgramEntity> programs;
    public UserinfoEntity userinfo;

    public static class UserinfoEntity implements Serializable{
        public UserinfoEntity(){}
        /**
         * game : 5
         * myfollow : 4
         * gender : 0
         * nickname : 测试用户1
         * follows : 1
         * photo : 3
         * id : 55f26d85aa45eb86c364d191
         * avatar : 12
         * video : 1
         */
        //    _id,nickname,onlines,follows,myfollows,gender,photo,avatar,desc,email,token;
        private static final long serialVersionUID = 1L;
        public String spic;
        public int game;
        public int myfollow;//myfollows
        public int gender;//gender
        public String nickname;//nickname
        public int follows;//follows
        public int photo;//photo
        public String _id;//_id
        public String avatar;//avatar
        public int video;
        public String desc;
        public int isfollow;
        public String email;
        public long mycoin;
        public String mobilephone;
        public long myjinjiao;
        public int type;
        public int onlines;//onlines
        public List<String> tags;
    }
}
