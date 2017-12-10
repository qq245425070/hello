package cn.gietv.mlive.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.home.model.NoviceTaskModel;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author: houde
 * Created by houde on 2015/11/24.
 */
public class NoviceTaskUtils {
    private static NoviceTaskModel noviceTask = RetrofitUtils.create(NoviceTaskModel.class);
    //任务资格更新
    public static void updateTask(String taskName,int num){
        if(UserUtils.isNotLogin()){

        }else{
            noviceTask.updateTask(taskName, num, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {

                }

                @Override
                public void failure(String message) {

                }
            });
        }
    }
    //完成新手任务的工具类
    public static void doNoviceTask(final String noviceTaskNum, final AbsBaseActivity activity){
        final UserCenterBean.UserinfoEntity  mUserinfoEntity = CacheUtils.getCacheUserInfo();
        if(!UserUtils.isNotLogin() && mUserinfoEntity != null) {
            if (SharedPreferenceUtils.getBoolean(mUserinfoEntity._id + noviceTaskNum, true)) {
                editTaskCount(noviceTaskNum ,activity,1,0);
                noviceTask.sendNoviceTast(noviceTaskNum, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        if (s != null) {
                            if (activity.isNotFinish()) {
                                try {
                                    ToastUtils.showToast(activity, s);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(activity, message);
                        if(message.contains("完成")){
                            editTaskCount(noviceTaskNum,activity,1,0);
                        }
                    }
                });
            }
        }
    }
    //完成每日任务
    public static void doDayTask(final String dayTaskName, final AbsBaseActivity activity){
        final UserCenterBean.UserinfoEntity  mUserinfoEntity = CacheUtils.getCacheUserInfo();
        if(!UserUtils.isNotLogin() && mUserinfoEntity != null) {
                editTaskResult(dayTaskName, activity, 1);
                noviceTask.sendDayTask(dayTaskName, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        ToastUtils.showToast(activity, s);

                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showToast(activity, message);
                        if (message.contains("完成过")) {
                            editTaskResult(dayTaskName, activity, 1);
                        }
                    }
                });
        }
    }
    //完成高级任务
    public static void doTask(final String taskName,final AbsBaseActivity activity,final int count){
        final UserCenterBean.UserinfoEntity  mUserinfoEntity = CacheUtils.getCacheUserInfo();
        if(!UserUtils.isNotLogin() && mUserinfoEntity != null) {
            if (count == 0) {
                editTaskCount(taskName, activity, 1, 0);
            } else {
                editTaskCount(taskName, activity, 0, count);
            }
            noviceTask.sendTask(taskName, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    ToastUtils.showToast(activity, s);
                }

                @Override
                public void failure(String message) {
                    ToastUtils.showToast(activity, message);
                }
            });

        }
    }
    //获取数据库中保存的每日任务
    public static long getDbData(String dayTaskName , Context context){
        if(UserUtils.isNotLogin()){
            return 0;
        }
        DBUtils dbUtils = DBUtils.getInstance(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String taskName = new StringBuffer(date).append("_").append(dayTaskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.getNumber(taskName);
    }
    public static int getNewTaskData(String dayTaskName , Context context){//获取数据库中保存的新手任务
        if(UserUtils.isNotLogin()){
            return 0;
        }
        DBUtils dbUtils = DBUtils.getInstance(context);
        String taskName = new StringBuffer().append(dayTaskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.getTaskResult(taskName);
    }
    public static boolean saveTask(String taskName,long number,int res,Context context){
        if(UserUtils.isNotLogin()){
            return false;
        }
        DBUtils dbUtils = DBUtils.getInstance(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String mTaskName = new StringBuffer(date).append("_").append(taskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.saveTask(mTaskName,number,res);
    }
    //获取数据库中是否保存每日任务
    public static boolean getData(String dayTaskName , Context context){
        DBUtils dbUtils = DBUtils.getInstance(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String taskName = new StringBuffer(date).append("_").append(dayTaskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.getData(taskName);
    }
    //向数据库中保存观看时间或者是发言次数
    public static boolean saveCount(String dayTaskName , Context context ,long count){
        DBUtils dbUtils = DBUtils.getInstance(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String taskName = new StringBuffer(date).append("_").append(dayTaskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.saveTask(taskName, count);
    }
    //获得任务领取的状态
    public static int getTaskResult(String dayTaskName , Context context ){
        if(UserUtils.isNotLogin()){
            return 0;
        }
        DBUtils dbUtils = DBUtils.getInstance(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String taskName = new StringBuffer(date).append("_").append(dayTaskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.getTaskResult(taskName);
    }
    //修改任务的领取状态
    public static boolean editTaskResult(String dayTaskName , Context context ,int result){
        DBUtils dbUtils = DBUtils.getInstance(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String taskName = new StringBuffer(date).append("_").append(dayTaskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.editTeskResult(taskName, result);
    }
    public static int getTaskCount(String taskName , Context context ){
        if(UserUtils.isNotLogin()){
            return 0;
        }
        DBUtils dbUtils = DBUtils.getInstance(context);
        String mTaskName = new StringBuffer().append(taskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.getTaskCount(mTaskName);
    }
    public static boolean editTaskCount(String taskName , Context context ,int result,int count){
        if(UserUtils.isNotLogin()){
            return false;
        }
        DBUtils dbUtils = DBUtils.getInstance(context);
        String mTaskName = new StringBuffer().append(taskName).append("_").append(CacheUtils.getCacheUserInfo()._id).toString();
        return dbUtils.editTaskCount(mTaskName, count, result);
    }
}
