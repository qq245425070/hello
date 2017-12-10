package cn.gietv.mlive.modules.video.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bright.videoplayer.widget.MediaController;
import com.bright.videoplayer.widget.media.VideoView;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.player.plugin.YoukuPlayerListener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.Map;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.db.DBUtils;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.program.bean.ProgramBean;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity2;
import cn.gietv.mlive.modules.video.activity.VideoPlayerActivity2;
import cn.gietv.mlive.modules.video.bean.DanmuBean;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HttpUtils;
import cn.gietv.mlive.utils.InputUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.kalading.android.retrofit.utils.GsonUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by houde on 2016/7/29.
 */
public class VideoPlayFragment4 extends AbsBaseFragment {
    public static final int FULL_SCREEN_MODEL = 0;
    public static final int SMALL_SCREEN_MODEL = 1;
    private View mRootView;
    private String mVideoPath;
    private String mID;
    private MessageModel model;
    private MediaController mMediaController;
    private String mVideoName;
    public VideoView mVideoView;
    private int mScreenModel;
    private boolean isPlaying = true;
    private int currentPotion;
    private MultiUserChat mChatRoom;
    private Map<String,String> mMessageMap;
    private LiveMqttMessageFragment mDanmuFragment;
    private YoukuPlayerView mYoukuVideoView;
    private YoukuPlayer youkuPlayer;
    private YoukuBasePlayerManager basePlayerManager;
    public static VideoPlayFragment4 getInstence(String path,String proId,String name,int videoType,int screenType,String urlfrom) {//优酷视频专用
        VideoPlayFragment4 fragment = new VideoPlayFragment4();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("id", proId);
        bundle.putString("name", name);
        bundle.putInt("videoType", videoType);
        bundle.putInt("type",screenType);
        bundle.putString("urlfrom",urlfrom);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static VideoPlayFragment4 getInstence(String path,String proId,String name,int videoType,int screenType) {
        VideoPlayFragment4 fragment = new VideoPlayFragment4();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("id", proId);
        bundle.putString("name", name);
        bundle.putInt("videoType", videoType);
        bundle.putInt("type",screenType);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static VideoPlayFragment4 getInstence(String path,String proId,int screenType,int position,String name,int videoType) {
        VideoPlayFragment4 fragment = new VideoPlayFragment4();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("id", proId);
        bundle.putInt("type", screenType);
        bundle.putInt("position", position);
        bundle.putString("name", name);
        bundle.putInt("videoType", videoType);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.video_layout4, container,false);
        mYoukuVideoView = (YoukuPlayerView) mRootView.findViewById(R.id.full_holder);
        mVideoView = (VideoView) mRootView.findViewById(R.id.video_view);
        getActivity().getSystemService(Context.WINDOW_SERVICE);
        mVideoPath = getArguments().getString("path");
        mID = getArguments().getString("id");
        model = RetrofitUtils.create(MessageModel.class);
        mVideoName = getArguments().getString("name");
        mScreenModel = getArguments().getInt("type");
        currentPotion = getArguments().getInt("position");
        String urlfrom = getArguments().getString("urlfrom");
        if(urlfrom == null) {
            if(mScreenModel != FULL_SCREEN_MODEL){
                ProgramBean.ProgramEntity entity = DBUtils.getInstance(getActivity()).getVideoById(mID);
                if(entity != null){
                    currentPotion = entity.position;
                }
            }
            findView();
        }else {
            initYoukuView();
        }
        addDanmuFragment();
        getDanmu();
        return mRootView;
    }

    private void initYoukuView() {
        mVideoView.setVisibility(View.GONE);
        Log.e("ceshi",getActivity().toString());
        basePlayerManager = new YoukuBasePlayerManager(getActivity()) {

            @Override
            public void setPadHorizontalLayout() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                // TODO Auto-generated method stub
                // 初始化成功后需要添加该行代码
                addPlugins();
                // 实例化YoukuPlayer实例
                youkuPlayer = player;
                // 进行播放
                goPlay();

            }

            @Override
            public void onSmallscreenListener() {
                ToastUtils.showToastShort(getActivity(),"onSmallscreenListener");
                Log.e("ceshi","onSmallscreenListener");
                ((VideoPlayListActivity2)getActivity()).mPager.setVisibility(View.VISIBLE);
                ((VideoPlayListActivity2)getActivity()).mLine.setVisibility(View.VISIBLE);
                ((VideoPlayListActivity2)getActivity()).mRelativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFullscreenListener() {
                ToastUtils.showToastShort(getActivity(),"onFullscreenListener");
                Log.e("ceshi","onFullscreenListener");
                ((VideoPlayListActivity2)getActivity()).mPager.setVisibility(View.GONE);
                ((VideoPlayListActivity2)getActivity()).mLine.setVisibility(View.GONE);
                ((VideoPlayListActivity2)getActivity()).mRelativeLayout.setVisibility(View.GONE);
            }
        };
        basePlayerManager.onCreate();

        mYoukuVideoView
                .setSmallScreenLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
        mYoukuVideoView
                .setFullScreenLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
        mYoukuVideoView.initialize(basePlayerManager);
        // 添加播放器的回调
        basePlayerManager.setPlayerListener(new YoukuPlayerListener() {

            @Override
            public void onCompletion() {
                // TODO Auto-generated method stub
                super.onCompletion();
            }
        });
    }
    private void goPlay() {
        youkuPlayer.playVideo(mVideoPath);
    }

    private void findView() {
        mYoukuVideoView.setVisibility(View.GONE);
        mMediaController = new MediaController(getActivity());
        mMediaController.setCallBack(mCallBack);
        mMediaController.setPlayNextVisibility(View.GONE);
        mMediaController.setScreenModel(mScreenModel);
        mMediaController.setKeepScreenOn(true);
        mMediaController.setMediaControllerListener(new MediaController.MediaControllerListener() {
            @Override
            public void fullScreen() {
                Bundle bundle = new Bundle();
                bundle.putString("proId", getArguments().getString("id"));
                bundle.putInt("position", mVideoView.getCurrentPosition());
                bundle.putString("name", getArguments().getString("name"));
                bundle.putString("videoPath", mVideoPath);
                Intent intent = new Intent(getActivity(), VideoPlayerActivity2.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }

            @Override
            public void smallScreen() {

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
                    ((VideoPlayerActivity2) getActivity()).exitActivity(mVideoView.getCurrentPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void smalScreenExit() {
                getActivity().finish();
            }

            @Override
            public void sendDanmu(EditText editText) {
                VideoPlayFragment4.this.sendDanmu(editText);
            }

            @Override
            public void reportVideo() {
                if (mReportListener != null)
                    mReportListener.showReportPopuWindow(2, mID);
            }

            @Override
            public void cancelInput(EditText editText) {
                InputUtils.closeInputKeyBoard(editText);
                mMediaController.hideDanmuInput();
                mMediaController.setShowInput(false);
            }

            @Override
            public void danmuSwitch() {

            }
        });
        mVideoView.setMediaController(mMediaController);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(basePlayerManager != null)
            basePlayerManager.onConfigurationChanged(newConfig);
        //
    }
    private ReportListener mReportListener;
    public void setShowReportWindowListener(ReportListener reportWindow){
        this.mReportListener = reportWindow;
    }
    public interface ReportListener{
        void showReportPopuWindow(int type, String id);
    }
    private void addDanmuFragment() {
        mDanmuFragment = new LiveMqttMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "video");
        mDanmuFragment.setArguments(bundle);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.danmu_fragment, mDanmuFragment);
        ft.commit();
    }
    private void sendDanmu(final EditText mDanmuText) {
        final String content = mDanmuText.getText().toString();
        if(UserUtils.isNotLogin()){
            IntentUtils.openActivity(getContext(), LoginActivity.class);
            return;
        }
        if(TextUtils.isEmpty(content)){
            ToastUtils.showToastShort(getContext(), "输入弹幕内容再提交");
            return;
        }
        model.sendDanmu(mID, content, mVideoView.getCurrentPosition() / 1000, new DefaultLiveHttpCallBack<DanmuBean.DanmuEntity>() {
            @Override
            public void success(DanmuBean.DanmuEntity s) {
                mDanmuText.setText("");
                InputUtils.closeInputKeyBoard(mDanmuText);
                try {
                    s.videotimestamp = mVideoView.getCurrentPosition()/1000 + 2;
                    mChatRoom.sendMessage(GsonUtils.getGson().toJson(s));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mScreenModel == 0) {
                    mMediaController.hideDanmuInput();
                    mMediaController.setShowInput(false);
                }
            }

            @Override
            public void failure(String message) {

            }
        });
    }
    private void preparedVideo() {
       mVideoView.setVideoPath(mVideoPath);
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
        new Thread(){
            @Override
            public void run() {
                mChatRoom = XmppConnection.getInstance().joinMultiUserChat(mID, ConfigUtils.TOPIC_LUBO+mID,"");
                myPacketListener = new MyPacketListener();
                if(mChatRoom != null) {
                    mChatRoom.addMessageListener(myPacketListener);
                }else{
                    XMPPConnection connection = XmppConnection.getInstance().getConnection();
                    if(connection != null){
                        mChatRoom = XmppConnection.getInstance().joinMultiUserChat(mID, ConfigUtils.TOPIC_LUBO+mID,"");
                        if(mChatRoom != null)
                            mChatRoom.addMessageListener(myPacketListener);
                    }
                }
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mVideoView != null) {
            isPlaying = mVideoView.isPlaying();
            currentPotion = mVideoView.getCurrentPosition();
        }
        if(basePlayerManager != null) {
            basePlayerManager.onPause();
        }
    }
    private int flag = 0;
    private Dialog mDialog;
    @Override
    public void onResume() {
        super.onResume();
        if(flag == 0) {
            if (HttpUtils.getNetworkType(getActivity()) != 1) {
                if (mDialog == null) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.update_dialog_layout, null);
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
                            preparedVideo();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view);
                    mDialog = builder.create();
                }
                mDialog.show();
            } else {
                flag = 1;
                preparedVideo();
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

    @Override
    public void onDestroy() {
        mVideoView.stopPlayback();
        mVideoView.release(true);
        IjkMediaPlayer.native_profileEnd();
        if(mChatRoom != null) {
            mChatRoom.removeMessageListener(myPacketListener);
        }
        if(basePlayerManager != null)
            basePlayerManager.onDestroy();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            int backPosition =data.getIntExtra("position", currentPotion);
            currentPotion = backPosition;
            mVideoView.seekTo(currentPotion);
        }
        mVideoView.start();
    }
    private Handler mHandler = new Handler();
    public void getDanmu() {
        model.getDanmuByProId(mID, 99999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<DanmuBean>() {
            @Override
            public void success(DanmuBean messageBean) {
                if (isNotFinish() && messageBean != null) {
                    if (isNotFinish() && messageBean.messages != null) {
                        mMessageMap = new HashMap<>();
                        for (int i = 0; i < messageBean.messages.size(); i++) {
                            putValue(messageBean.messages.get(i));
                        }
                        mHandler.postDelayed(mDanmuRunnable, 1000);
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    ToastUtils.showToastShort(getContext(), message);
                }
            }
        });
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
            long currentPosition = mVideoView.getCurrentPosition()/1000;
            if(temp != currentPosition){
                if (mMessageMap != null && mMessageMap.containsKey(String.valueOf(currentPosition))) {
                    if (mMessageMap.get(String.valueOf(currentPosition)) != null) {
                        String[] messages = mMessageMap.get(String.valueOf(currentPosition)).split("#$%123");
                        for (int i = 0; i < messages.length; i++) {
                            mDanmuFragment.addDanmaku(messages[i], ConfigUtils.DANMU_TEXT_SIZE);
                        }
                    }
                    temp = currentPosition;
                }
            }
            mHandler.postDelayed(mDanmuRunnable,800);
        }
    };
}
