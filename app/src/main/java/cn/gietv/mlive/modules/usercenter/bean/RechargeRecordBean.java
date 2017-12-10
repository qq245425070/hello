package cn.gietv.mlive.modules.usercenter.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by houde on 2016/3/24.
 */
public class RechargeRecordBean implements Serializable {
    public List<RecordBean> recharges;
    public int cnt;
   public static class RecordBean{
        public String _id;
        public String desc;
        public String num;
        public String paytime;
        public String title;
    }
}
