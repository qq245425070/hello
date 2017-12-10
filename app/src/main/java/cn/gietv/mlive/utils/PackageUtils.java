package cn.gietv.mlive.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author：steven
 * datetime：15/10/9 14:22
 *
 */
public class PackageUtils {
    public static Set<String> getInstalledPackage(Context context) {
        Set<String> packageSet = new HashSet<>();
        List<PackageInfo> listPackage = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < listPackage.size(); i++) {
            packageSet.add(listPackage.get(i).packageName);
        }
        return packageSet;
    }

    public static void openApplication(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(packageInfo.packageName);
            PackageManager pManager = context.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public static boolean hasInstalled(Context context, String packageName) {
        return getInstalledPackage(context).contains(packageName);
    }
    /**
     * 判断指定名称的服务是否运行
     * @param
     * @param name
     * @return
     */
    public static boolean isServiceRunning(Context activity, String name) {
//		System.out.println("name:"+name);
        // ActivityManager 不但管理activity，而且管理，手机所有的运行时状态，类似于windows中的任务管理器
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceInfoList = am.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceInfoList) {

            String className = serviceInfo.service.getClassName();
//			System.out.println("runnService:"+className);
            if(name.equals(className)){
                //说明指定名称的服务，正在运行
                return true;
            }
        }
        return false;
    }
    /*****
     * 判断当前应用是否运行在前台
     *
     * */
    public static boolean isAppOnForeground(Context context) {
        List<ActivityManager.RunningTaskInfo> taskInfos = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        if (taskInfos.size() > 0
                && TextUtils.equals(context.getPackageName(),
                taskInfos.get(0).topActivity.getPackageName())) {
            return true;
        }

        return false;
    }
}
