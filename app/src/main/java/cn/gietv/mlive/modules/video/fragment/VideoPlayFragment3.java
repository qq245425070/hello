package cn.gietv.mlive.modules.video.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.constants.CommConstants;
import cn.gietv.mlive.http.DefaultLiveHttpCallBack;
import cn.gietv.mlive.modules.login.activity.LoginActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity2;
import cn.gietv.mlive.modules.video.activity.VideoPlayerActivity2;
import cn.gietv.mlive.modules.video.bean.DanmuBean;
import cn.gietv.mlive.modules.video.model.MessageModel;
import cn.gietv.mlive.modules.xmpp.XmppConnection;
import cn.gietv.mlive.utils.CacheUtils;
import cn.gietv.mlive.utils.ConfigUtils;
import cn.gietv.mlive.utils.HttpUtils;
import cn.gietv.mlive.utils.InputUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.NotificationTitleBarUtils;
import cn.gietv.mlive.utils.ToastUtils;
import cn.gietv.mlive.utils.UserUtils;
import cn.gietv.mlive.utils.VersionUtils;
import cn.kalading.android.retrofit.utils.GsonUtils;
import cn.kalading.android.retrofit.utils.RetrofitUtils;
/**
 * author：steven
 * datetime：15/10/8 19:38
 */
public class VideoPlayFragment3 extends AbsBaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private View mRootView;
    public VideoView mVideoView;
    private ProgressBar mProgressBar;
    private ImageView mExitButton,mReportImage;
    private LinearLayout mSeekBarLayout;
    private TextView mTotalTime,mCurrentTime;
    private ImageView mPlayButton,mFullScreen;
    public int mPosition;
    private SeekBar mSeekBar;
    private String mVideoPath;
    private LiveMqttMessageFragment mDanmuFragment;
    private MessageModel model;
    private String mID;
    private EditText mDanmuText;
    private LinearLayout sendDanmuParent;
    private ImageView mDanmuSwitch;
    private int type;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private MultiUserChat mChatRoom;
    public static VideoPlayFragment3 getInstence(String path,String proId,String name,int videoType) {
        VideoPlayFragment3 fragment = new VideoPlayFragment3();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("id", proId);
        bundle.putString("name",name);
        bundle.putInt("videoType",videoType);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static VideoPlayFragment3 getInstence(String path,String proId,int type,int position,String name,int videoType) {
        VideoPlayFragment3 fragment = new VideoPlayFragment3();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("id", proId);
        bundle.putInt("type", type);
        bundle.putInt("position", position);
        bundle.putString("name",name);
        bundle.putInt("videoType",videoType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.video_layout3, null);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        mVideoPath = getArguments().getString("path");
        mPosition = getArguments().getInt("position", 0);
        mID = getArguments().getString("id");
        model = RetrofitUtils.create(MessageModel.class);
        findView();
        addDanmuFragment();
        mSeekBar.setOnSeekBarChangeListener(this);
        mExitButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mVideoView.setOnClickListener(this);
        mReportImage.setOnClickListener(this);
        getDanmu();
        return mRootView;
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

    private void findView(){
        mReportImage = (ImageView) mRootView.findViewById(R.id.jubao);
        mVideoView = (VideoView) mRootView.findViewById(R.id.video_view);
        mSeekBarLayout = (LinearLayout) mRootView.findViewById(R.id.seekBarLayout);
        mDanmuText = (EditText) mRootView.findViewById(R.id.danmu_text);
        sendDanmuParent = (LinearLayout) mRootView.findViewById(R.id.send_danmu_parent);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progress_bar);
        mTotalTime = (TextView) mRootView.findViewById(R.id.totaltime);
        mCurrentTime = (TextView) mRootView.findViewById(R.id.currenttime);
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.seekbar);
        mPlayButton = (ImageView) mRootView.findViewById(R.id.play);
        mFullScreen = (ImageView) mRootView.findViewById(R.id.full_screen);
        mExitButton = (ImageView) mRootView.findViewById(R.id.head_ib_exit);
        mDanmuSwitch = (ImageView) mRootView.findViewById(R.id.danmu_switch);
        type = getArguments().getInt("type", 0);
        if(type != 0){
            mFullScreen.setVisibility(View.INVISIBLE);
            mDanmuSwitch.setVisibility(View.VISIBLE);
            sendDanmuParent.setVisibility(View.INVISIBLE);
            mReportImage.setVisibility(View.INVISIBLE);
        }else{
            mDanmuSwitch.setVisibility(View.INVISIBLE);
        }
        mRootView.findViewById(R.id.video_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    mExitButton.setVisibility(View.INVISIBLE);
                    mSeekBarLayout.setVisibility(View.INVISIBLE);
                    mHandler.removeCallbacks(hintExitButton);
                    sendDanmuParent.setVisibility(View.INVISIBLE);
                    mReportImage.setVisibility(View.INVISIBLE);
                    NotificationTitleBarUtils.hideSystemUI(getActivity().getWindow().getDecorView());
                } else {
                    mExitButton.setVisibility(View.VISIBLE);
                    mSeekBarLayout.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(hintExitButton, 3000);
                    if(type != 0)
                        mReportImage.setVisibility(View.INVISIBLE);
                    else
                        mReportImage.setVisibility(View.VISIBLE);
                    if(type == 0){
                        sendDanmuParent.setVisibility(View.VISIBLE);
                    }else{
                        sendDanmuParent.setVisibility(View.INVISIBLE);
                    }

                    NotificationTitleBarUtils.showSystemUI(getActivity().getWindow().getDecorView());
                }
            }
        });
        mDanmuText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            sendDanmu();
                            return true;
                        default:
                            return true;
                    }
                }

                return false;
            }
        });
        mDanmuSwitch.setOnClickListener(this);
    }

    private void sendDanmu() {
        final String content = mDanmuText.getText().toString();
        if(UserUtils.isNotLogin()){
            IntentUtils.openActivity(getContext(), LoginActivity.class);
            return;
        }
        if(TextUtils.isEmpty(content)){
            ToastUtils.showToastShort(getContext(),"输入弹幕内容再提交");
            return;
        }
        model.sendDanmu(mID, content, mVideoView.getCurrentPosition()/1000, new DefaultLiveHttpCallBack<DanmuBean.DanmuEntity>() {
            @Override
            public void success(DanmuBean.DanmuEntity s) {
                mDanmuText.setText("");
                InputUtils.closeInputKeyBoard(mDanmuText);
                try {
                    mChatRoom.sendMessage(GsonUtils.getGson().toJson(s));
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
                mDanmuFragment.addDanmaku(content, ConfigUtils.DANMU_TEXT_SIZE);
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
        mCurrentPosition = mVideoView.getCurrentPosition();
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacks(hintExitButton);

    }
    private void preparedVideoView(){
        Log.e("ceshi", mVideoPath);
        try {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
        }catch (Exception e){
            e.printStackTrace();
        }

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                            mp.pause();
                        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                            mp.start();
                        }
                        return true;
                    }
                });
                mProgressBar.setVisibility(View.GONE);
                NotificationTitleBarUtils.showSystemUI(getActivity().getWindow().getDecorView());
                mHandler.postDelayed(hintExitButton, 3000);
                mTotalTime.setText(format.format(new Date(mp.getDuration())));
                mSeekBar.setMax(mVideoView.getDuration());
                mp.seekTo(mPosition);
                mp.start();
                mHandler.postDelayed(mDanmuRunnable,1000);
                //准备聊天室
                final String id ;
                if(UserUtils.isNotLogin()){
                    id = VersionUtils.getDeviceId(getActivity());
                }else{
                    id = CacheUtils.getCacheUserInfo()._id;
                }

                final XmppConnection xmppconnection = XmppConnection.getInstance();
                if(xmppconnection.getXMPPConnection() != null && xmppconnection.getXMPPConnection().isConnected()){
                    mChatRoom = XmppConnection.getInstance().joinMultiUserChat(id, ConfigUtils.TOPIC_LUBO+mID,"");
                    myPacketListener = new MyPacketListener();
                    mChatRoom.addMessageListener(myPacketListener);
                }
            }
        });
    }
    private Dialog mDialog;
    private int flag = 0;
    private int mCurrentPosition ;
    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(runnable, 1000);
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
                            preparedVideoView();
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
                preparedVideoView();
            }
        }else{
            mVideoView.start();
            mVideoView.seekTo(mCurrentPosition);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.head_ib_exit){
            if(mFullScreen.getVisibility() == View.VISIBLE) {
                getActivity().finish();
            }else {
                getActivity().setResult(Activity.RESULT_OK, new Intent().putExtra("position", mVideoView.getCurrentPosition()));
                getActivity().finish();
            }
        }else if(v.getId() == R.id.play){
           if(mVideoView.isPlaying()){
               mVideoView.pause();
               mPlayButton.setImageResource(R.mipmap.videopause);
               mHandler.removeCallbacks(runnable);
           }else{
               mVideoView.start();
               mPlayButton.setImageResource(R.mipmap.videoplay);
               mHandler.post(runnable);
           }
        }else if(v.getId() == R.id.full_screen){
            Bundle bundle = new Bundle();
            bundle.putString("proId", getArguments().getString("id"));
            bundle.putInt("position", mVideoView.getCurrentPosition());
            bundle.putString("name", getArguments().getString("name"));
            bundle.putString("videoPath", mVideoPath);
            IntentUtils.openActivityForResult(getActivity(), VideoPlayerActivity2.class, 66, bundle);
        }else if(v.getId() == R.id.danmu_switch){
            mHandler.post(hintExitButton);
            sendDanmuParent.setVisibility(View.VISIBLE);
            mDanmuText.requestFocus();
            InputUtils.showInputKeyBoard(mDanmuText);
        }else if(v.getId() == R.id.jubao){
            if(mActivity == null){
                try {
                    mActivity = (VideoPlayListActivity2) getActivity();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(mActivity != null){
                mActivity.resourceid = mID;
                mActivity.resourceType = 2;
            }

        }
    }
    private VideoPlayListActivity2 mActivity;
    private Handler mHandler = new Handler();
    private boolean isShowing(){
        return mExitButton.getVisibility() == View.VISIBLE;
    }
    private Runnable hintExitButton = new Runnable() {
        @Override
        public void run() {
            mExitButton.setVisibility(View.INVISIBLE);
            mSeekBarLayout.setVisibility(View.INVISIBLE);
            mReportImage.setVisibility(View.INVISIBLE);
            mReportImage.setVisibility(View.INVISIBLE);
            if( getActivity() != null && getActivity().getWindow() != null && getActivity().getWindow().getDecorView() != null)
                NotificationTitleBarUtils.hideSystemUI(getActivity().getWindow().getDecorView());
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mSeekBar.setProgress(Integer.parseInt(mVideoView.getCurrentPosition() + ""));
            mCurrentTime.setText(format.format(new Date(mVideoView.getCurrentPosition())) );
            if (mVideoView.getCurrentPosition() == mVideoView.getDuration())
                return;
            mHandler.postDelayed(runnable, 1000);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int position = seekBar.getProgress();
        if (fromUser == true) {
            mVideoView.seekTo(position);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    private Map<String,String> mMessageMap;
    public void getDanmu() {
        model.getDanmuByProId(mID, 99999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<DanmuBean>() {
            @Override
            public void success(DanmuBean messageBean) {
                if(isNotFinish() && messageBean != null ) {
                    if (isNotFinish() && messageBean.messages != null) {
                        mMessageMap = new HashMap<>();
                        for (int i = 0; i < messageBean.messages.size(); i++) {
                            putValue(messageBean.messages.get(i));
                        }
                        Log.e("ceshi",mMessageMap.toString());
                        mHandler.postDelayed(mDanmuRunnable,1000);
                    }
                }
            }

            @Override
            public void failure(String message) {
                if(isNotFinish()){
                    ToastUtils.showToastShort(getContext(),message);
                }
            }
        });
    }
    private void putValue(DanmuBean.DanmuEntity message){
        if(mMessageMap.containsKey(message.videotimestamp)){
            String messages = mMessageMap.get(message.videotimestamp);
            mMessageMap.put(String.valueOf(message.videotimestamp),messages+"#$%123"+message.message);
        }else{
            mMessageMap.put(String.valueOf(message.videotimestamp),message.message);
        }
    }
    private long temp;
    private Runnable mDanmuRunnable= new Runnable() {
        public void run() {
            long currentPosition = mVideoView.getCurrentPosition()/1000;
            if(temp != currentPosition){
                if (mMessageMap != null && mMessageMap.containsKey(String.valueOf(currentPosition))) {
                    String[] messages = mMessageMap.get(String.valueOf(currentPosition)).split("#$%123");
                    for (int i = 0; i < messages.length; i++) {
                        mDanmuFragment.addDanmaku(messages[i], ConfigUtils.DANMU_TEXT_SIZE);
                    }
                }
                temp = currentPosition;
            }
            mHandler.postDelayed(mDanmuRunnable,800);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatRoom.removeMessageListener(myPacketListener);
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
}
