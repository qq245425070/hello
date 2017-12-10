package cn.gietv.mlive.modules.xmpp;

import android.content.Context;
import android.content.Intent;

import cn.gietv.mlive.service.XmppService;
import cn.gietv.mlive.utils.PackageUtils;

/**
 * Created by houde on 2016/3/17.
 */
public class XmppUtils {
    private static XmppUtils xmppUtils = new XmppUtils();
    private XmppUtils(){}
    public static XmppUtils getInstance(){
        return xmppUtils;
    }

    public void reconnection(Context context){
        XmppConnection connection = XmppConnection.getInstance();
        connection.closeConnection();
        Intent service = new Intent();
        service.setClass(context, XmppService.class);
        if(PackageUtils.isServiceRunning(context, XmppService.class.getName())){
            context.stopService(service);
        }
        context.startService(service);
    }
}
