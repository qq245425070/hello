package cn.gietv.mlive.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.constants.HttpConstants;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/9/16 20:56
 */
public class HttpUtils {
    public static void initHttpHead() {
        RetrofitUtils.addHeader(HttpConstants.HEAD_DEVICE_ID, VersionUtils.getDeviceId(MainApplication.getInstance()));
        RetrofitUtils.addHeader(HttpConstants.HEAD_DEVICE_SYSTEM, VersionUtils.getDeviceSystemName(MainApplication.getInstance()));
        RetrofitUtils.addHeader(HttpConstants.HEAD_DEVICE_TYPE, VersionUtils.getDeviceTpye(MainApplication.getInstance()));
        RetrofitUtils.addHeader(HttpConstants.HEAD_VERSION, CommConstants.API_VERSION);
        int channelid = 0;
        try {
            ApplicationInfo info = MainApplication.getInstance().getPackageManager().getApplicationInfo(MainApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            channelid = info.metaData.getInt("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(channelid == 0){
            channelid = 10100;
        }
        RetrofitUtils.addHeader(HttpConstants.HEAD_CHINNEL_ID, channelid + "");
        RetrofitUtils.addHeader(HttpConstants.HEAD_USER_ID, CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID));
        RetrofitUtils.addHeader(HttpConstants.HEAD_TOKEN, CacheUtils.getCache().getAsString(CacheConstants.CACHE_TOKEN));
        RetrofitUtils.addHeader(HttpConstants.HEAD_TYPE, "android");
        RetrofitUtils.addHeader(HttpConstants.HEAD_VERSION_CODE, String.valueOf(VersionUtils.getSoftVersionCode(MainApplication.getInstance())));
    }
    public static void setHttpHead(String headVersion){
        RetrofitUtils.addHeader(HttpConstants.HEAD_VERSION, headVersion);
    }
    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!StringUtils.isEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
}
