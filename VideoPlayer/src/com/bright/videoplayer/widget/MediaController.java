/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author: y.haiyang@qq.com
 */

package com.bright.videoplayer.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bright.videoplayer.R;
import com.bright.videoplayer.utils.ScreenOrientationUtils;
import com.bright.videoplayer.utils.VideoUtils;
import com.bright.videoplayer.widget.media.IMediaController;
import com.bright.videoplayer.widget.media.VideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;


public class MediaController extends FrameLayout implements IMediaController, View.OnClickListener {
    private static final String TAG = "MediaController";

    /**
     * 更新进度条
     */
    private static final int HANDLER_UPDATE_PROGRESS = 1003;
    /**
     * 设置Activity位sensor控制
     */
    private static final int HANDLER_SCREEN_SENSOR = 1004;
    /**
     * 隐藏
     */
    private static final int HANDLER_HIDE_NAVIGATION = 1005;
    /**
     * 多长时间后重新设置为sensor控制
     */
    private static final int DEFAULT_DELAY_TIME_SET_SENSOR = 5000;
    private boolean mDragging;

    private VideoView mVideoView;
    /**
     * 普通功能的包裹区域
     */
    private View mNormalFeaturesContent;
    // Top panel 中包裹的
    private TextView mTitle;
    // 进度条
    private SeekBar mProgress;
    // 当前时间和总共多长时间
    private TextView mCurrentTime, mEndTime;

    private ImageView mFullScreenView;

    private ImageView mStartOrPauseView;
    private View mPlayNextView;
    /**
     * 加载功能的包裹区域
     */
    private View mLoadingContent;
    /**
     * 滑动功能区
     */
    private View mSlideContent;
    private ImageView mSlideIcon;
    private TextView mSlideTargetTime;
    private TextView mSlideTotleTime;
    /**
     * 广告区
     */
    private RelativeLayout mADContent;
    /*
    * 控制弹幕输入框显示和隐藏的
    * */
    private ImageView mDanmuImage;
    /**
     * 弹幕输入框
     * */
    private LinearLayout mDanmuParent;
    private EditText mDanmuContent;

    /*
    * 举报的图片
    * */
    private ImageView mReportImage;
    //发送按钮
    private ImageView mSendImage;
    //取消按钮
    private TextView mCancelText;

    //弹幕开关按钮
    private ImageView mDanmuSwitch;
    private ViewGroup mPortraitVideoRootView;
    private ViewGroup mLandVideoRootView;
    private MediaPlayerControl mPlayer;

    private CallBack mCallBack;



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UPDATE_PROGRESS:
                    int pos = setProgress();
                    Log.i(TAG, "isPlaying = " + mPlayer.isPlaying());
                    if (mPlayer.isPlaying()) {
                        msg = obtainMessage(HANDLER_UPDATE_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }

                    break;
                case HANDLER_SCREEN_SENSOR:
                    Log.i(TAG, "handleMessage: HANDLER_SCREEN_SENSOR = " + isCanAutoRotation());
                    if (isCanAutoRotation()) {
                        ScreenOrientationUtils.setSensor(getContext());
                    } else if (ScreenOrientationUtils.isLandscape(getContext())) {
                        ScreenOrientationUtils.setLandscape(getContext(), true);
                    } else {
                        ScreenOrientationUtils.setPortrait(getContext(), true);
                    }
                    break;
                case HANDLER_HIDE_NAVIGATION:
                    if (ScreenOrientationUtils.isLandscape(getContext())) {
                        hideSystemBar();
                    }
                    break;
            }
        }
    };

    public MediaController(Context context) {
        this(context, null);
    }

    public MediaController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.media_controller, this, true);

        mNormalFeaturesContent = findViewById(R.id.normal_content);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.title);

        mSendImage = (ImageView) findViewById(R.id.send_button);
        mSendImage.setOnClickListener(this);
        mDanmuParent = (LinearLayout) findViewById(R.id.send_danmu_parent);
        mDanmuContent = (EditText) findViewById(R.id.danmu_text);

        mCancelText = (TextView) findViewById(R.id.cancel);
        mCancelText.setOnClickListener(this);

        mReportImage = (ImageView) findViewById(R.id.report);
        mReportImage.setOnClickListener(this);
        mDanmuSwitch = (ImageView) findViewById(R.id.danmu_switch);
        mDanmuSwitch.setOnClickListener(this);
        mDanmuContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            if(mediaControllerListener != null)
                                mediaControllerListener.sendDanmu(mDanmuContent);
                            return true;
                        default:
                            return true;
                    }
                }

                return false;
            }
        });

        mFullScreenView = (ImageView) findViewById(R.id.full_screen);
        mFullScreenView.setOnClickListener(this);
        mDanmuImage = (ImageView) findViewById(R.id.danmu_screen);
        mDanmuImage.setOnClickListener(this);

        mStartOrPauseView = (ImageView) findViewById(R.id.start_or_pause);
        mStartOrPauseView.setOnClickListener(this);

        mPlayNextView = findViewById(R.id.play_next);
        mPlayNextView.setOnClickListener(this);

        mProgress = (SeekBar) findViewById(R.id.progress);
        mProgress.setOnSeekBarChangeListener(mSeekListener);
        mProgress.setMax(1000);

        mCurrentTime = (TextView) findViewById(R.id.current_time);
        mEndTime = (TextView) findViewById(R.id.end_time);

        // Loading 区域
        mLoadingContent = findViewById(R.id.loading_content);

        mSlideContent = findViewById(R.id.slide_content);
        mSlideIcon = (ImageView) findViewById(R.id.slide_icon);
        mSlideTargetTime = (TextView) findViewById(R.id.slide_time_target);
        mSlideTotleTime = (TextView) findViewById(R.id.slide_time_totle);

        mADContent = (RelativeLayout) findViewById(R.id.ad_content);
    }
    @Override
    public void hide() {
        Log.i(TAG, "timeout hide");
        mNormalFeaturesContent.setVisibility(GONE);
        if(mScreenModel == FULL_SCREEN && !showingInput){
            mDanmuParent.setVisibility(GONE);
        }
        mHandler.removeMessages(HANDLER_UPDATE_PROGRESS);

        mHandler.removeMessages(HANDLER_HIDE_NAVIGATION);
        mHandler.sendEmptyMessage(HANDLER_HIDE_NAVIGATION);
    }

    private boolean showingInput;
    public void setShowInput(boolean show){
        showingInput = show;
    }
    @Override
    public void show() {
        Log.i(TAG, "show");
        /**
         * 1. 先进性设置播放状态
         * 2. 更新进度条
         * 3. 显示
         */
        showSystemBar();
        mHandler.sendEmptyMessage(HANDLER_UPDATE_PROGRESS);
        syncPlayStatus();
        mNormalFeaturesContent.setVisibility(VISIBLE);
    }

    @Override
    public boolean isShowing() {
        return mNormalFeaturesContent.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showLoading() {
        if (mLoadingContent.getVisibility() == VISIBLE) {
            return;
        }
        mLoadingContent.setVisibility(VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingContent.setVisibility(GONE);
    }

    @Override
    public void showSlideView(long position, float distance) {
        mSlideContent.setVisibility(VISIBLE);
        int duration = mPlayer.getDuration();

        if (distance > 0) {
            mSlideIcon.setImageResource(R.drawable.ic_forward);
        } else {
            mSlideIcon.setImageResource(R.drawable.ic_backward);
        }

        mSlideTargetTime.setText(VideoUtils.generatePlayTime(position));
        mSlideTotleTime.setText(VideoUtils.generatePlayTime(duration));
    }

    @Override
    public void hideSlideView() {
        mSlideContent.setVisibility(GONE);
    }

    @Override
    public void effectiveSlide(long position) {
        mPlayer.seekTo((int) (position));
    }

    @Override
    public RelativeLayout getAdView() {
        return mADContent;
    }

    @Override
    public void setAnchorView(View view) {
        if (view == null) {
            return;
        }
        // 如果已经设置了 那么就直接退出就好
        if (mVideoView != null) {
            return;
        }

        // 如果有父类 那么先移除
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }

        mVideoView = (VideoView) view;
        mVideoView.addView(this);
        mVideoView.bringChildToFront(this);
        mVideoView.setOnCompletionListener(mCompletionListener);

        mPortraitVideoRootView = (ViewGroup) mVideoView.getParent();

        mHandler.sendEmptyMessage(HANDLER_SCREEN_SENSOR);
        registerContentObservers();
    }

    /**
     * 设置横屏时候的RootView
     */
    public void setLandVideoRootView(ViewGroup root) {
        mLandVideoRootView = root;
    }

    @Override
    public void setMediaPlayer(android.widget.MediaController.MediaPlayerControl player) {
        mPlayer = player;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }


    public void setPlayNextVisibility(int visiablilty) {
        mPlayNextView.setVisibility(visiablilty);
    }

    /*
    * 隐藏弹幕输入框
    * */
    public void hideDanmuInput(){
        mDanmuParent.setVisibility(GONE);
    }

    /*
    * 显示弹幕输入框
    * */
    public void showDanmuInput(){
        mDanmuParent.setVisibility(VISIBLE);
    }
    private final int FULL_SCREEN = 0;
    private final int SMALL_SCREEN = 1;
    private int mScreenModel = 0;
    public void setScreenModel(int model){
        this.mScreenModel = model;
        if(mScreenModel == FULL_SCREEN){
            mDanmuImage.setVisibility(VISIBLE);
            mReportImage.setVisibility(INVISIBLE);
            mCancelText.setVisibility(VISIBLE);
        }else if(mScreenModel == SMALL_SCREEN){
            mDanmuImage.setVisibility(GONE);
            mReportImage.setVisibility(VISIBLE);
            mCancelText.setVisibility(GONE);
        }
    }

    public interface MediaControllerListener{
        void fullScreen();//小屏页时，全屏按钮的点击事件
        void smallScreen();//全屏页时，全屏按钮的点击事件
        void showDanmuInput(EditText editText);//全屏页时，弹幕按钮的点击事件
        void fullScreenExit();//全屏页时，退出按钮的点击事件
        void smalScreenExit();//小屏页时，退出按钮的点击事件
        void sendDanmu(EditText editText);//软键盘的回车按钮的点击事件
        void reportVideo();//举报按钮的点击事件
        void cancelInput(EditText editText);//取消按钮的点击事件
        void danmuSwitch();// 弹幕开关
    }
    public MediaControllerListener mediaControllerListener;
    public void setMediaControllerListener(MediaControllerListener listener){
        this.mediaControllerListener = listener;
    }
    public ViewGroup getPortraitVideoRootView(){
        return  mPortraitVideoRootView;
    }
    public ViewGroup getLandVideoRootView(){
        if(mLandVideoRootView == null) {
            Activity activity = (Activity) getContext();
            mLandVideoRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        }
        return  mLandVideoRootView;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.danmu_screen){
            if(mediaControllerListener != null) {
                mediaControllerListener.showDanmuInput(mDanmuContent);
            }
        } else if (id == R.id.full_screen) {
            if (ScreenOrientationUtils.isLandscape(getContext())) {
                changePortrait(false);
                setScreenModel(SMALL_SCREEN);
                if(mediaControllerListener != null)
                    mediaControllerListener.fullScreen();
            } else {
                changeLand(false);
                setScreenModel(FULL_SCREEN);
                if(mediaControllerListener != null)
                    mediaControllerListener.smallScreen();
            }

        } else if (id == R.id.start_or_pause) {
            if (mCallBack != null) {
                mCallBack.onPlay(!mPlayer.isPlaying());
            }

            if (mPlayer.isPlaying()) {
                mStartOrPauseView.setImageResource(R.drawable.ic_play_play);
                mPlayer.pause();
            } else {
                mStartOrPauseView.setImageResource(R.drawable.ic_play_pause);
                mPlayer.start();
                mHandler.sendEmptyMessageDelayed(HANDLER_UPDATE_PROGRESS, 1000);
            }
        } else if (id == R.id.play_next) {
            if (mCallBack != null) {
                mCallBack.onPlayNext();
            }
        } else if (id == R.id.back) {
            if (ScreenOrientationUtils.isLandscape(getContext())) {
                if(mediaControllerListener != null)
                    mediaControllerListener.fullScreenExit();
            }else{
                if(mediaControllerListener != null)
                    mediaControllerListener.smalScreenExit();
            }
        } else if(id == R.id.report){
            if(mediaControllerListener != null)
                mediaControllerListener.reportVideo();
        }else if(id == R.id.send_button){
            if(mediaControllerListener != null)
                mediaControllerListener.sendDanmu(mDanmuContent);
        }else if(id == R.id.cancel){
            if(mediaControllerListener != null)
                mediaControllerListener.cancelInput(mDanmuContent);
        }else if (id == R.id.danmu_switch){
            if(danmuSwitch % 2 ==0){
                mDanmuSwitch.setImageResource(R.mipmap.danmu_off);
            }else{
                mDanmuSwitch.setImageResource(R.mipmap.danmu_on);
            }
            danmuSwitch ++;
            if(mediaControllerListener != null){
                mediaControllerListener.danmuSwitch();
            }
        }
    }
    private int danmuSwitch = 0;//改变弹幕开关的图片

    /**
     * 切换成横屏模式
     *
     * @param bySensor 是否是通过sensor来切换的
     */
    public void changeLand(boolean bySensor) {
        Activity activity = (Activity) getContext();
//        mPortraitVideoRootView.removeView(mVideoView);
        mFullScreenView.setImageResource(R.drawable.ic_to_smallscreen);
        mDanmuImage.setVisibility(VISIBLE);
//        ViewGroup root;
//        if (mLandVideoRootView != null) {
//            root = mLandVideoRootView;
//        } else {
//            root = (VideoP)activity.findViewById(android.R.id.content);
//        }
        if (mLandVideoRootView != null) {
            try {
                // 如果是通过sensor来切换的那么非强制更换
                ScreenOrientationUtils.setLandscape(activity, !bySensor);
                ScreenOrientationUtils.setStatusBarVisible(activity, true);
                mHandler.removeMessages(HANDLER_SCREEN_SENSOR);
                mHandler.sendEmptyMessageDelayed(HANDLER_SCREEN_SENSOR, DEFAULT_DELAY_TIME_SET_SENSOR);
                final ViewGroup.LayoutParams lp = mLandVideoRootView.getLayoutParams();
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoView.post(new Runnable() {
                    @Override
                    public void run() {
                        mLandVideoRootView.setLayoutParams(lp);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        mHandler.removeMessages(HANDLER_HIDE_NAVIGATION);
        mHandler.sendEmptyMessage(HANDLER_HIDE_NAVIGATION);
    }

    /**
     * 切换成竖屏模式
     *
     * @param bySensor 是否是通过sensor来切换的
     */
    public void changePortrait(boolean bySensor) {
        Activity activity = (Activity) getContext();
//        ViewGroup root;
//        if (mLandVideoRootView != null) {
//            root = mLandVideoRootView;
//        } else {
//            root = (ViewGroup) activity.findViewById(android.R.id.content);
//        }
//        root.removeView(mVideoView);
        mFullScreenView.setImageResource(R.mipmap.ic_to_fullscreen);
        mDanmuImage.setVisibility(GONE);
        if(mLandVideoRootView != null) {
            try {
                ScreenOrientationUtils.setPortrait(activity, !bySensor);
                ScreenOrientationUtils.setStatusBarVisible(activity, false);
                mHandler.removeMessages(HANDLER_SCREEN_SENSOR);
                mHandler.sendEmptyMessageDelayed(HANDLER_SCREEN_SENSOR, DEFAULT_DELAY_TIME_SET_SENSOR);
                final ViewGroup.LayoutParams lp = mLandVideoRootView.getLayoutParams();
                lp.height = getResources().getDimensionPixelOffset(R.dimen.common_video_height);
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoView.post(new Runnable() {
                    @Override
                    public void run() {
                        mLandVideoRootView.setLayoutParams(lp);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        showSystemBar();
    }

    /**
     * 同步播放状态
     */
    private void syncPlayStatus() {
        if (mPlayer.isPlaying()) {
            mStartOrPauseView.setImageResource(R.drawable.ic_play_pause);
        } else {
            mStartOrPauseView.setImageResource(R.drawable.ic_play_play);
        }
    }

    /**
     * 设置进度
     */
    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mNormalFeaturesContent == null) {
            return position;
        }

        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            mProgress.setProgress((int) pos);
        }
        int percent = mPlayer.getBufferPercentage();
        mProgress.setSecondaryProgress(percent * 10);

        mEndTime.setText(VideoUtils.generatePlayTime(duration));
        mCurrentTime.setText(VideoUtils.generatePlayTime(position));
        return position;
    }

    /**
     * 设置进度
     */
    public void setProgress(int position) {
        if (position < 0) {
            return;
        }
        mPlayer.seekTo(position);
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(HANDLER_UPDATE_PROGRESS);
            mVideoView.removeHideAction();
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {

            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;

            if (mCurrentTime != null) {
                mCurrentTime.setText(VideoUtils.generatePlayTime(newposition));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;

            long duration = mPlayer.getDuration();
            long newposition = (duration * bar.getProgress()) / 1000L;
            mPlayer.seekTo((int) newposition);

            mVideoView.setShowAction();
        }
    };


    private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
        public void onCompletion(IMediaPlayer mp) {
            if (mCallBack != null) {
                mCallBack.onComplete();
            }
        }
    };


    private void hideSystemBar() {
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHandler.postDelayed(mHideSystemBarRunnable, 300);
    }

    private final Runnable mHideSystemBarRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void showSystemBar() {
        // Show the system bar
        //setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        // Schedule a runnable to display UI elements after a delay
        mHandler.removeCallbacks(mHideSystemBarRunnable);
    }


    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }


    public interface CallBack {
        void onPlay(boolean isPlaying);

        void onComplete();

        void onPlayNext();
    }


    private void registerContentObservers() {
        // 通过调用getUriFor
        Uri airplaneUri = Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION);
        // 注册内容观察者
        // 第二个参数false 为精确匹配
        getContext().getContentResolver().registerContentObserver(airplaneUri, false, mObserver);
    }


    private void unregisterContentObservers() {
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: mVideoView = " + mVideoView);
        if (mVideoView != null) {
            registerContentObservers();
            mHandler.sendEmptyMessage(HANDLER_SCREEN_SENSOR);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
        unregisterContentObservers();
    }


    private ContentObserver mObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.sendEmptyMessage(HANDLER_SCREEN_SENSOR);
        }
    };

    private boolean isCanAutoRotation() {
        int acc = Settings.System.getInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        return acc == 1;
    }
}
