package cn.gietv.mlive.utils;

import android.content.Context;

import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.video.activity.LivePlayActivity;
import cn.gietv.mlive.modules.video.bean.PropAcoinBean;
import cn.gietv.mlive.modules.video.bean.PropJinjiaoBean;
import cn.gietv.mlive.modules.video.fragment.LiveMessageFragment;
import cn.gietv.mlive.modules.video.model.GiftModel;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/2/1.
 */
public class GiveGiftUtil {
    public static final int DEFAULT_CPNUM = 1;
    public static void giveGiftAcoin(String type,String userID,final LiveMessageFragment fragment,final int count , final String propName, final String programId,final String propimg){
                GiftModel mGiftModel = RetrofitUtils.create(GiftModel.class);
                mGiftModel.sendGift(type, DEFAULT_CPNUM, count, userID, new DefaultLiveHttpCallBack<PropAcoinBean>() {
                    @Override
                    public void success(PropAcoinBean propAcoinBean) {
                        if (propAcoinBean.mycontribution_coin != null) {
                          //  CacheUtils.saveMyContributionCoin(propAcoinBean.mycontribution_coin, programId);
                        }
                        UserCenterBean.UserinfoEntity mUserinfo = CacheUtils.getCacheUserInfo();
                        mUserinfo.myjinjiao = propAcoinBean.mycoin;
                        CacheUtils.saveUserInfo(mUserinfo);
                        //if (fragment.isConnected()) {
                            fragment.sendMessage(ConfigUtils.CHAT_ROOM_SEND_GIFT, ConfigUtils.SEND_MSG_GIFT,  propName,propimg);
                        //}
                        if ("666金币".equals(propName)) {
                            NoviceTaskUtils.updateTask(ConfigUtils.TASK_DAY_GIVE_COIN_66,1);
                        }

                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(fragment.getActivity(), message);
                    }
                });
    }
    public static void giveGiftJinjao(String propId,String userID,final LiveMessageFragment fragment, final int count,final String proName,String programId,final String propimg){
            GiftModel model = RetrofitUtils.create(GiftModel.class);
            model.sendRMBProp(propId, DEFAULT_CPNUM, count, userID, new DefaultLiveHttpCallBack<PropJinjiaoBean>() {
                @Override
                public void success(PropJinjiaoBean givePropBean) {
                    UserCenterBean.UserinfoEntity mUserinfo = CacheUtils.getCacheUserInfo();
                    mUserinfo.myjinjiao = givePropBean.myjinjiao;
                    CacheUtils.saveUserInfo(mUserinfo);
                    fragment.sendMessage(ConfigUtils.CHAT_ROOM_SEND_GIFT, ConfigUtils.SEND_MSG_GIFT, proName, propimg);
                    saveTaskData(count, fragment.getActivity());
                    NoviceTaskUtils.updateTask(ConfigUtils.TASK_JINJIAO,count * 10);
                }

                @Override
                public void failure(String message) {
                    ToastUtils.showToast(fragment.getActivity(), message);
                }
            });
    }
    public static void giveGiftAcoin(String type,String userID,final LivePlayActivity activity,final int count , final String propName, final String programId,final String propimg){
        GiftModel mGiftModel = RetrofitUtils.create(GiftModel.class);
        mGiftModel.sendGift(type, DEFAULT_CPNUM, count, userID, new DefaultLiveHttpCallBack<PropAcoinBean>() {
            @Override
            public void success(PropAcoinBean propAcoinBean) {
                if (propAcoinBean.mycontribution_coin != null) {
                    //CacheUtils.saveMyContributionCoin(propAcoinBean.mycontribution_coin, programId);
                }
                UserCenterBean.UserinfoEntity mUserinfo = CacheUtils.getCacheUserInfo();
                mUserinfo.myjinjiao = propAcoinBean.mycoin;
                CacheUtils.saveUserInfo(mUserinfo);
//                activity.sendMessage(ConfigUtils.CHAT_ROOM_SEND_GIFT, ConfigUtils.SEND_MSG_GIFT, propName,propimg);
                if ("666金币".equals(propName)) {
                    NoviceTaskUtils.updateTask(ConfigUtils.TASK_DAY_GIVE_COIN_66,1);
                }
            }

            @Override
            public void failure(String message) {
//                ToastUtils.showToast(activity, message);
            }
        });
    }
    public static void giveGiftJinjao(String propId,String userID,final LivePlayActivity fragment, final int count,final String proName,String programId,final String propimg){
        GiftModel model = RetrofitUtils.create(GiftModel.class);
        model.sendRMBProp(propId, DEFAULT_CPNUM, count, userID, new DefaultLiveHttpCallBack<PropJinjiaoBean>() {
            @Override
            public void success(PropJinjiaoBean givePropBean) {
                UserCenterBean.UserinfoEntity mUserinfo = CacheUtils.getCacheUserInfo();
                mUserinfo.myjinjiao = givePropBean.myjinjiao;
                CacheUtils.saveUserInfo(mUserinfo);
//                fragment.sendMessage(ConfigUtils.CHAT_ROOM_SEND_GIFT, ConfigUtils.SEND_MSG_GIFT, proName, propimg);
//                saveTaskData(count, fragment);
                NoviceTaskUtils.updateTask(ConfigUtils.TASK_JINJIAO, count * 10);
            }

            @Override
            public void failure(String message) {
//                ToastUtils.showToast(fragment, message);
            }
        });
    }
    public static void saveTaskData(int number,Context context){
        if(number == 199){
            if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199, context) == 0){
                int  count= NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,context);
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,context,0,count + 1);
            }else{
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_199,context,0,1);
            }
        }else if(number == 99){
            if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99, context) == 0){
                int count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,context);
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,context,0,count + 1);
            }else{
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_99,context,0,1);
            }
        }else if(number == 49){
            if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49, context) == 0){
                int count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,context);
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,context,0,count + 1);
            }else{
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_49,context,0,1);
            }
        }else if(number == 5){
            if(NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5, context) == 0){
                int count = NoviceTaskUtils.getTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,context);
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,context,0,count + 1);
            }else{
                NoviceTaskUtils.editTaskCount(ConfigUtils.TASK_DAY_GIVE_JINJIAO_5,context,0,1);
            }
        }
    }
}
