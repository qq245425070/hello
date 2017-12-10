package cn.gietv.mlive.modules.search.bean;

import java.util.List;

/**
 * author：steven
 * datetime：15/10/31 15:20
 * email：liuyingwen@kalading.com
 */
public class SearchBean {
    public int cnt;
    public List<SearchEnitity> search;

    public static class SearchEnitity {
        public String _id;
        public int type;
        public String icon;
        public String name;
        public String desc;
        public String nickname;
        public int onlines;
        public int programnums;
        public int follows;
        public int isfollow;
    }
}
