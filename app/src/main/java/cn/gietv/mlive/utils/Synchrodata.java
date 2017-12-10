package cn.gietv.mlive.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.List;

import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.news.bean.RosterBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.usercenter.model.UserCenterModel;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * Created by houde on 2016/7/11.
 */
public class Synchrodata {
    private List<RosterBean> mData;
    private Context context;
    private static Synchrodata synchrodata ;
    private UserCenterModel model;
    private DBUtils dbUtils;
    private Synchrodata(Context context){
        this.context = context;
        model = RetrofitUtils.create(UserCenterModel.class);
        dbUtils = DBUtils.getInstance(context);
    }
    public static Synchrodata getInstance(Context context){
        if(synchrodata == null)
            synchrodata = new Synchrodata(context);
        return synchrodata;
    }
    public void synchrodata(List<RosterBean> rosterBeans){
        this.mData = rosterBeans;
        if(mData == null || mData.size() == 0){
            return;
        }

        for(int i = 0; i< rosterBeans.size() ; i++){
            final int finalI = i;
            model.getUserInfo(mData.get(i).userId, 0, 1, 1, new DefaultLiveHttpCallBack<UserCenterBean>() {
                @Override
                public void success(UserCenterBean userCenterBean) {
                    if(userCenterBean == null || userCenterBean.userinfo == null){
                        return ;
                    }
                    updateDatabase(mData.get(finalI).avatar, mData.get(finalI).nickname, userCenterBean.userinfo.avatar, userCenterBean.userinfo.nickname, userCenterBean.userinfo._id);
                }

                @Override
                public void failure(String message) {

                }
            });
        }
    }

    private void updateDatabase(String avatar,String nickname,String currentAvatar,String currentNickanme,String userid) {
        if(!avatar.equals(currentAvatar) || !nickname.equals(currentNickanme)){
           new MyTask(currentAvatar,currentNickanme,userid).execute();
        }
    }
    class MyTask extends AsyncTask{
        private String mAvatar,mNickname,mUserID;
        public MyTask(String avatar,String nickname,String userid){
            this.mAvatar = avatar;
            this.mNickname = nickname;
            this.mUserID = userid;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            ContentValues values = new ContentValues();
            Uri uri = Uri.parse("content://cn.gietv.mlive.RosterProvider/roster");
            values.put("avatar",mAvatar);
            values.put("nickname", mNickname);
            context.getContentResolver().update(uri, values, "userId = ?", new String[]{mUserID});
            uri = Uri.parse("content://cn.gietv.mlive.NewsProvider/message");
            context.getContentResolver().update(uri,values,"userId = ?",new String[]{mUserID});
//            dbUtils.updateRoster(mAvatar , mNickname , mUserID);
//            dbUtils.updateMessageInfo(mAvatar , mNickname , mUserID);
            return null;
        }
    }
}
