package cn.gietv.mlive.modules.video.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by houde on 2016/1/26.
 */
public class PropBeanList  implements Serializable{
    public List<PropBean> props;
    public static class PropBean implements Serializable{
        public String _id;
        public String bulletscreen_color;
        public String desc;
        public String effect;
        public String img;
        public int index;
        public String name;
        public double price;
        public int status;
        public String type;
        public String bgimg;
    }
}
