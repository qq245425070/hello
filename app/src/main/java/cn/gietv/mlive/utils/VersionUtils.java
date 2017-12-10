package cn.gietv.mlive.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * author：steven
 * datetime：15/9/16 21:01
 *
 */
public class VersionUtils {
    public static String getSoftVersionName(Context context) {
        String version = null;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }

    public static int getSoftVersionCode(Context context) {
        int version = 0;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = manager.getSubscriberId();
        if(deviceId == null){
            deviceId = manager.getDeviceId();
        }
        if(deviceId == null){
            deviceId = "123456789";
        }
        return deviceId;
    }

    public static String getDeviceSystemName(Context context) {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceTpye(Context context) {
        return Build.MODEL;
    }
}
