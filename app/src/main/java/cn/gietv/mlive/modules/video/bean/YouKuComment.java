package cn.gietv.mlive.modules.video.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：houde on 2016/12/15 16:57
 * 邮箱：yangzhonghao@gietv.com
 */
public class YouKuComment implements Serializable {
    public String video_id;
    public String total;
    public int page;
    public int count;
    public List<CommentBean> comments;
    public static class CommentBean implements Serializable{
        public String content;
        public String id;
        public String published;
        public User user;
//        SourseBean sourse;
    }
//    public class SourseBean implements Serializable{
//        String link;
//        String name;
//    }
    public static class User implements Serializable{
        public int id;
        public String link;
        public String name;
    }
}
