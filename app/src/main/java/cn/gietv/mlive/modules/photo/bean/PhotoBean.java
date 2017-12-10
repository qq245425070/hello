package cn.gietv.mlive.modules.photo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author：steven
 * datetime：15/10/14 22:53
 *
 */
public class PhotoBean implements Serializable {

    /**
     * cnt : 2
     * photos : [{"uid":"20150911","in_time":" 2015-09-11 14:22:10 ","name":"1photo","_id":"55f271f0aa45eb86c364d194","url":"asdd","desc":"as"},{"uid":"20150911","in_time":"2015-09-11 14:12:10","name":"2photo","_id":"55f271f0aa45eb86c364d122","url":"asdd","desc":"as"}]
     */
    public int cnt;
    public List<PhotosEntity> photos;

    public class PhotosEntity implements Serializable {
        /**
         * uid : 20150911
         * in_time :  2015-09-11 14:22:10
         * name : 1photo
         * _id : 55f271f0aa45eb86c364d194
         * url : asdd
         * desc : as
         */
        public String uid;
        public String in_time;
        public String name;
        public String _id;
        public String url;
        public String desc;

    }
}
