package cn.gietv.mlive.modules.xmpp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.Random;

import cn.gietv.mlive.R;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.modules.home.activity.HomeActivity;
import cn.gietv.mlive.modules.news.activity.NewsInfoActivity;
import cn.gietv.mlive.modules.news.bean.NewsBean;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.PackageUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.GsonUtils;

/**
 * Created by houde on 2016/3/16.
 */
public class UserChatManager implements ChatManagerListener {
    private Context mContext;
    private static UserChatManager userChatManager ;
    public static UserChatManager getInstance(Context context){
        if(userChatManager == null){
            userChatManager = new UserChatManager(context);
        }
        return userChatManager;
    }
    private UserChatManager(Context context){
        this.mContext = context;
    }
    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                //登录用户
                String username = XmppConnection.getInstance().getConnection().getUser();
                //发送用户
                String fromUsername = message.getFrom();
                //用户内容
                String messageBody = message.getBody();
                if(messageBody == null || messageBody.length() ==0){
                    return ;
                }
                System.out.println("messageBody    " + messageBody);
                //NewsBean newsBean = GsonUtils.getGson().fromJson(messageBody,NewsBean.class);
                parseJsonAndSaveMsg(messageBody);
            }
        });
    }
    private void saveNotReadMessage(NewsBean newsBean) {
        DBUtils dbUtils = DBUtils.getInstance(mContext.getApplicationContext());
        Uri uri = Uri.parse("content://cn.gietv.mlive.RosterProvider/roster");
        ContentValues values = new ContentValues();
        if(dbUtils.getRoster(newsBean.userId)){
            values.put("count",dbUtils.getNotReadCount(newsBean.userId) + 1);
            values.put("owner",newsBean.owner);
            values.put("message",newsBean.message);
            values.put("nickname",newsBean.nickname);
            values.put("avatar",newsBean.avatar);
            mContext.getContentResolver().update(uri,values,"userId = ?",new String[]{newsBean.userId});
        }else {
            values.put("userId", newsBean.userId);
            values.put("avatar",newsBean.avatar);
            values.put("nickname",newsBean.nickname);
            values.put("time",newsBean.time);
            values.put("message",newsBean.message);
            values.put("count",1);
            values.put("owner",newsBean.owner);
            mContext.getContentResolver().insert(uri, values);
        }
    }
    public void parseJsonAndSaveMsg(String message){
        Gson gson = GsonUtils.getGson();
        Log.e("ceshi",message);
        NewsBean newsBean = null;
        try {

            newsBean = gson.fromJson(message, NewsBean.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        UserCenterBean.UserinfoEntity userinfo = CacheUtils.getCacheUserInfo();
        if(userinfo != null) {
            if ((userinfo._id).equals(newsBean.userId)) {
                return;
            }
            if ("open".equals(newsBean.type) || "trailer".equals(newsBean.type)) {
                if (!DBUtils.getInstance(mContext.getApplicationContext()).queryAttentionAnchor(newsBean.userId)) {
                    return;
                }
            }
        }
            newsBean.toOrFrom = newsBean.userId;
            newsBean.read = ConfigUtils.MESSAGE_NOT_READ;
            newsBean.from_me = ConfigUtils.FROM_OTHER;
            ContentResolver resolver =  mContext.getContentResolver();
            Uri insertUri = Uri.parse("content://cn.gietv.mlive.NewsProvider/message");
            ContentValues contentValues = new ContentValues();
            contentValues.put("userId",newsBean.userId);
            contentValues.put("nickname" , newsBean.nickname);
            contentValues.put("avatar",newsBean.avatar);
            contentValues.put("message",(newsBean.message==null)? newsBean.content: newsBean.message);
            contentValues.put("from_me",newsBean.from_me);
            contentValues.put("read",newsBean.read);
            contentValues.put("time", newsBean.time);
            contentValues.put("to_from", newsBean.toOrFrom);
            contentValues.put("type",newsBean.type);
            contentValues.put("title",newsBean.title);
            contentValues.put("desc",newsBean.desc);
            contentValues.put("content",newsBean.content);
            contentValues.put("proid",(newsBean.attach==null)?"":newsBean.attach.proid);
            if(!UserUtils.isNotLogin()){
                contentValues.put("owner",CacheUtils.getCacheUserInfo()._id);
                newsBean.owner = CacheUtils.getCacheUserInfo()._id;
                System.out.println("owner      "+CacheUtils.getCacheUserInfo()._id);
            }else{
                contentValues.put("owner","nologin");
            }
            resolver.insert(insertUri, contentValues);
            saveNotReadMessage(newsBean);
            sendNotification(newsBean);
    }
    private void sendNotification(NewsBean newsBean) {
        if(!PackageUtils.isAppOnForeground(mContext.getApplicationContext())){
            Log.e("ceshi","进来通知栏提示");
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
            PendingIntent pendingIntent = PendingIntent.getActivities(mContext.getApplicationContext(), 0, makeIntentStack(mContext.getApplicationContext(),newsBean), 0);
            Notification notify =  mBuilder.build();
            notify.icon = R.mipmap.icon;
            notify.tickerText = newsBean.nickname + " : " + newsBean.message;
            notify.when = System.currentTimeMillis();
            notify.contentIntent = pendingIntent;
//            notify.setLatestEventInfo(mContext.getApplicationContext(), newsBean.nickname, (newsBean.message == null) ? newsBean.content : newsBean.message, pendingIntent);
            notify.number = 1;
            notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
            // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
            //添加声音
            notify.sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.tishi);
            //添加震动
            notify.defaults |= Notification.DEFAULT_VIBRATE;
            long[] vibrate = {0,100};
            notify.vibrate = vibrate ;
            if(newsBean.type == null){
                manager.notify(1, notify);
            }else{
                manager.notify(new Random().nextInt(1000),notify);
            }
        }
    }

    private Intent[] makeIntentStack(Context context,NewsBean newsBean) {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context,HomeActivity.class));
        if(ConfigUtils.SYSTEM_MSG_VIDEO.equals(newsBean.type) || ConfigUtils.SYSTEM_MSG_OPEN.equals(newsBean.type)){
            Log.e("ceshi",newsBean.userId);
            intents[1] = new Intent(context, NewsInfoActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putString(LivePlayListActivity.ROOM_ID,newsBean.attach.proid);
            intents[1].putExtras(bundle);
            return intents;
        }else{
            Log.e("ceshi",newsBean.userId +"     ..........");
            intents[1] = new Intent(context, NewsInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(NewsInfoActivity.NICK_NAME, newsBean.nickname);
            bundle.putString(NewsInfoActivity.USER_ID, newsBean.userId);
            bundle.putString(NewsInfoActivity.AVATAR, newsBean.avatar);
            Log.e("ceshi",bundle.toString());
            intents[1].putExtras(bundle);
            return intents;
        }
    }
}
