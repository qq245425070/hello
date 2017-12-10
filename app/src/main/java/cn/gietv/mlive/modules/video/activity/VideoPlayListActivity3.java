package cn.gietv.mlive.modules.video.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bright.videoplayer.utils.ScreenOrientationUtils;
import com.bright.videoplayer.widget.MediaController;
import com.bright.videoplayer.widget.media.VideoView;
import com.youku.player.ApiManager;
import com.youku.player.VideoQuality;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.player.plugin.PluginSimplePlayer;
import com.youku.player.plugin.YoukuPlayerListener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.Map;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.statistics.StatisticsMode;
import cn.gietv.mlive.modules.video.adapter.VideoPlayListPagerAdapter;
import cn.gietv.mlive.modules.video.bean.DanmuBean;
import cn.gietv.mlive.modules.video.bean.VideoPlayBean;
import cn.gietv.mlive.modules.video.fragment.CommentFragment;
import cn.gietv.mlive.modules.video.fragment.LiveMqttMessageFragment;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment2;
import cn.gietv.mlive.modules.video.fragment.VideoPlayFragment4.ReportListener;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.modules.video.model.VideoModel;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.utils.AndroidBug5497Workaround;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.HttpUtils;
import cn.gietv.mlive.utils.InputUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.SharedPreferenceUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.views.ReportPopuWindow;
import cn.gietv.mlive.views.VideoGuidePopuWindow;
import cn.gietv.mlive.views.slidingTab.SlidingTabLayout;
import cn.kalading.android.retrofit.utils.GsonUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by houde on 2016/5/29.
 *  mMediaController.setScreenModel(1);控制取消文字的隐藏
 *  mMediaController.setScreenModel(0);控制取消文字的显示
 *   mMediaController.setShowInput(true);控制弹幕发送框一直显示
 *   mMediaController.setShowInput(false);控制弹幕发送框随控制隐藏一起隐藏
 *   mMediaController.hideDanmuInput();隐藏弹幕发送框
 *   mMediaController.showDanmuInput();显示弹幕发送框（这两个需要配合上面的true或者false使用）
 */
public class VideoPlayListActivity3 extends AbsBaseActivity {
    private ViewPager mPager;
    private SlidingTabLayout mIndicator;
    private RelativeLayout mRelativeLayout;
    private View mLine;
    public ProgramBean.ProgramEntity mProgram;
    private VideoModel model;
//    private VideoPlayFragment4 mInstance;
    private StatisticsMode statisticsMode;
    private long mCurrentDate;
    private long mStartTime;
    private long mTotalTime;
    public TextView mSignText;
    private VideoPlayListPagerAdapter pagerAdapter;
    public ImageView mSign;
    public String resourceid;
    public int resourceType;
    private Context mContext;

    private LiveMqttMessageFragment mDanmuFragment;
    private YoukuPlayerView mYoukuVideoView;
    private YoukuPlayer youkuPlayer;
    private YoukuBasePlayerManager basePlayerManager;
    public VideoView mVideoView;
    private MediaController mMediaController;

    private MessageModel messageModel;
    private MultiUserChat mChatRoom;
    private Map<String,String> mMessageMap;
    private Handler mHandler = new Handler();
    private boolean isPlaying = true;
    private FrameLayout mDanmuLayout;
    private Window window;
    public static void openVideoPlayListActivity2(Context context,ProgramBean.ProgramEntity bean){
        Bundle bundle = new Bundle();
        bundle.putSerializable("program", bean);
        IntentUtils.openActivity(context, VideoPlayListActivity3.class, bundle);
    }

    public void setHideVirtualKey(Window window){
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT>=19){
            uiOptions |= 0x00001000;
        }else{
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置透明状态栏
//        NotificationTitleBarUtils.setNotificationColor(this,"#000000");
        NotificationTitleBarUtils.setTranspanrentNotification(this);
//        NotificationTitleBarUtils.hideSystemUI(getWindow().getDecorView());
        setContentView(R.layout.activity_video_play_list3);
        window = getWindow();
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
               setHideVirtualKey(window);
            }
        });
        mYoukuVideoView = (YoukuPlayerView) findViewById(R.id.full_holder);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mDanmuLayout = (FrameLayout) findViewById(R.id.danmu_fragment);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        mLine = findViewById(R.id.line);
        AndroidBug5497Workaround.assistActivity(this);
        mContext = this;
        mProgram = (ProgramBean.ProgramEntity) getIntent().getSerializableExtra("program");
        if(mProgram == null){
            ToastUtils.showToast(this,"视频错误，请退出重试");
            return;
        }
        messageModel = RetrofitUtils.create(MessageModel.class);
        mStartTime = System.currentTimeMillis();
        statisticsMode = RetrofitUtils.create(StatisticsMode.class);
        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (SlidingTabLayout) findViewById(R.id.tab_indicator);
        mSignText = (TextView) findViewById(R.id.sign);
        mSign = (ImageView) findViewById(R.id.sign_image);

        model = RetrofitUtils.create(VideoModel.class);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mHeight = windowManager.getDefaultDisplay().getHeight();
        findView();
        model.queryProByProid(mProgram._id, new DefaultLiveHttpCallBack<VideoPlayBean>() {
            @Override
            public void success(VideoPlayBean videoPlayBean) {
                if (isNotFinish()) {
                    if (videoPlayBean == null)
                        return;
                    if (videoPlayBean.program == null) {
                        return;
                    }
                    mProgram = videoPlayBean.program;
                    String[] titles = new String[]{"简介", "评论" };
                    CommentFragment fragment = CommentFragment.getInstance(mProgram._id,mProgram.urlfrom);
                    fragment.setShowReportWindowListener(listener);
                    pagerAdapter = new VideoPlayListPagerAdapter(getSupportFragmentManager(), titles, mProgram._id,fragment);
                    mPager.setAdapter(pagerAdapter);
                    mIndicator.setViewPager(mPager);
                    setPagerAdapter();
                    openFragment();

                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToast(VideoPlayListActivity3.this, message);
                }
            }
        });

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (mSignText.getVisibility() == View.VISIBLE) {
                        mSignText.setTextColor(Color.parseColor("#4fc396"));
                    }
                } else {
                    mSignText.setTextColor(Color.parseColor("#bbbdbf"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTotalTime += System.currentTimeMillis() - mCurrentDate;
        if(mVideoView != null) {
            isPlaying = mVideoView.isPlaying();
            currentPotion = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
        if(basePlayerManager != null) {
            basePlayerManager.onPause();
        }

    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mDanmuRunnable);
        if(mProgram != null && mProgram.type == 2) {
            statisticsMode.sendProgramTime(mProgram._id,mTotalTime, mProgram.name, mProgram.type,mStartTime,System.currentTimeMillis(), new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {

                }

                @Override
                public void failure(String message) {

                }
            });
        }
        if(mYoukuVideoView.getVisibility() == View.GONE){
            if( mVideoView != null){
                DBUtils.getInstance(this).saveUserVideo(mProgram._id,mVideoView.getCurrentPosition(),"");
            }
        }else{
            if(mYoukuVideoView != null && null != mYoukuVideoView.mMediaPlayerDelegate){
                Log.e("ceshi",mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition() + "    = mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition()");
                Log.e("ceshi",DBUtils.getInstance(this).saveUserVideo(mProgram._id,mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition(),"") + "  保存结果");
            }
        }
        mVideoView.stopPlayback();
        mVideoView.release(true);
        IjkMediaPlayer.native_profileEnd();
        if(mChatRoom != null) {
            mChatRoom.removeMessageListener(myPacketListener);
        }
        if(basePlayerManager != null)
            basePlayerManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(basePlayerManager != null)
            basePlayerManager.onLowMemory();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(basePlayerManager != null)
            basePlayerManager.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentDate = System.currentTimeMillis();
        addDanmuFragment();
        if(isPlaying && mVideoView != null){
            mVideoView.start();
        }else{
            mVideoView.start();
            mVideoView.pause();
        }

        new Thread(){
            @Override
            public void run() {
                mChatRoom = XmppConnection.getInstance().joinMultiUserChat(mProgram._id, ConfigUtils.TOPIC_LUBO+mProgram._id,"");
                myPacketListener = new MyPacketListener();
                if(mChatRoom != null) {
                    mChatRoom.addMessageListener(myPacketListener);
                }else{
                    XMPPConnection connection = XmppConnection.getInstance().getConnection();
                    if(connection != null){
                        mChatRoom = XmppConnection.getInstance().joinMultiUserChat(mProgram._id, ConfigUtils.TOPIC_LUBO+mProgram._id,"");
                        if(mChatRoom != null)
                            mChatRoom.addMessageListener(myPacketListener);
                    }
                }
            }
        }.start();
        mHandler.postDelayed(hiteNavigation,100);
        if(SharedPreferenceUtils.getBoolean(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_SMALL_VIDEO_PAGE,true)) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGuidePopuWindow();
                }
            }, 100);
        }
    }
    private void showGuidePopuWindow(){
        SharedPreferenceUtils.saveProp(ConfigUtils.FIRST_VERSION + ConfigUtils.FIRST_IN_SMALL_VIDEO_PAGE,false);
        VideoGuidePopuWindow popuWindow = new VideoGuidePopuWindow(this);
        popuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popuWindow.setFocusable(true);
        popuWindow.setOutsideTouchable(true);
        popuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_video_play_list3,null);
        popuWindow.showAtLocation(view,Gravity.NO_GRAVITY,0,0);

    }
    private Runnable hiteNavigation = new Runnable() {
        @Override
        public void run() {
            setHideVirtualKey(window);
        }
    };
    private Dialog mDialog;
    private int flag;
    private void checkNet(final String path) {
        if(flag == 0) {
            if (HttpUtils.getNetworkType(this) != 1) {
                if (mDialog == null) {
                    View view = LayoutInflater.from(this).inflate(R.layout.update_dialog_layout, null);
                    TextView title = (TextView) view.findViewById(R.id.update_tv_title);
                    TextView content = (TextView) view.findViewById(R.id.update_tv_content);
                    TextView sure = (TextView) view.findViewById(R.id.update_tv_sure);
                    TextView cancel = (TextView) view.findViewById(R.id.update_tv_cancel);
                    sure.setText("继续播放");
                    cancel.setText("取消播放");
                    title.setText("提示");
                    content.setText("您当前观看视频使用的不是WIFI，检查网络");
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                            preparedVideo(path);
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(view);
                    mDialog = builder.create();
                }
                mDialog.show();
            } else {
                flag = 1;
                preparedVideo(path);
            }
        }else{
            if(mVideoView != null){
                if(isPlaying) {
                    mVideoView.start();
                }
                mVideoView.seekTo(currentPotion);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mVideoView != null) {
            if (mVideoView.canPause()) {
                mVideoView.pause();
            }
        }
        if(basePlayerManager != null)
            basePlayerManager.onStop();
    }
    private int mHeight;
    private void openFragment (){
        if("youku".equals(mProgram.urlfrom) || "优酷".equals(mProgram.urlfrom)){
            ((ViewGroup)mVideoView.getParent()).removeView(mVideoView);
            initYouKuPlayer(mProgram._id);
            getDanmu();
            return;
        }
        model.getPlayUrl(mProgram._id, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if (isNotFinish()) {
                    if(s == null || TextUtils.isEmpty(s)){
                        ToastUtils.showToastShort(VideoPlayListActivity3.this,"返回的播放地址为空，请重新请求");
                        return;
                    }
                    if (mProgram.type == 2) {
                        getDanmu();
                        checkNet(s);
                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.video_fragment, VideoPlayFragment2.getInstence(s, mProgram.spic, mProgram._id, mProgram.type, mProgram.name));
                        ft.commitAllowingStateLoss();
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish())
                    ToastUtils.showToastShort(VideoPlayListActivity3.this, message);
            }
        });
    }
    private void addDanmuFragment() {
        mDanmuFragment = new LiveMqttMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "video");
        mDanmuFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.danmu_fragment, mDanmuFragment);
        ft.commit();
    }
    private MediaController.CallBack mCallBack = new MediaController.CallBack() {
        @Override
        public void onPlay(boolean isPlaying) {

        }

        @Override
        public void onComplete() {
            mVideoView.seekTo(0);
            mVideoView.start();
        }

        @Override
        public void onPlayNext() {

        }
    };
    private ReportPopuWindow mReportPopuWindow;
    private ReportListener listener= new ReportListener(){
        @Override
        public void showReportPopuWindow(int type, String id) {
            VideoPlayListActivity3.this.showReportPopuWindow(type,id);
        }
    };
    public void showReportPopuWindow(int type, String id){
        int popuWindowHeight = DensityUtil.dip2px(mContext, 122);
        if(mReportPopuWindow == null){
            mReportPopuWindow = new ReportPopuWindow(mContext,id,type);
            mReportPopuWindow.setHeight(popuWindowHeight);
            mReportPopuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mReportPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mReportPopuWindow.setFocusable(true);
            mReportPopuWindow.setOutsideTouchable(true);
        }else {
            mReportPopuWindow.setTypeAndID(type,id);
        }
        mReportPopuWindow.showAtLocation(mPager, Gravity.NO_GRAVITY,0,mHeight - popuWindowHeight);
    }
    private void setPagerAdapter() {
        if (mProgram.message_cnt  > 0) {
            mSign.setVisibility(View.INVISIBLE);
            mSignText.setText("(" + mProgram.message_cnt + ")");
            mSignText.setTextColor(Color.parseColor("#bbbdbf"));
        }else{
            mSignText.setVisibility(View.INVISIBLE);
        }
    }
    private LinearLayout.LayoutParams mLp;
    private FrameLayout mVideoParent;
    private void initYouKuPlayer(final String videoPath){
        basePlayerManager = new YoukuBasePlayerManager(this) {

            @Override
            public void setPadHorizontalLayout() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                // TODO Auto-generated method stub
                // 初始化成功后需要添加该行代码
                addPlugins();
                //给添加的控件添加点击事件
                setListener();
                // 实例化YoukuPlayer实例
                youkuPlayer = player;
                // 进行播放
                Log.e("ceshi","youku  id = " + videoPath);
                youkuPlayer.playVideo(videoPath);

            }

            @Override
            public void onSmallscreenListener() {
                mLp.height = getResources().getDimensionPixelOffset(R.dimen.common_video_height);
                mYoukuVideoView.post(new Runnable() {
                    @Override
                    public void run() {
                        mVideoParent.setLayoutParams(mLp);
                    }
                });
                ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).setShowing(true);
                if(((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuParent != null)
                    ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuParent.setVisibility(View.VISIBLE);
                if(((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuImage != null)
                    ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuImage.setVisibility(View.GONE);
                if(((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mCancelText != null)
                    ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mCancelText.setVisibility(View.GONE);
                ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).fullSmallBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFullscreenListener() {
                mVideoParent = ((FrameLayout)mYoukuVideoView.getParent());
                mLp = (LinearLayout.LayoutParams) mVideoParent.getLayoutParams();
                mLp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                mYoukuVideoView.post(new Runnable() {
                    @Override
                    public void run() {
                        mVideoParent.setLayoutParams(mLp);
                    }
                });
                ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).setShowing(false);
                if(((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuParent != null)
                    ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuParent.setVisibility(View.GONE);
                if(((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuImage != null)
                    ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuImage.setVisibility(View.VISIBLE);
                if(((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mCancelText != null)
                    ((PluginSimplePlayer)basePlayerManager.mPluginSmallScreenPlay).mDanmuImage.setVisibility(View.VISIBLE);
            }
        };
        basePlayerManager.onCreate();

        mYoukuVideoView
                .setSmallScreenLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        getResources().getDimensionPixelOffset(R.dimen.common_video_height)));
        mYoukuVideoView
                .setFullScreenLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
        mYoukuVideoView.initialize(basePlayerManager);

        // 添加播放器的回调
        basePlayerManager.setPlayerListener(new YoukuPlayerListener() {
            @Override
            public void onPrepared() {
                super.onPrepared();

            }

            @Override
            public void onCompletion() {
                // TODO Auto-generated method stub
                super.onCompletion();

            }

            @Override
            public void onRealVideoStart() {
                super.onRealVideoStart();
                currentPotion = DBUtils.getInstance(VideoPlayListActivity3.this).getVideoById(mProgram._id) == null ? 0 : DBUtils.getInstance(VideoPlayListActivity3.this).getVideoById(mProgram._id).position;
                Log.e("ceshi","mYoukuVideoView currentPosition = " + currentPotion);
                mYoukuVideoView.mMediaPlayerDelegate.seekTo(currentPotion);
                mHandler.post(mDanmuRunnable);

            }

            @Override
            public void onGetInfoStart() {
                super.onGetInfoStart();
                //设置视频的清晰度
                ApiManager.getInstance().changeVideoQuality(VideoQuality.SUPER,basePlayerManager);
            }
        });

        PluginSimplePlayer pluginSmallScreenPlay = (PluginSimplePlayer) basePlayerManager.mPluginSmallScreenPlay;
        pluginSmallScreenPlay.setVideoPlayErrorListener(new PluginSimplePlayer.VideoPlayErrorListener() {
            @Override
            public void onPlayErrorListener() {

            }
        });
    }

    private void setListener() {
        final PluginSimplePlayer pluginSmallScreenPlay = (PluginSimplePlayer) basePlayerManager.mPluginSmallScreenPlay;
        pluginSmallScreenPlay.mDanmuContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            VideoPlayListActivity3.this.sendDanmu(pluginSmallScreenPlay.mDanmuContent);
                            return true;
                        default:
                            return true;
                    }
                }

                return false;
            }
        });
        pluginSmallScreenPlay.mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pluginSmallScreenPlay.setShowing(false);
                pluginSmallScreenPlay.mDanmuParent.setVisibility(View.GONE);
                pluginSmallScreenPlay.fullSmallBtn.setVisibility(View.GONE);
            }
        });
        pluginSmallScreenPlay.mDanmuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pluginSmallScreenPlay.setShowing(true);
                pluginSmallScreenPlay.mCancelText.setVisibility(View.VISIBLE);
                pluginSmallScreenPlay.mDanmuParent.setVisibility(View.VISIBLE);
                InputUtils.closeInputKeyBoard(pluginSmallScreenPlay.mDanmuContent);

            }
        });
        pluginSmallScreenPlay.mSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayListActivity3.this.sendDanmu(pluginSmallScreenPlay.mDanmuContent);
            }
        });
        View view = pluginSmallScreenPlay.retryView;
        ImageView exitImage = (ImageView) view.findViewById(R.id.exit_icon);
        ImageView reportImage = (ImageView) view.findViewById(R.id.report);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportPopuWindow(mProgram.type,mProgram._id);
            }
        });
        pluginSmallScreenPlay.mDanmuSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDanmuFragment.controlDanmu();
                if(mDanmuFragment.getDanmuShowing())
                    pluginSmallScreenPlay.mDanmuSwitch.setImageResource(R.mipmap.danmu_off);
                else{
                    pluginSmallScreenPlay.mDanmuSwitch.setImageResource(R.mipmap.danmu_on);
                }
            }
        });
    }
    private FrameLayout frameLayout;
    public FrameLayout getFrameLayout(){
        return frameLayout;
    }
    private void findView() {
        mMediaController = new MediaController(this);
        frameLayout = (FrameLayout) findViewById(R.id.video_fragment);
        mMediaController.setLandVideoRootView(frameLayout);
        mMediaController.setCallBack(mCallBack);
        mMediaController.setPlayNextVisibility(View.GONE);
        mMediaController.setScreenModel(1);
        mMediaController.setKeepScreenOn(true);
        mMediaController.setMediaControllerListener(new MediaController.MediaControllerListener() {
            @Override
            public void fullScreen() {
                mMediaController.setShowInput(true);
                mMediaController.showDanmuInput();
            }

            @Override
            public void smallScreen() {
                mMediaController.setShowInput(false);
                mMediaController.hideDanmuInput();
//                mMediaController.getPortraitVideoRootView().removeView(mDanmuLayout);
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//                mMediaController.getLandVideoRootView().addView(mDanmuLayout,lp);
            }

            @Override
            public void showDanmuInput(final EditText editText) {
                mMediaController.setShowInput(true);
                mMediaController.showDanmuInput();
                editText.requestFocus();
                InputUtils.showInputKeyBoard(editText);
            }

            @Override
            public void fullScreenExit() {
                try {
                    changeSmallScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void smalScreenExit() {
                finish();
            }

            @Override
            public void sendDanmu(EditText editText) {
                VideoPlayListActivity3.this.sendDanmu(editText);
            }

            @Override
            public void reportVideo() {
                showReportPopuWindow(mProgram.type,mProgram._id);
            }

            @Override
            public void cancelInput(EditText editText) {
                InputUtils.closeInputKeyBoard(editText);
                mMediaController.hideDanmuInput();
                mMediaController.setShowInput(false);
            }

            @Override
            public void danmuSwitch() {
                if(mDanmuFragment != null){
                    mDanmuFragment.controlDanmu();
                }
            }
        });
        mVideoView.setMediaController(mMediaController);
    }

    private void sendDanmu(final EditText mDanmuText) {
        final String content = mDanmuText.getText().toString();
        if(UserUtils.isNotLogin()){
            IntentUtils.openActivity(this, LoginActivity.class);
            return;
        }
        if(TextUtils.isEmpty(content)){
            ToastUtils.showToastShort(this, "输入弹幕内容再提交");
            return;
        }
        int videoCurrentPosition = 0;
        if(mYoukuVideoView.getVisibility() == View.GONE){
            videoCurrentPosition = mVideoView.getCurrentPosition() / 1000;
        }else{
            videoCurrentPosition = mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition() / 1000;
        }
        messageModel.sendDanmu(mProgram._id, content, videoCurrentPosition, new DefaultLiveHttpCallBack<DanmuBean.DanmuEntity>() {
            @Override
            public void success(DanmuBean.DanmuEntity s) {
                mDanmuFragment.addDanmaku(s.message, ConfigUtils.DANMU_TEXT_SIZE);
                mDanmuText.setText("");
                InputUtils.closeInputKeyBoard(mDanmuText);
                try {
                    if(mYoukuVideoView.getVisibility() == View.GONE) {
                        s.videotimestamp = mVideoView.getCurrentPosition() / 1000 - 5;
                    }else{
                        Log.e("ceshi","mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition = " + mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition());
                        s.videotimestamp = mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition() / 1000 - 5;
                    }
                    mChatRoom.sendMessage(GsonUtils.getGson().toJson(s));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private int currentPotion;
    private void preparedVideo(String path) {
        mYoukuVideoView.setVisibility(View.GONE);
        mVideoView.setVideoPath(path);
        currentPotion = DBUtils.getInstance(this).getVideoById(mProgram._id) == null ? 0 : DBUtils.getInstance(VideoPlayListActivity3.this).getVideoById(mProgram._id).position;
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                            mp.pause();
                        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                            mp.start();
                        }
                        return true;
                    }
                });
                if(currentPotion == mVideoView.getCurrentPosition())
                    currentPotion = 0;
                mp.seekTo(currentPotion);
                mp.start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(ScreenOrientationUtils.isLandscape(this)){
            if(mYoukuVideoView.getVisibility() != View.GONE){
                mYoukuVideoView.mMediaPlayerDelegate.goSmall();
                return;
            }
           changeSmallScreen();
        }else {
            super.onBackPressed();
        }
    }

    private void changeSmallScreen() {
        mMediaController.changePortrait(false);
        mMediaController.setScreenModel(1);
        mMediaController.setShowInput(true);
        mMediaController.showDanmuInput();
        mMediaController.getLandVideoRootView().removeView(mDanmuLayout);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mMediaController.getPortraitVideoRootView().addView(mDanmuLayout,lp);
    }

    private MyPacketListener myPacketListener;
    class MyPacketListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {
            String message = ((org.jivesoftware.smack.packet.Message)packet).getBody();
            Log.e("ceshi",message);
            DanmuBean.DanmuEntity messagesEntity = GsonUtils.getGson().fromJson(message, DanmuBean.DanmuEntity.class);
            putValue(messagesEntity);
        }
    }
    private void putValue(DanmuBean.DanmuEntity message){
        if(mMessageMap.containsKey(message.videotimestamp)){
            String messages = mMessageMap.get(message.videotimestamp);
            mMessageMap.put(String.valueOf(message.videotimestamp),messages+"#$%123"+message.message);
        }else{
            mMessageMap.put(String.valueOf(message.videotimestamp), message.message);
        }
    }
    private long temp;
    private Runnable mDanmuRunnable= new Runnable() {
        public void run() {
            long currentPosition ;
            if(mYoukuVideoView.getVisibility() == View.GONE) {
                currentPosition = mVideoView.getCurrentPosition() / 1000;
            }else{
                currentPosition = mYoukuVideoView.mMediaPlayerDelegate.getCurrentPosition() / 1000;
            }
            if(temp != currentPosition){
                if (mMessageMap != null && mMessageMap.containsKey(String.valueOf(currentPosition))) {
                    if (mMessageMap.get(String.valueOf(currentPosition)) != null) {
                        String[] messages = mMessageMap.get(String.valueOf(currentPosition)).split("#$%123");
                        for (int i = 0; i < messages.length; i++) {
                            Log.e("ceshi","messages["+i+"] = " + messages[i]);
                            mDanmuFragment.addDanmaku(messages[i], ConfigUtils.DANMU_TEXT_SIZE);
                        }
                    }
                    temp = currentPosition;
                }
            }
            mHandler.postDelayed(mDanmuRunnable,800);
        }
    };
    public void getDanmu() {
        messageModel.getDanmuByProId(mProgram._id, 99999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<DanmuBean>() {
            @Override
            public void success(DanmuBean messageBean) {
                if (isNotFinish() && messageBean != null) {
                    if (isNotFinish() && messageBean.messages != null) {
                        mMessageMap = new HashMap<>();
                        for (int i = 0; i < messageBean.messages.size(); i++) {
                            putValue(messageBean.messages.get(i));
                        }
                        if(mYoukuVideoView.getVisibility() == View.GONE)
                            mHandler.postDelayed(mDanmuRunnable, 1000);
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToastShort(VideoPlayListActivity3.this, message);
                }
            }
        });
    }
}
