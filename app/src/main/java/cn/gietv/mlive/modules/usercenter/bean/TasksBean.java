package cn.gietv.mlive.modules.usercenter.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by houde on 2016/3/7.
 */
public class TasksBean implements Serializable {
    public List<TaskBean> taskqualifications;
    public static class TaskBean{
        public String _id;
        public int status;
        public String taskcode;
    }
}
