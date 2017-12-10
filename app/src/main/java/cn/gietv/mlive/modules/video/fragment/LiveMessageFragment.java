package cn.gietv.mlive.modules.video.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;
import cn.gietv.mlive.modules.video.adapter.MessageAdapter;
import cn.gietv.mlive.modules.video.bean.BlackListBean;
import cn.gietv.mlive.modules.video.bean.MessageBean;
import cn.gietv.mlive.modules.video.bean.MqttMessageBean;
import cn.gietv.mlive.modules.video.bean.MqttMessageBeanOld;
import cn.gietv.mlive.modules.video.bean.NoticeBean;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.VersionUtils;
import cn.kalading.android.retrofit.utils.DefaultHttpCallback;
import cn.kalading.android.retrofit.utils.GsonUtils;
import cn.kalading.android.retrofit.utils.HttpResponse;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * author：steven
 * datetime：15/10/11 09:52
 */
public class LiveMessageFragment extends AbsBaseFragment{
    public static final String EXTRA_TOPIC = "extra_topic";
    public static final String EXTRA_HOST = "extra_host";
    public static final String EXTRA_ROOM_ID = "extra_room_id";
    public static final String EXTRA_TITLE = "extra_title";
    private static final int MAX_MESSAGE_COUNT = 30;
    private View mCurrentView;
    private ListView mListView;
    private String mTitle;
    private List<MessageBean.MessagesEntity> mMessageList = new ArrayList<>();
    private MessageAdapter mAdapter;
    private ProgressDialog mDialog;

    private String mTopic;
    private String mHost;
    private UserCenterBean.UserinfoEntity mUserinfoEntity;
    private MessageListener mMessageListener;
    private View mHeaderView;
    private VideoModel mVideoModel;
    public String mRoomID;
    public static LiveMessageFragment getInstence(String host,String roomId,String title) {
        LiveMessageFragment fragment = new LiveMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HOST, host);
        bundle.putString(EXTRA_ROOM_ID, roomId);
        bundle.putString(EXTRA_TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }
    public List<MessageBean.MessagesEntity> getData(){
      return mMessageList;
    }
    public void setData(List<MessageBean.MessagesEntity> data){
        this.mMessageList = data;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.live_message_layout, container, false);
        mListView = (ListView) mCurrentView.findViewById(R.id.live_message_lv_list);
        mVideoModel = RetrofitUtils.create(VideoModel.class);
        if (mHeaderView != null) {
            mListView.addHeaderView(mHeaderView);
        }
        mHost = getArguments().getString(EXTRA_HOST);
        mTopic = getArguments().getString(EXTRA_TOPIC);
        mRoomID = getArguments().getString(EXTRA_ROOM_ID);
        mTitle = getArguments().getString(EXTRA_TITLE);
        if(mTitle == null || "".equals(mTitle)){
            mTitle = "粉丝";
        }
        mAdapter = new MessageAdapter(getActivity(), mMessageList,true , mTitle);
        mListView.setAdapter(mAdapter);
        return mCurrentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendOutRoom();
                try {
                    mChatRoom.removeMessageListener(mChatListener);
                    mSendCoinRoom.removeMessageListener(mSendGifListener);
                    mBlackListRoom.removeMessageListener(mBlackListListener);
                    mNoticeRoom.removeMessageListener(mNoticeListener);
                    mChatRoom.leave();
                    mSendCoinRoom.leave();
                    mEnterRoom.leave();
                    mLeftRoom.leave();
                    mBlackListRoom.leave();
                    mNoticeRoom.leave();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void sendOutRoom() {
        if(UserUtils.isNotLogin()){
            return;
        }
        System.out.println("退出房间，发送数据");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        MqttMessageBean bean = new MqttMessageBean();
        MqttMessageBean.ChatRoom data = new MqttMessageBean.ChatRoom();
        bean.type = ConfigUtils.CHAT_ROOM_IN_OUT;
        bean.clientid = VersionUtils.getDeviceId(MainApplication.getInstance());
        bean.time = date;
        bean.topic = ConfigUtils.TOPIC_LEFT_ROOM + mRoomID;
        data.uid = mUserinfoEntity._id;
        data.nickname = mUserinfoEntity.nickname;
        data.avatar = mUserinfoEntity.avatar;
        data.news = mUserinfoEntity.nickname + "退出直播间";
        data.date = date;
        bean.data = data;
        data.behavior = "n";
        Gson gson = GsonUtils.getGson();
        try {
//               mController.sendMessage(ConfigUtils.TOPIC_LEFT_ROOM + mRoomID ,gson.toJson(bean));
            System.out.println(gson.toJson(bean));
           mLeftRoom.sendMessage(gson.toJson(bean));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String proid = ((LivePlayListActivity)getActivity()).mProgram._id;
        mVideoModel.chatLogin("", 2, new DefaultHttpCallback<String>() {
            @Override
            public void success(HttpResponse<String> stringHttpResponse, Response response) {
                }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
    public void destory(){

    }
//    private MqttService mMqttService;
    //private MultiUserChat multiUserChat;
    private MultiUserChat mChatRoom;
    private MultiUserChat mSendCoinRoom;
    private MultiUserChat mEnterRoom;
    private MultiUserChat mLeftRoom;
    private MultiUserChat mNoticeRoom;
    private MultiUserChat mBlackListRoom;
    @Override
    public void onResume() {
        super.onResume();
       if( XmppConnection.getInstance().isConnection()){
           joinMultiUserChat();
       }else{
           new Thread(){
               @Override
               public void run() {
                   joinMultiUserChat();
               }
           }.start();
       }
    }
    private void joinMultiUserChat(){
        mUserinfoEntity = CacheUtils.getCacheUserInfo();
        String id ;
        if(mUserinfoEntity == null){
            id = VersionUtils.getDeviceId(getActivity());
        }else{
            id = mUserinfoEntity._id;
        }
        try {
            mChatRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_CHAT + mRoomID,"");
            mChatListener = new ChatPacketListener();
            mChatRoom.addMessageListener(mChatListener);

            mSendCoinRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_SEND_CION + mRoomID,"");
            mSendGifListener = new SendCoinPacketListener();
            mSendCoinRoom.addMessageListener(mSendGifListener);

            mEnterRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_ENTER_ROOM + mRoomID,"");
            mLeftRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_LEFT_ROOM  + mRoomID,"");

            mNoticeRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_NOTICE + mRoomID,"");
            mNoticeListener = new NoticePackListener();
            mNoticeRoom.addMessageListener(mNoticeListener);

            mBlackListRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_BLACK_LIST + mRoomID,"");
            mBlackListListener = new BlackListPackListener();
            mBlackListRoom.addMessageListener(mBlackListListener);

            //订阅成功，发送进入房间消息
            sendInRoomMessage();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendInRoomMessage() {
        //连接服务器成功通知后台
            if (!UserUtils.isNotLogin()) {
//                String proid = ((LivePlayListActivity) getActivity()).mProgram._id;
                if ("" != null) {
                    mVideoModel.chatLogin("", 1, new DefaultHttpCallback<String>() {
                        @Override
                        public void success(HttpResponse<String> stringHttpResponse, Response response) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = format.format(new Date());
                            MqttMessageBean bean = new MqttMessageBean();
                            MqttMessageBean.ChatRoom data = new MqttMessageBean.ChatRoom();
                            bean.type = ConfigUtils.CHAT_ROOM_IN_OUT;
                            bean.clientid = VersionUtils.getDeviceId(MainApplication.getInstance());
                            bean.time = date;
                            data.date = date;
                            bean.topic = ConfigUtils.TOPIC_ENTER_ROOM + mRoomID;
                            data.uid = mUserinfoEntity._id;
                            data.nickname = mUserinfoEntity.nickname;
                            data.avatar = mUserinfoEntity.avatar;
                            data.news = mUserinfoEntity.nickname + "进入直播间";
                            bean.data = data;
                            data.behavior = "y";
                            Gson gson = GsonUtils.getGson();
                            try {
                                //mController.sendMessage(ConfigUtils.TOPIC_ENTER_ROOM + mRoomID, gson.toJson(bean));
                                mEnterRoom.sendMessage(gson.toJson(bean));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
    }

    private void addMessage(MessageBean.MessagesEntity message) {
        mMessageList.add(message);
        if (mMessageList.size() > MAX_MESSAGE_COUNT) {
            mMessageList.remove(0);
        }
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mAdapter.getCount() - 1);
    }
    private void addMessages(List<MessageBean.MessagesEntity> messages){
        mMessageList.addAll(messages);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mAdapter.getCount() - 1);
    }


    public void sendMessage(String messageText) {
       /* if (!mController.isConnect()) {
            ToastUtils.showToast(getActivity(), "服务器未连接");
            return;
        }*/
        MqttMessageBeanOld bean = new MqttMessageBeanOld();
        bean.avatar = mUserinfoEntity.avatar;
        bean.news = messageText;
        bean.uid = mUserinfoEntity._id;
        bean.nickname = mUserinfoEntity.nickname;
        Gson gson = GsonUtils.getGson();
        try {
            //mController.sendMessage(ConfigUtils.TOPIC_CHAT + mRoomID, gson.toJson(bean));
            mChatRoom.sendMessage(gson.toJson(bean));
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), e.getMessage());
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }
    public void sendMessage(String type , String messageText, String behavior,String propimg) {//送礼物，发言
        System.out.println(messageText);
        /*if (!mController.isConnect()) {
            ToastUtils.showToast(getActivity(), "服务器未连接");
            return;
        }*/
        MqttMessageBean bean = new MqttMessageBean();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        bean.time = date;
        bean.topic = ConfigUtils.TOPIC_SEND_CION + mRoomID;
        bean.clientid = VersionUtils.getDeviceId(MainApplication.getInstance());
        MqttMessageBean.ChatRoom data = new MqttMessageBean.ChatRoom();
        data.avatar = mUserinfoEntity.avatar;
        data.news = messageText;
        data.nickname = mUserinfoEntity.nickname;
        data.uid = mUserinfoEntity._id;
        data.date = date;
        bean.type = type;
        if(CacheUtils.getMyContributionJinjiao(mRoomID) != null) {
            data.jinjiaoImg = CacheUtils.getMyContributionJinjiao(mRoomID).levelimg;
            data.jinjiaoLevel = CacheUtils.getMyContributionJinjiao(mRoomID).level;
        }
        if(CacheUtils.getMyContributionCoin(mRoomID) != null) {
            data.acoinImg = CacheUtils.getMyContributionCoin(mRoomID).levelimg;
            data.acoinLevel = CacheUtils.getMyContributionCoin(mRoomID).level;
        }
        if(ConfigUtils.CHAT_ROOM_CONCERN.equals(type)){
            data.behavior = behavior;
        }else if (ConfigUtils.CHAT_ROOM_IN_OUT.equals(type)){
            data.behavior = behavior;
        }else if(ConfigUtils.CHAT_ROOM_SEND_GIFT.equals(type)){
            data.prop = behavior;
            data.propimg = propimg;
        }else if(ConfigUtils.CHAT_ROOM_LIVE.equals(type)){
            data.behavior = behavior;
        }
        bean.data = data;
        Gson gson = GsonUtils.getGson();
        try {
            if(ConfigUtils.USER_CHAT.equals(type)){
               // mController.sendMessage(ConfigUtils.TOPIC_CHAT + mRoomID, gson.toJson(bean));
                mChatRoom.sendMessage(gson.toJson(bean));
            }else {
                //mController.sendMessage(ConfigUtils.TOPIC_SEND_CION + mRoomID, gson.toJson(bean));
                mSendCoinRoom.sendMessage(gson.toJson(bean));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), e.getMessage());
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

  /*  public boolean isConnected() {
        if (mController == null) {
            return false;
        }
        return mController.isConnect();
    }*/
    private android.os.Handler mHandler = new android.os.Handler(){
      @Override
      public void handleMessage(android.os.Message msg) {
          MessageBean.MessagesEntity messagesEntity = (MessageBean.MessagesEntity) msg.obj;
          addMessage(messagesEntity);
      }
  };
    private android.os.Message mMessage;
    public void messageArrived(String message) {
        MqttMessageBean bean = GsonUtils.getGson().fromJson(message, MqttMessageBean.class);
        MessageBean.MessagesEntity msgBean = new MessageBean.MessagesEntity();
        try {
            msgBean.uid = bean.data.uid;
            msgBean.message = bean.data.news;
            msgBean.avatar = bean.data.avatar;
            msgBean.nickname = bean.data.nickname;
            msgBean.date = bean.time;
            msgBean.topic = bean.topic;
            msgBean.clientid = bean.clientid;
            msgBean.type = bean.type;
            msgBean.prop = bean.data.prop;
            msgBean.behavior = bean.data.behavior;
            msgBean.jinjiaoImg = bean.data.jinjiaoImg;
            msgBean.jinjiaoLevel = bean.data.jinjiaoLevel;
            msgBean.acoinImg = bean.data.acoinImg;
            msgBean.acoinLevel = bean.data.acoinLevel;
            mMessage = mHandler.obtainMessage();
            mMessage.obj = msgBean;
            mHandler.sendMessage(mMessage);
        }catch (Exception e){
            e.printStackTrace();
            MqttMessageBeanOld bean1 = GsonUtils.getGson().fromJson(message, MqttMessageBeanOld.class);
            msgBean.uid = bean1.uid;
            msgBean.avatar = bean1.avatar;
            msgBean.message = bean1.news;
            msgBean.nickname = bean1.nickname;
            mMessage = mHandler.obtainMessage();
            mMessage.obj = msgBean;
            mHandler.sendMessage(mMessage);
        }

    }

    public void messageArrivedNew(String message) {
        MqttMessageBean bean = GsonUtils.getGson().fromJson(message, MqttMessageBean.class);
        MessageBean.MessagesEntity msgBean = new MessageBean.MessagesEntity();
        msgBean.uid = bean.data.uid;
        msgBean.message = bean.data.news;
        msgBean.avatar = bean.data.avatar;
        msgBean.nickname = bean.data.nickname;
        msgBean.date = bean.time;
        msgBean.topic = bean.topic;
        msgBean.clientid = bean.clientid;
        msgBean.type = bean.type;
        msgBean.prop = bean.data.prop;
        msgBean.behavior = bean.data.behavior;
        msgBean.jinjiaoImg = bean.data.jinjiaoImg;
        System.out.println(msgBean.jinjiaoImg + "        " +  bean.data.jinjiaoImg);
        msgBean.jinjiaoLevel = bean.data.jinjiaoLevel;
        msgBean.acoinImg = bean.data.acoinImg;
        msgBean.acoinLevel = bean.data.acoinLevel;
        msgBean.propimg = bean.data.propimg;
        if(ConfigUtils.CHAT_ROOM_SEND_GIFT.equals(bean.type)) {
            msgBean.message = bean.data.nickname + bean.data.news + bean.data.prop;
            mMessage = mHandler.obtainMessage();
            mMessage.obj = msgBean;
            mHandler.sendMessage(mMessage);
        }
    }

    public void messageArrivedChatHistory(String message) {
        List<MessageBean.MessagesEntity> messagesEntities = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(message);
            String jsonObject = null;
            Gson gson= GsonUtils.getGson();
            MessageBean.MessagesEntity msgBean = null;
            MqttMessageBean bean =null;
            for(int i = 0; i < jsonArray.length(); i++ ){
                jsonObject = jsonArray.getString(1);
                msgBean = new MessageBean.MessagesEntity();
                bean = gson.fromJson(jsonObject, MqttMessageBean.class);
                msgBean.uid = bean.data.uid;
                msgBean.message = bean.data.news;
                msgBean.avatar = bean.data.avatar;
                msgBean.nickname = bean.data.nickname;
                msgBean.date = bean.time;
                msgBean.topic = bean.topic;;
                msgBean.clientid = bean.clientid;
                msgBean.type = bean.type;
                //msgBean.jinjiaoImg = jsonObject.getJSONObject("data").getString("jinjiaoImg");
                //msgBean.jinjiaoLevel = jsonObject.getJSONObject("data").getInt("jinjiaoLevel");
                //msgBean.acoinImg = jsonObject.getJSONObject("data").getString("acoinImg");
                //msgBean.acoinLevel = jsonObject.getJSONObject("data").getInt("acoinLevel");
                if(ConfigUtils.CHAT_ROOM_SEND_GIFT.equals(bean.type)) {
                    msgBean.message = bean.data.nickname + bean.data.news + bean.data.prop;
                }
                messagesEntities.add(msgBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(messagesEntities.size() > 0)
            addMessages(messagesEntities);
    }
    public void messageArrivedBlackList(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            int status = jsonObject.getInt("status");
            if(status == 0) {
                BlackListBean blackListBean = GsonUtils.getGson().fromJson(jsonObject.get("data").toString(), BlackListBean.class);
                CacheUtils.saveBlackList(mRoomID,blackListBean);
                System.out.println(blackListBean.blacklist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void messageArrivedNotice(String message) {
        Gson gson = GsonUtils.getGson();
        NoticeBean bean = gson.fromJson(message, NoticeBean.class);
        NoticeBean oldBean = CacheUtils.getNotice(mRoomID);
        if(oldBean != null){
            if(bean != null) {
                if(!oldBean.notice.equals(bean.notice)){
//                    ((LivePlayListActivity)getActivity()).mPrompt.setBackgroundDrawable(((LivePlayListActivity)getActivity()).framAnimation);
//                    ((LivePlayListActivity) getActivity()).framAnimation.start();
//                    ((LivePlayListActivity) getActivity()).prompt_text.setText(bean.notice);
                }

            }
        }else{
            if(bean != null){
//                ((LivePlayListActivity)getActivity()).mPrompt.setBackgroundDrawable(((LivePlayListActivity)getActivity()).framAnimation);
//                ((LivePlayListActivity) getActivity()).framAnimation.start();
//                ((LivePlayListActivity)getActivity()).prompt_text.setText(bean.notice);
            }
        }
    }

    public interface MessageListener {
        void sendMessageSuccess();
    }

    public void setMessageListener(MessageListener messageListener) {
        mMessageListener = messageListener;
    }
    private ChatPacketListener mChatListener;
    private SendCoinPacketListener mSendGifListener;
    private BlackListPackListener mBlackListListener;
    private NoticePackListener mNoticeListener;
    class ChatPacketListener implements PacketListener{
        @Override
        public void processPacket(Packet packet) {
            String message = ((Message)packet).getBody();
            System.out.println("ChatPacketListener    "+message);
            messageArrived(message);
        }
    }
    class SendCoinPacketListener implements PacketListener{
        @Override
        public void processPacket(Packet packet) {
            String message = ((Message)packet).getBody();
            System.out.println("SendCoinPacketListener    "+message);
            messageArrivedNew(message);
        }
    }
    class BlackListPackListener implements  PacketListener{
        @Override
        public void processPacket(Packet packet) {
            String message = ((Message)packet).getBody();
            System.out.println("BlackListPackListener    "+message);
            messageArrivedBlackList(message);
        }
    }
    class NoticePackListener implements  PacketListener{
        @Override
        public void processPacket(Packet packet) {
            String message = ((Message)packet).getBody();
            System.out.println("NoticePackListener    "+message);
            messageArrivedNotice(message);
        }
    }
/*    public class MyMqttHandler extends MqttHandler {
        @Override
        public void connectionLost(Throwable t) {

        }

        @Override
        public void connectSuccess() {
            try {
                if(mController != null) {
                    mController.getMqttClient().subscribe(ConfigUtils.TOPIC_BLACK_LIST + mRoomID, 0);//黑名单
                    mController.getMqttClient().subscribe(ConfigUtils.TOPIC_NOTICE + mRoomID, 0);//公告跟新
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }

        @Override
        public void messageArrivedPay(String message) throws Exception {

        }

        @Override
        public void messageArrivedBlackList(String message) throws Exception {
            try {
                JSONObject jsonObject = new JSONObject(message);
                int status = jsonObject.getInt("status");
                if(status == 0) {
                    BlackListBean blackListBean = GsonUtils.getGson().fromJson(jsonObject.get("data").toString(), BlackListBean.class);
                    CacheUtils.saveBlackList(mRoomID,blackListBean);
                    System.out.println(blackListBean.blacklist);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void messageArrivedNotice(String message) throws Exception {
            Gson gson = GsonUtils.getGson();
            NoticeBean bean = gson.fromJson(message, NoticeBean.class);
            NoticeBean oldBean = CacheUtils.getNotice(mRoomID);
            if(oldBean != null){
                if(bean != null) {
                    if(!oldBean.notice.equals(bean.notice)){
                        ((LivePlayListActivity)getActivity()).mPrompt.setBackgroundDrawable(((LivePlayListActivity)getActivity()).framAnimation);
                        ((LivePlayListActivity) getActivity()).framAnimation.start();
                        ((LivePlayListActivity) getActivity()).prompt_text.setText(bean.notice);
                    }

                }
            }else{
                if(bean != null){
                    ((LivePlayListActivity)getActivity()).mPrompt.setBackgroundDrawable(((LivePlayListActivity)getActivity()).framAnimation);
                    ((LivePlayListActivity)getActivity()).prompt_text.setText(bean.notice);
                }
            }
        }

        @Override
        public void messageArrivedSystem(String message) throws Exception {

        }
    }*/
}
