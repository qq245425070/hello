package cn.gietv.mlive.modules.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.List;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.follow.fragment.FollowContentFragment;
import cn.gietv.mlive.modules.game.fragment.GameListFragment3;
import cn.gietv.mlive.modules.news.fragment.NewsListFragment;
import cn.gietv.mlive.modules.recommend.fragment.RecommendHomeFragment;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.usercenter.fragment.UserCenterFragment;
import cn.gietv.mlive.modules.video.bean.PropBeanList;
import cn.gietv.mlive.modules.video.model.GiftModel;
import cn.gietv.mlive.modules.xmpp.UserChatManager;
import cn.gietv.mlive.service.XmppService;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DownLoadImageUtil;
import cn.gietv.mlive.utils.PackageUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UpdataController;
import cn.gietv.mlive.views.MyFragmentTabHost;
import cn.kalading.android.retrofit.utils.RetrofitUtils;

/**
 * author：steven
 * datetime：15/10/8 10:46
 */
public class HomeActivity extends AbsBaseActivity {
    public static final String SHARE_FIRST_INTO = "first_install";
    public static final String THEME_CHANGE_ACTION = "theme.change.action";
    public MyFragmentTabHost mTabHost;
    private LayoutInflater mInflater;//,,NewsListFragment.class
    private Class mFragments[] = {RecommendHomeFragment.class,FollowContentFragment.class,  GameListFragment3.class,NewsListFragment.class,UserCenterFragment.class};
    private String mBottomText[] = {"发现","订阅", "游戏", "消息","我的"};//R.drawable.message_icon,
    private int mBottomDrawable[] = {R.drawable.tab_game,R.drawable.tab_home,  R.drawable.tab_live,R.drawable.tab_news, R.drawable.tab_user};

    private boolean mHasThemeChanged = false;
    private ThemeChangeReceiver mThemeChangeReceiver;

    private UpdataController mUpdataController;

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tab_fragment);
        for (int i = 0; i < mFragments.length; i++) {
            TabHost.TabSpec spec = mTabHost.newTabSpec(mBottomText[i]).setIndicator(getTabItemView(i));
            Bundle bundle = new Bundle();
            bundle.putString(UserCenterFragment.EXTRA_USER_ID, CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID));
            mTabHost.addTab(spec, mFragments[i], bundle);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.tab_bg);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                FragmentManager fm = getSupportFragmentManager();
                AbsBaseFragment framgent = (AbsBaseFragment) fm.findFragmentByTag(s);
                if (framgent != null) {
                    framgent.onBack();
                }
            }
        });
    }

    /**
     * 连续按两次返回键就退出
     */
    private long firstTime;
    @Override
    public void onBackPressed() {
       if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            ToastUtils.showToastShort(this, "再按一次退出");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasThemeChanged) {
            finish();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("position", 3);
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        MainApplication.getInstance().saveHomeActivity(null);
        super.onDestroy();
        unregisterReceiver(mThemeChangeReceiver);
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
//        if(index != 2) {
            View view = mInflater.inflate(R.layout.main_bottom_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.home_tv_bottom);
            imageView.setImageResource(mBottomDrawable[index]);
            TextView textView = (TextView) view.findViewById(R.id.home_title);
            textView.setText(mBottomText[index]);
            return view;
//        }else {
//            View view = mInflater.inflate(cn.gietv.mvr.R.layout.main_bottom_vr, null);
//            ImageView imageView = (ImageView) view.findViewById(cn.gietv.mvr.R.id.home_tv_bottom);
//            imageView.setImageResource(mBottomDrawable[index]);
//            return view;
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        NotificationTitleBarUtils.setGreenNotification(this);
        setContentView(R.layout.main_layout);
        MainApplication.getInstance().saveHomeActivity(this);
//        boolean firstInstall = SharedPreferenceUtils.getBoolean(SHARE_FIRST_INTO, true);
//        if (firstInstall) {
//        IntentUtils.openActivity(this, WelcomeActivity.class);
//        }
        //为U盟添加渠道号  改为在manifast文件中设置友盟渠道号
//        setUmengChanelID();
        initView();
        //发送设备信息
        sendDevice();
        startService();
        //查询礼物列表
       // queryProps();
        //保存默认消息
        saveNews();

        mUpdataController =new UpdataController(this);
        mUpdataController.indexCheckUpdate();
        mThemeChangeReceiver = new ThemeChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(THEME_CHANGE_ACTION);
        registerReceiver(mThemeChangeReceiver, intentFilter);
        int position = getIntent().getIntExtra("position", 0);
        mTabHost.getTabWidget().getChildTabViewAt(position).performClick();
       // mTabHost.getTabWidget().setCurrentTab(position);
        boolean system_message_swith = SharedPreferenceUtils.getBoolean("system_message_swith",true);
        //    开启友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(MainApplication.getInstance());
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("ceshi","deviceToken = "  + deviceToken);
                //向服务器发送umeng token
                statisticsMode.sendUmengDevice(deviceToken, new DefaultLiveHttpCallBack<String>() {
                    @Override
                    public void success(String s) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("ceshi","s = " + s + "       s1 =   " + s1);
            }
        });
        //开启推送并设置注册的回调处理
        if(system_message_swith) {
            //开启推送并设置注册的回调处理
            mPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
        else{
            mPushAgent.disable(new IUmengCallback(){
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }

    private void saveNews() {
        if(DBUtils.getInstance(MainApplication.getInstance()).getSystemRoster(ConfigUtils.SYSTEM_MESSAGE_USERID).size() == 0) {
            UserChatManager chat = UserChatManager.getInstance(MainApplication.getInstance());
            String message = "{\"avatar\": \"http://download.mlive.gietv.cn/liveservice/256-256.png\",\"from_me\": 0,\"message\": \"欢迎来到游戏茶餐厅\",\"nickname\": \"系统通知\",\"read\": 0,\"time\": "+System.currentTimeMillis()+",\"toOrFrom\": " + ConfigUtils.SYSTEM_MESSAGE_USERID + ",\"userId\": "+ConfigUtils.SYSTEM_MESSAGE_USERID+"}";
            chat.parseJsonAndSaveMsg(message);
        }
    }

    private void setUmengChanelID() {
        int channelid = 0;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            channelid = info.metaData.getInt("TEA_CHANNELID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(channelid == 0){
            channelid = 10100;
        }
        AnalyticsConfig.setChannel(channelid + "");
    }

    private void startService() {
        if(PackageUtils.isServiceRunning(this,XmppService.class.getName())){
        }else {//开启服务连接xmpp服务器
            Intent service1 = new Intent();
            service1.setClass(this, XmppService.class);
            startService(service1);
            service1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }
    private StatisticsMode statisticsMode = RetrofitUtils.create(StatisticsMode.class);
    //向服务器发送设备信息
    private void sendDevice() {
        statisticsMode.sendDevice(new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        FragmentTransaction ft = fm.beginTransaction();
        ft.attach(fragments.get(fragments.size() - 1));
        ft.commit();
    }

    private class ThemeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mHasThemeChanged = true;
        }
    }

    private void queryProps(){
        GiftModel model = RetrofitUtils.create(GiftModel.class);
        model.queryProps(new DefaultLiveHttpCallBack<PropBeanList>() {
            @Override
            public void success(PropBeanList propBeanList) {
                CacheUtils.savePropList(propBeanList);//保存
               for (int i = 0;i<propBeanList.props.size();i++){
                    DownLoadImageUtil.loadfile(MainApplication.getInstance(), propBeanList.props.get(i).bgimg,propBeanList.props.get(i).name+".png");
                }
            }

            @Override
            public void failure(String message) {

            }
        });
    }
}
