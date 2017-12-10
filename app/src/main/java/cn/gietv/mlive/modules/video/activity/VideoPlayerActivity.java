package cn.gietv.mlive.modules.video.activity;

/**
 * author：steven
 * datetime：15/10/8 16:42
 */
public class VideoPlayerActivity {/*extends AbsBaseActivity implements IScreenLightListener{
    public static final String EXTRA_PATH = "extra_path";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_ROOM_ID = "extra_room_id";
    public static final String EXTRA_MODEL = "extra_model";
    private String path;

    private long mStartTime;


    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    private FrameLayout mMainLayout;
//    private ProgressWheel mWheel;
//    private ProgressHelper mProgressHelper;
    private LiveMqttMessageFragment fragment;
    private ProgramBean.ProgramEntity mProgram;
    *//**
     * 最大声音
     *//*
    private int mMaxVolume;
    *//**
     * 当前声音
     *//*
    private int mVolume = -1;
    *//**
     * 当前亮度
     *//*
    private float mBrightness = -1f;
    *//**
     * 当前缩放模式
     *//*
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;
    private TextView mPosition;
    private String mRoomId;
    private int model;
 *//*   private MqttService mMqttService;
    private MqttController mController;*//*
    private MultiUserChat mChatRoom;
    private VideoSettingPopuWindow mSettingPopuWindow;
    private FrameLayout mMainView;
    private int screenWidth;
    public static void openVideoPlayerActivity(Activity activity, String path, long position) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PATH, path);
        bundle.putLong(EXTRA_POSITION, position);
        IntentUtils.openActivityForResult(activity, VideoPlayerActivity.class, Activity.RESULT_FIRST_USER, bundle);
    }
    public static void openVideoPlayerActivity(Activity activity, String path, long position,int model) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PATH, path);
        bundle.putLong(EXTRA_POSITION, position);
        bundle.putInt(EXTRA_MODEL,model);
        IntentUtils.openActivityForResult(activity, VideoPlayerActivity.class, Activity.RESULT_FIRST_USER, bundle);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //全屏显示
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.video_layout);
        model = getIntent().getExtras().getInt(EXTRA_MODEL);
        path = getIntent().getExtras().getString(EXTRA_PATH);
        mRoomId = getIntent().getExtras().getString(EXTRA_ROOM_ID);
        mProgram = (ProgramBean.ProgramEntity) getIntent().getExtras().getSerializable("extra_program");
        String title = getIntent().getExtras().getString(EXTRA_TITLE);
        if (StringUtils.isEmpty(path)) {
            ToastUtils.showToast(this, "视频路径不存在");
            finish();
            return;
        }
        mMainView = (FrameLayout) findViewById(R.id.video_fl_main);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mMainLayout = (FrameLayout) findViewById(R.id.video_fl_main);
        mPosition = (TextView) findViewById(R.id.operation_position);
//        mWheel = (ProgressWheel) findViewById(R.id.imageViewLoading);
//
//        mProgressHelper = new ProgressHelper(this);
//        mProgressHelper.setBarColor(Color.parseColor("#999999"));
//        mProgressHelper.setProgressWheel(mWheel);
////        mProgressHelper.spin();
//
//        mWheel.setVisibility(View.VISIBLE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVideoView.setVideoPath(path);
//        mMediaController = new MediaController(this);
        mMediaController = new MediaController(this, true, mMainLayout);
        mMediaController.setModel(MediaController.VIDEO_MODEL_VIDEO_BIG);
        mMediaController.setTitle(title);
        mMediaController.setExitClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("position", mVideoView.getCurrentPosition());
//                setResult(RESULT_OK, intent);
//                mVideoView.stopPlayback();
               VideoPlayerActivity.this.onBackPressed();
            }
        });
        mMediaController.getRoot().setVisibility(View.GONE);
        mVideoView.setMediaController(mMediaController);

        mVideoView.requestFocus();
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
                mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FULL, 0f);
                mVideoView.seekTo(getIntent().getExtras().getLong(EXTRA_POSITION));
//                mWheel.setVisibility(View.GONE);
                mVideoView.start();
            }
        });

        mMediaController.setVideoSettingClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSettingPopuWindow == null){
                    mSettingPopuWindow = new VideoSettingPopuWindow(VideoPlayerActivity.this);
                    mSettingPopuWindow.setWidth(screenWidth / 2);
                    mSettingPopuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                    mSettingPopuWindow.setFocusable(true);
                    mSettingPopuWindow.setOutsideTouchable(true);
                    mSettingPopuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mSettingPopuWindow.setiScreenLightListener(VideoPlayerActivity.this);
                    mSettingPopuWindow.mDanmuSwitch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment.controlDanmu();;
                        }
                    });
            }
                mSettingPopuWindow.showAtLocation(mMainView, Gravity.NO_GRAVITY, screenWidth / 2, 0);
            }
        });
        fragment = new LiveMqttMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "video");
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.video_play_fragment, fragment);
        ft.commit();
        if(mRoomId != null) {
            mHandler.post(mRunnable);
            getMessage();
        }
        WindowManager wm = this.getWindowManager();
        screenWidth = wm.getDefaultDisplay().getWidth();
    }
    private long temp;
    Handler mHandler=new Handler();
    Runnable mRunnable= new Runnable() {
        public void run() {
            long currentPosition = mVideoView.getCurrentPosition()/1000;
            if(temp != currentPosition){
                if (mMessageMap != null && mMessageMap.containsKey(String.valueOf(currentPosition))) {
                    String[] messages = mMessageMap.get(String.valueOf(currentPosition)).split("#$%123");
                    for (int i = 0; i < messages.length; i++) {
                        fragment.addDanmaku(messages[i]);
                    }
                }
                temp = currentPosition;
            }
            mHandler.postDelayed(mRunnable,800);
        }
    };
    private MessageModel mMessageModel;
    private Map<String,String> mMessageMap ;
    private void getMessage() {
        mMessageModel = RetrofitUtils.create(MessageModel.class);
        mMessageModel.getMessageByProId(mRoomId, 99999, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<MessageBean>() {
            @Override
            public void success(MessageBean messageBean) {
                if (isNotFinish() && messageBean.messages != null) {
                    mMessageMap = new HashMap<>();
                    for (int i = 0; i < messageBean.messages.size(); i++) {
                        putValue(messageBean.messages.get(i));
                    }
                }
            }

            @Override
            public void failure(String message) {
                //showError(message);
            }
        });
    }
    private void putValue(MessageBean.MessagesEntity message){
        if(mMessageMap.containsKey(message.videotimestamp)){
            String messages = mMessageMap.get(message.videotimestamp);
            mMessageMap.put(String.valueOf(message.videotimestamp),messages+"#$%123"+message.message);
        }else{
            mMessageMap.put(String.valueOf(message.videotimestamp),message.message);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(model == 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(VideoPlayListActivity.EXTRA_PROGRAM, mProgram);
            IntentUtils.openActivity(VideoPlayerActivity.this, VideoPlayListActivity.class, bundle);
            finish();
        }else if (model == 1){
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        mChatRoom.removeMessageListener(myPacketListener);
    }

    boolean mIsFirstLoad = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsFirstLoad) {
            try {
                mVideoView.resume();
            } catch (Exception e) {

            }
            mIsFirstLoad = false;
        }
        mStartTime = SystemClock.currentThreadTimeMillis();
        final String id ;
        if(UserUtils.isNotLogin()){
            id = VersionUtils.getDeviceId(this);
        }else{
            id = CacheUtils.getCacheUserInfo()._id;
        }
        if(XmppConnection.getInstance().isConnection()){
            mChatRoom = XmppConnection.getInstance().joinMultiUserChat(id, ConfigUtils.TOPIC_LUBO+mRoomId,"");
            myPacketListener = new MyPacketListener();
            mChatRoom.addMessageListener(myPacketListener);
        }else{
            new Thread(){
                @Override
                public void run() {
                    mChatRoom = XmppConnection.getInstance().joinMultiUserChat(id, ConfigUtils.TOPIC_LUBO+mRoomId,"");
                    myPacketListener = new MyPacketListener();
                    mChatRoom.addMessageListener(myPacketListener);
                }
            }.start();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return false;
    }

    *//**
     * 手势结束
     *//*
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        if (mPosition.getVisibility() == View.VISIBLE) {
            mVideoView.seekTo(mCurrentPosition);

            mMediaController.setProgress();
            mCurrentPosition = 0;
        }
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    long mCurrentPosition = 0;

    @Override
    public void getPosition(int progress) {
        LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = progress/100f;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        *//**
         * 双击
         *//*
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        *//**
         * 滑动
         *//*
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            int x = (int) e2.getRawX();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth * 4.0 / 5 && mOldX > windowWidth / 5.0) {
                long miSecond = (long) ((x - mOldX) / 10 * 1000f);
                if (x > mOldX) {
                    onGo(miSecond);
                } else {
                    onBack(miSecond);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private void onGo(long misecond) {
        long currentPosition = mVideoView.getCurrentPosition();
        currentPosition += misecond;
        mPosition.setVisibility(View.VISIBLE);
        mPosition.setText("+" + misecond / 1000 + " 秒");
        mCurrentPosition = currentPosition;
    }

    private void onBack(long misecond) {
        long currentPosition = mVideoView.getCurrentPosition();
        currentPosition += misecond;
        System.out.println("misecond          ===  " + misecond);
        System.out.println("currentPosition   ===  " + currentPosition);
        mPosition.setVisibility(View.VISIBLE);
        mPosition.setText(misecond / 1000 + " 秒");
        mCurrentPosition = currentPosition;
    }


    *//**
     * 定时隐藏
     *//*
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
            mPosition.setVisibility(View.GONE);
        }
    };

    *//**
     * 滑动改变声音大小
     *
     * @param percent
     *//*
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.mipmap.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    *//**
     * 滑动改变亮度
     *
     * @param percent
     *//*
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.mipmap.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
            LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }
    private MyPacketListener myPacketListener;
    class MyPacketListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {
            String message = ((org.jivesoftware.smack.packet.Message)packet).getBody();
            MessageBean.MessagesEntity messagesEntity = GsonUtils.getGson().fromJson(message, MessageBean.MessagesEntity.class);
            putValue(messagesEntity);
        }
    }*/
}
