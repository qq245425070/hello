package cn.gietv.mlive.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.modules.xmpp.UserChatManager;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.Util;
import cn.gietv.mlive.utils.VersionUtils;

/**
 * Created by houde on 2016/3/15.
 */
public class XmppService extends Service {
    private XMPPConnection mConnection;
    private XmppConnection mXmppConnection;
    private UserChatManager userChatManager;
    private SystemPacketListener systemPacketListener;
    private MultiUserChat multiUserChat;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Xmpp服务Create");
        mXmppConnection =  XmppConnection.getInstance();
        if(mConnection == null){
            new Thread(){
                @Override
                public void run() {
                    System.out.println("创建XMPPConnection");
                    try {
                        mConnection = mXmppConnection.getConnection();
                        String id;
                        if (UserUtils.isNotLogin()) {
                            id = VersionUtils.getDeviceId(MainApplication.getInstance());
                        } else {
                            id = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
                        }
                        boolean result = mXmppConnection.login(id, Util.MD5((id + ConfigUtils.KEY).getBytes()));
                        System.out.println(result);
                        userChatManager = UserChatManager.getInstance(XmppService.this);
                        mConnection.getChatManager().addChatListener(userChatManager);
                        systemPacketListener = new SystemPacketListener();
//                        multiUserChat = mXmppConnection.joinMultiUserChat(id, ConfigUtils.TOPIC_SYSTEM_MESSAGE, "");
                        multiUserChat = mXmppConnection.joinMultiUserChat(id, ConfigUtils.TOPIC_SYSTEM_MESSAGE_TEST, "");
                        if (multiUserChat != null) {
                            Log.e("ceshi","订阅房间成功");
                            multiUserChat.addMessageListener(systemPacketListener);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Xmpp服务destroy");
//        if(multiUserChat != null) {
//            multiUserChat.removeMessageListener(systemPacketListener);
//        }
        if(mConnection != null && mConnection.getChatManager() != null)
             mConnection.getChatManager().removeChatListener(userChatManager);
    }
    class SystemPacketListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {
            Message message = (Message) packet;
            if (message != null && message.getBody() != null) {
                if(userChatManager != null)
                    userChatManager.parseJsonAndSaveMsg(message.getBody());
            }
        }
    }
}
