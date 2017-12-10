package cn.gietv.mlive.base;

import java.io.Serializable;

/**
 * Created by houde on 2016/8/23.
 */
public class BaseBean implements Serializable {
    public String _id;
    public String name;
    public String desc;
    public String img;
    public String spic;
    public int follows;
    public int isfollow;
    public int type;
    public int onlines;
}
