package cn.gietv.mlive.modules.usercenter.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by houde on 2016/1/29.
 */
public class ProductBeanList implements Serializable{
    public List<ProductBean> recharges;
    public static class ProductBean implements Serializable{
        public String _id;
        public String desc;
        public String name;
        public int index;
        public int price;
        public int status;
    }
}
