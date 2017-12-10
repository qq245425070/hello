package cn.gietv.mlive.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
//import cn.steven.mqtt.MqttController;
//import cn.steven.mqtt.MqttHandler;

/**
 * Created by houde on 2015/12/15.
 */
public class MqttService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//    private MqttController mqttController;
//    private Binder mMqttBinder = new MqttBinder();
//    @Override
//    public IBinder onBind(Intent intent) {
//       return mMqttBinder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        if(mqttController == null){
//            mqttController = new MqttController(UrlConstants.Mqtt.URL_MQTT_HOST, VersionUtils.getDeviceId(MainApplication.getInstance()), "", new MyMqttHandler());
//            mqttController.connect();
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, START_STICKY, startId);
//    }
//
//    @Override
//    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
//        return super.bindService(service, conn, flags);
//    }
//
//    @Override
//    public void unbindService(ServiceConnection conn) {
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Intent receiver = new Intent();
//        receiver.setAction("cn.gietv.mqttservice.destroy");
//        this.sendBroadcast(receiver);
//        System.err.println("service被销毁");
//    }
//
//    public MqttController getMqttController(){
//        return mqttController;
//    }
//    public class MyMqttHandler extends MqttHandler {
//
//        @Override
//        public void connectionLost(Throwable t) {
//            System.out.println("connectionLost............");
//        }
//
//        @Override
//        public void deliveryComplete(IMqttDeliveryToken token) {
//
//        }
//
//
//     /*   @Override
//        public void messageArrivedPrivateChat(String message) throws Exception {
//            parseJsonAndSaveMsg(message);
//            Gson gson = GsonUtils.getGson();
//            NewsBean newsBean = gson.fromJson(message, NewsBean.class);
//            UserCenterBean.UserinfoEntity userinfo = CacheUtils.getCacheUserInfo();
//            if(userinfo != null){
//                if((userinfo._id).equals(newsBean.toOrFrom)){
//                    return ;
//                }
//                newsBean.toOrFrom = newsBean.userId;
//                newsBean.read = ConfigUtils.MESSAGE_NOT_READ;
//                newsBean.from_me = ConfigUtils.FROM_OTHER;
//                ContentResolver resolver =  getContentResolver();
//                Uri insertUri = Uri.parse("content://cn.gietv.mlive.NewsProvider/message");
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("userId",newsBean.userId);
//                contentValues.put("nickname" , newsBean.nickname);
//                contentValues.put("avatar",newsBean.avatar);
//                contentValues.put("message",newsBean.message);
//                contentValues.put("from_me",newsBean.from_me);
//                contentValues.put("read",newsBean.read);
//                contentValues.put("time", newsBean.time);
//                contentValues.put("to_from", newsBean.toOrFrom);
//                resolver.insert(insertUri, contentValues);
//                saveNotReadMessage(newsBean);
//                sendNotification(newsBean);
//            }
//        }*/
//
//
//        @Override
//        public void messageArrivedBlackList(String message) throws Exception {
//            if(parseJson != null) {
//                parseJson.messageArrivedBlackList(message);
//            }
//        }
//
//        @Override
//        public void messageArrivedPay(String message) throws Exception {
//            if(parsePayListener != null){
//                parsePayListener.messageArrivedPay(message);
//            }
//        }
//
//        @Override
//        public void messageArrivedNotice(String message) throws Exception {
//            if(noticeListener != null){
//                noticeListener.messageArrivedNotice(message);
//            }
//        }
//
//        @Override
//        public void messageArrivedSystem(String message) throws Exception {
//            JSONObject jsonObject = new JSONObject(message);
//            parseJsonAndSaveMsg(jsonObject.getString("data"));
//        }
//
//        @Override
//        public void connectSuccess() {
//            System.out.println("服务器连接成功.........................");
//            if(conntectSuccess != null) {
//                conntectSuccess.mqttClientConnectSuccess();
//            }
//        }
//    }
//
//    private void saveNotReadMessage(NewsBean newsBean) {
//        DBUtils dbUtils = DBUtils.getInstance(getApplicationContext());
//        Uri uri = Uri.parse("content://cn.gietv.mlive.RosterProvider/roster");
//        ContentValues values = new ContentValues();
//        if(dbUtils.getRoster(newsBean.userId)){
//            values.put("count",dbUtils.getNotReadCount(newsBean.userId) + 1);
//            values.put("message",newsBean.message);
//            getContentResolver().update(uri,values,"userId = ?",new String[]{newsBean.userId});
//        }else {
//            values.put("userId", newsBean.userId);
//            values.put("avatar",newsBean.avatar);
//            values.put("nickname",newsBean.nickname);
//            values.put("time",newsBean.time);
//            values.put("message",newsBean.message);
//            values.put("count",1);
//            getContentResolver().insert(uri, values);
//        }
//    }
//
//    private void sendNotification(NewsBean newsBean) {
//        if(!PackageUtils.isAppOnForeground(getApplicationContext())){
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, makeIntentStack(getApplicationContext(),newsBean), 0);
//            Notification notify = new Notification();
//            notify.icon = R.mipmap.icon;
//            notify.tickerText = newsBean.nickname + " : " + newsBean.message;
//            notify.when = System.currentTimeMillis();
//            notify.setLatestEventInfo(getApplicationContext(), newsBean.nickname, (newsBean.message == null) ? newsBean.content : newsBean.message, pendingIntent);
//            notify.number = 1;
//            notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
//            // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
//            //添加声音
//            notify.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tishi);
//            //添加震动
//            notify.defaults |= Notification.DEFAULT_VIBRATE;
//            long[] vibrate = {0,100};
//            notify.vibrate = vibrate ;
//            if(newsBean.type == null){
//                manager.notify(1, notify);
//            }else{
//                manager.notify(new Random().nextInt(1000),notify);
//            }
//        }
//    }
//
//    private Intent[] makeIntentStack(Context context,NewsBean newsBean) {
//        Intent[] intents = new Intent[2];
//        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context,HomeActivity.class));
//        if(ConfigUtils.SYSTEM_MSG_VIDEO.equals(newsBean.type) || ConfigUtils.SYSTEM_MSG_OPEN.equals(newsBean.type)){
//            intents[1] = new Intent(context, NewsInfoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(LivePlayListActivity.ROOM_ID,newsBean.attach.proid);
//            intents[1].putExtras(bundle);
//            return intents;
//        }else{
//            intents[1] = new Intent(context, NewsInfoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(NewsInfoActivity.NICK_NAME, newsBean.nickname);
//            bundle.putString(NewsInfoActivity.USER_ID, newsBean.userId);
//            bundle.putString(NewsInfoActivity.AVATAR, newsBean.avatar);
//            intents[1].putExtras(bundle);
//            return intents;
//        }
//    }
//    private void parseJsonAndSaveMsg(String message){
//        Gson gson = GsonUtils.getGson();
//        NewsBean newsBean = gson.fromJson(message, NewsBean.class);
//        UserCenterBean.UserinfoEntity userinfo = CacheUtils.getCacheUserInfo();
//        if(userinfo != null) {
//            if ((userinfo._id).equals(newsBean.userId)) {
//                return;
//            }
//            if("open".equals(newsBean.type) || "trailer".equals(newsBean) ){
//                if(!DBUtils.getInstance(getApplicationContext()).queryAttentionAnchor(newsBean.userId)){
//                    return;
//                }
//            }
//            newsBean.toOrFrom = newsBean.userId;
//            newsBean.read = ConfigUtils.MESSAGE_NOT_READ;
//            newsBean.from_me = ConfigUtils.FROM_OTHER;
//            ContentResolver resolver =  getContentResolver();
//            Uri insertUri = Uri.parse("content://cn.gietv.mlive.NewsProvider/message");
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("userId",newsBean.userId);
//            contentValues.put("nickname" , newsBean.nickname);
//            contentValues.put("avatar",newsBean.avatar);
//            contentValues.put("message",(newsBean.message==null)? newsBean.content: newsBean.message);
//            contentValues.put("from_me",newsBean.from_me);
//            contentValues.put("read",newsBean.read);
//            contentValues.put("time", newsBean.time);
//            contentValues.put("to_from", newsBean.toOrFrom);
//            contentValues.put("type",newsBean.type);
//            contentValues.put("title",newsBean.title);
//            contentValues.put("desc",newsBean.desc);
//            contentValues.put("content",newsBean.content);
//            contentValues.put("proid",(newsBean.attach==null)?"":newsBean.attach.proid);
//            resolver.insert(insertUri, contentValues);
//            saveNotReadMessage(newsBean);
//            sendNotification(newsBean);
//        }
//    }
//    private ParseJsonListener parseJson;
//    public void setParseJsonListener(ParseJsonListener parseJson){
//        this.parseJson = parseJson;
//    }
//    public class MqttBinder extends Binder {
//        public MqttService getMqttService(){
//            return MqttService.this;
//        }
//    }
//    private IConntectSuccess conntectSuccess;
//    public void setConntectSuccess(IConntectSuccess conntectSuccess){
//        this.conntectSuccess = conntectSuccess;
//    }
//    private IParsePayListener parsePayListener;
//    public void setParsePay(IParsePayListener parsePay){
//        this.parsePayListener = parsePay;
//    }
//    private INoticeListener noticeListener;
//    public void setNoticeListener(INoticeListener noticeListener){
//        this.noticeListener = noticeListener;
//    }
//    public interface ParseJsonListener{
//        void messageArrivedBlackList(String message);
//    }
//    public interface IConntectSuccess{
//        void mqttClientConnectSuccess();
//    }
//    public interface IParsePayListener{
//        void messageArrivedPay(String message);
//    }
//    public interface INoticeListener{
//        void messageArrivedNotice(String message);
//    }
}


