package cn.gietv.mlive;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.youku.player.YoukuPlayerBaseConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.home.activity.HomeActivity;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HttpUtils;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/9/16 20:21
 */
public class MainApplication extends Application {
    public static YoukuPlayerBaseConfiguration configuration;
    private static MainApplication instance;
    private List<AbsBaseActivity> activityList;
    private Map<String ,Long> downloadHashMap;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String url = "";
//        refWatcher = LeakCanary.install(this);
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            url = info.metaData.getString("TEAROOM_MLIVE_SERVER_URL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(url == null){
            url = "http://api.mlive.gietv.cn:8080/liveservice";
        }
        //设置Umeng统计的方式
        MobclickAgent.openActivityDurationTrack(false);

		//注册友盟第三方登录和分享
        PlatformConfig.setWeixin(ConfigUtils.WEIXIN_APPID, ConfigUtils.WEIXIN_APPKEY);
        PlatformConfig.setSinaWeibo(ConfigUtils.SINA_APPID, ConfigUtils.SINA_APPKEY);
        PlatformConfig.setQQZone(ConfigUtils.QQ_APPID, ConfigUtils.QQ_APPKEY);
		
        RetrofitUtils.initRetrofit(url);
        CacheUtils.initCache();
        HttpUtils.initHttpHead();
        SharedPreferenceUtils.init(this);
        ImageLoaderUtils.init(this);
        downloadHashMap = new HashMap<>();
        //youKuSDK的配置
        configuration = new YoukuPlayerBaseConfiguration(this) {


            /**
             * 通过覆写该方法，返回“正在缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的缓存界面
             */
            @Override
            public Class<? extends Activity> getCachingActivityClass() {
                // TODO Auto-generated method stub
                return null;
            }

            /**
             * 通过覆写该方法，返回“已经缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的已缓存界面
             */
            @Override
            public Class<? extends Activity> getCachedActivityClass() {
                // TODO Auto-generated method stub
                return null;
            }

            /**
             * 配置视频的缓存路径，格式举例： /appname/videocache/
             * 如果返回空，则视频默认缓存路径为： /应用程序包名/videocache/
             *
             */
            @Override
            public String configDownloadPath() {
                // TODO Auto-generated method stub
                //return "/myapp/videocache/";			//举例
                return null;
            }
        };
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public void addActivityList(AbsBaseActivity activity){
        if(activityList == null){
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
    }
    public void destroyActivity(){
        if(activityList != null){
            for (int i=0;i<activityList.size();i++){
                if(activityList.get(i).isNotFinish())
                    activityList.get(i).finish();
            }
            activityList.clear();
            activityList = null;
        }
    }
    public void saveDownloadStatus(String fileName,long id){
        downloadHashMap.put(fileName,id);
    }

    private HomeActivity mHomeActivity;
    public void saveHomeActivity(HomeActivity homeActivity){
        this.mHomeActivity = homeActivity;
    }
    public HomeActivity getHomeActivity(){return  mHomeActivity;
    }

//    private RefWatcher refWatcher;
//    public static RefWatcher getRefWatcher(FragmentActivity activity) {
//        MainApplication application = (MainApplication) activity.getApplicationContext();
//        return application.refWatcher;
//    }
}
