package com.houde.competitive.lagua.model.recommend;

import com.houde.competitive.lagua.contrart.recommend.RecommendContract;
import com.houde.competitive.lagua.model.bean.RecommendBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class RecommendModel implements RecommendContract.IRecommendModel {
    public static RecommendModel newInstance(){
        return new RecommendModel();
    }
    @Override
    public List<RecommendBean> getData() {
        return this.getRecommendBeanList();
    }
    public List<RecommendBean> getRecommendBeanList(){
        List<RecommendBean> beanList = new ArrayList<>();
        beanList.add(createRecommendBean(RecommendBean.TYPE_HEADER));
        beanList.add(createRecommendBean(RecommendBean.TYPE_ARTICLE));
        beanList.add(createRecommendBean(RecommendBean.TYPE_IMAGE));
        beanList.add(createRecommendBean(RecommendBean.TYPE_SATINS));
        beanList.add(createRecommendBean(RecommendBean.TYPE_ARTICLE));
        beanList.add(createRecommendBean(RecommendBean.TYPE_IMAGE));
        beanList.add(createRecommendBean(RecommendBean.TYPE_SATINS));
        return beanList;
    }
    private RecommendBean createRecommendBean(int type){
        RecommendBean bean = new RecommendBean();
        bean.id = new Random().nextInt(100);
        switch (type){
            case RecommendBean.TYPE_HEADER:
                bean.type = 3;
                break;
            case RecommendBean.TYPE_ARTICLE:
                bean.title = "豪车背后的故事";
                bean.type = 1;
                bean.imagePaths = new ArrayList<>();
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg");
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg");
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20180102083655_3t4ytm_Screenshot.jpeg");
                bean.imagePath = "http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg";
                break;
            case RecommendBean.TYPE_IMAGE:
                bean.title = "美女欣赏图片";
                bean.type = 0;
                bean.imagePaths = new ArrayList<>();
                bean.imagePath = "http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg";
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg");
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg");
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20180102083655_3t4ytm_Screenshot.jpeg");
                break;
            case RecommendBean.TYPE_SATINS:
                bean.title = "狗狗的快乐生活";
                bean.type = 2;
                bean.imagePaths = new ArrayList<>();
                bean.imagePath = "http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg";
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20171227115959_lmlLZ3_Screenshot.jpeg");
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20171219224721_wFH5PL_Screenshot.jpeg");
                bean.imagePaths.add("http://7xi8d6.com1.z0.glb.clouddn.com/20171219115747_tH0TN5_Screenshot.jpeg");
                break;
            default:
                break;
        }
        return bean;
    }
}
