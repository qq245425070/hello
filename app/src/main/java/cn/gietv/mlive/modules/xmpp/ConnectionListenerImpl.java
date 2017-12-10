package cn.gietv.mlive.modules.xmpp;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;

import java.util.Timer;
import java.util.TimerTask;

import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.Util;

/**
 * Created by houde on 2016/3/15.
 */
public class ConnectionListenerImpl implements ConnectionListener {
    private Timer tExit;
    private String username;
    private String password;
    private int logintime = 3000;

    @Override
    public void connectionClosed() {
        Log.i("TaxiConnectionListener", "连接关闭");
        // 關閉連接
        XmppConnection.getInstance().closeConnection();
        // 重连服务器
        tExit = new Timer();
        tExit.schedule(new timetask(), logintime);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i("TaxiConnectionListener", "连接关闭异常");
        // 判斷為帳號已被登錄
        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            // 關閉連接
            XmppConnection.getInstance().closeConnection();
            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new timetask(), logintime);
        }
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
            username = CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID);
            password = Util.MD5((username + ConfigUtils.KEY).getBytes());
            if (username != null && password != null) {
                Log.i("TaxiConnectionListener", "尝试登陆");
                // 连接服务器
                try {
                    if (XmppConnection.getInstance().login(username, password)) {
                        Log.i("TaxiConnectionListener", "登录成功");
                    } else {
                        Log.i("TaxiConnectionListener", "重新登录");
                        tExit.schedule(new timetask(), logintime);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void reconnectingIn(int arg0) {
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        tExit.cancel();
    }

    @Override
    public void reconnectionSuccessful() {
    }

}
