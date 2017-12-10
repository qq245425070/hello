package cn.gietv.mlive.modules.video.activity;

/**
 * author：steven
 * datetime：15/10/11 20:30
 */
public class LivePlayActivity {//extends AbsBaseActivity {
 /*   public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_PATH = "extra_path";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_USER_ID = "extra_user_id";
    private static final int TYPE_LOOK_10 = 0;
    private static final int TYPE_LOOK_20 = 1;
    private static final int TYPE_LOOK_40 = 2;
    private static final int TYPE_LOOK_90 = 3;
    private static final int TASK_OVER = 4;
    private static final int TYPE_OTHER = 5;
    private static final int CHANGE_BUTTON_BG_WHAT = 88;
    private static final int TOTAL_TIME_10 = 600;
    private static final int TOTAL_TIME_20 = 1200;
    private static final int TOTAL_TIME_40 = 2400;
    private static final int TOTAL_TIME_90 = 5400;

    private String mHost = UrlConstants.Mqtt.URL_MQTT_HOST;
    private String path;
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    private View mMainLayout;
    private GiftModel mGiftModel;
    private GiftFullPopWindow giftFullPopWindow;
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
//    private MqttController mMqttController;
    private MessageModel mMessageModel;
    private ImageButton mReceiveButton;
    private String mProId;
    private String mTitle;
    private LiveMqttMessageFragment fragment;
    private UserCenterBean.UserinfoEntity mUserinfoEntity;
    private TextView mPosition;
    private String mUserid;
    public long mStartTime;
    private VideoModel mVideoModel;
    private boolean flag = true;
    private long mTime;
    private int mType = TYPE_OTHER;
    private int mCurrentType;
    private StatisticsMode mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
    private long mCurrentTotalTime;
    private ProgramBean.ProgramEntity programEntity;

    public static void openLivePlayActivity(Activity activity, String path, String title, String proId,String userid,ProgramBean.ProgramEntity programEntity) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PATH, path);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_ID, proId);
        bundle.putString(EXTRA_USER_ID,userid);
        bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, programEntity);
        IntentUtils.openActivity(activity, LivePlayActivity.class, bundle);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_layout);
        mVideoModel = RetrofitUtils.create(VideoModel.class);
        path = getIntent().getExtras().getString(EXTRA_PATH);
        mTitle = getIntent().getExtras().getString(EXTRA_TITLE);
        mProId = getIntent().getExtras().getString(EXTRA_ID);
        mUserid = getIntent().getExtras().getString(EXTRA_USER_ID);
        programEntity = (ProgramBean.ProgramEntity) getIntent().getExtras().getSerializable(LivePlayListActivity.EXTRA_PROGRAM);
        mUserinfoEntity = CacheUtils.getCacheUserInfo();
        if (StringUtils.isEmpty(path)) {
            ToastUtils.showToast(this, "视频路径不存在");
            finish();
            return;
        }
        Vitamio.isInitialized(this);
        mMainLayout = (FrameLayout) findViewById(R.id.video_fl_main);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mPosition = (TextView) findViewById(R.id.operation_position);
//        mWheel = (ProgressWheel) findViewById(R.id.imageViewLoading);
//        mProgressHelper = new ProgressHelper(this);
//        mProgressHelper.setBarColor(Color.parseColor("#999999"));
//        mProgressHelper.setProgressWheel(mWheel);
//        mProgressHelper.spin();
//        mWheel.setVisibility(View.VISIBLE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVideoView.setVideoPath(path);
        mMediaController = new MediaController(this, true, mMainLayout);
        mMediaController.setModel(MediaController.VIDEO_MODEL_LIVE_BIG);
        mMediaController.setResolutionClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mMediaController.setDanmuClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.controlDanmu();
            }
        });
        //刷新按钮的监听事件
        mMediaController.setRefreshClick(new MediaControllerViewBuilder.MediaControllerRefreshListener() {
            @Override
            public void onClickRefresh() {
                if (TextUtils.isEmpty(path)) {
                    ToastUtils.showToast(LivePlayActivity.this, "视频路径不存在");
                    finish();
                    return;
                }
                mVideoView.setVideoPath(path);
            }
        });
        mMediaController.setSendClick(new MediaControllerViewBuilder.MediaControllerSendMessageListener() {
            @Override
            public void sendMessage(String msg) {
                if (UserUtils.isNotLogin()) {
                    IntentUtils.openActivity(LivePlayActivity.this, LoginActivity.class);
                    return;
                }
                try {
                    MqttMessageBeanOld bean = new MqttMessageBeanOld();
                    bean.uid = mUserinfoEntity._id;
                    bean.nickname = mUserinfoEntity.nickname;
                    bean.avatar = mUserinfoEntity.avatar;
                    bean.news = msg;
                    ArrayList<BlackListBean.BlackName> blackList = CacheUtils.getCacheBlackList(mProId);
                    if (blackList != null) {
                        if (blackList != null && blackList.size() > 0) {
                            for (BlackListBean.BlackName blackName : blackList) {
                                if (blackName.uid.equals(CacheUtils.getCacheUserInfo()._id) && System.currentTimeMillis() < blackName.gagtime) {
                                    ToastUtils.showToast(LivePlayActivity.this, "您已经被加入黑名单，" + TimeUtil.getTimeString(blackName.gagtime) + "之后再发言");
                                    return;
                                }
                            }
                        }
                    }
                    // mMqttController.sendMessage(ConfigUtils.TOPIC_CHAT + mProId, gson.toJson(bean));
                    LivePlayActivity.this.sendMessage(ConfigUtils.USER_CHAT, msg, "", "");
                    if (NoviceTaskUtils.getData(ConfigUtils.TASK_DAY_SEND_MSG, LivePlayActivity.this)) {
                        long count = NoviceTaskUtils.getDbData(ConfigUtils.TASK_DAY_SEND_MSG, LivePlayActivity.this);
                        NoviceTaskUtils.saveCount(ConfigUtils.TASK_DAY_SEND_MSG, LivePlayActivity.this, ++count);
                        if (count == 5) {
                            NoviceTaskUtils.updateTask(ConfigUtils.TASK_DAY_SEND_MSG, 30);
                        }
                    } else {
                        NoviceTaskUtils.saveCount(ConfigUtils.TASK_DAY_SEND_MSG, LivePlayActivity.this, 1);
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void sendGift() {
                if (UserUtils.isNotLogin()) {
                    ToastUtils.showToast(LivePlayActivity.this, "请先登录");
                    return;
                }
                if (mUserid != null && !mUserid.equals(mUserinfoEntity._id)) {
                    if (CacheUtils.getPropList() == null) {
                        queryProps();
                    } else {
                        showPopupWindow(CacheUtils.getPropList());
                    }
                } else {
                    ToastUtils.showToast(LivePlayActivity.this, "自己不能给自己送游戏币");
                }
            }
        });
        mMediaController.setExitClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LivePlayActivity.this.onBackPressed();
            }
        });
       // mMediaController.getRoot().setVisibility(View.GONE);
        mMediaController.setShowEnable(true);
        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();
        mVideoView.start();
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
//        mVideoView.seekTo(getIntent().getExtras().getLong(EXTRA_POSITION));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
                mediaPlayer.setPlaybackSpeed(1.0f);
                mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FULL, 0f);
                mDismissHandler.sendEmptyMessageDelayed(0, 3000);
                setListener();
                getDayLookData();
                mMediaController.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            mMediaController.setShowEnable(false);
                        } else {
                            mMediaController.setShowEnable(true);
                        }
                    }
                });
            }
        });
        mMainLayout.setFocusable(true);
        mMainLayout.setFocusableInTouchMode(true);
        mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputUtils.closeInputKeyBoard(mMediaController.getEditText());
                mVideoView.requestFocus();
            }
        });
        fragment = new LiveMqttMessageFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.video_play_fragment, fragment);
        ft.commit();
        getTopic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LivePlayListActivity.EXTRA_PROGRAM, programEntity);
        IntentUtils.openActivity(LivePlayActivity.this, LivePlayListActivity.class, bundle);
        finish();
    }

    private void setListener() {
        mMediaController.mReceiveButton.setVisibility(View.VISIBLE);
        mMediaController.mReceiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mType) {
                    case TYPE_LOOK_10:
                        NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOOK_TIME_10, LivePlayActivity.this);
                        mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_grey);
                        flag = true;
                        startThread();
                        mCurrentTotalTime = TOTAL_TIME_20;
                        mType = TYPE_OTHER;
                        mCurrentType = TYPE_LOOK_20;
                        mTime = 0;
                        break;
                    case TYPE_LOOK_20:
                        NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOOK_TIME_20, LivePlayActivity.this);
                        mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_grey);
                        flag = true;
                        startThread();
                        mCurrentTotalTime = TOTAL_TIME_40;
                        mType = TYPE_OTHER;
                        mCurrentType = TYPE_LOOK_40;
                        mTime = 0;
                        break;
                    case TYPE_LOOK_40:
                        NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOOK_TIME_40, LivePlayActivity.this);
                        mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_grey);
                        flag = true;
                        startThread();
                        mCurrentTotalTime = TOTAL_TIME_90;
                        mType = TYPE_OTHER;
                        mCurrentType = TYPE_LOOK_90;
                        mTime = 0;
                        break;
                    case TYPE_LOOK_90:
                        NoviceTaskUtils.doDayTask(ConfigUtils.TASK_DAY_LOOK_TIME_90, LivePlayActivity.this);
                        mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_grey);
                        flag = false;
                        startThread();
                        mType = TASK_OVER;
                        mCurrentType = TASK_OVER;
                        mTime = 0;
                        break;
                    case TASK_OVER:
                        if(isNotFinish())
                        ToastUtils.showToast(LivePlayActivity.this, "您今天的任务已经完成！！！");
                        break;
                    default:
                        if(UserUtils.isNotLogin()){
                            ToastUtils.showToast(LivePlayActivity.this,"请先登录");
                            return;
                        }
                        String sb = new StringBuffer().append("您需要累积观看").append(TimeUtil.getTimeString(1000 * (mCurrentTotalTime - mTime) + System.currentTimeMillis())).append("才能完成任务").toString();
                        ToastUtils.showToast(LivePlayActivity.this, sb);
                        break;
                }
            }
        });
    }

    private void getDayLookData() {
        if(UserUtils.isNotLogin()){
            flag = false;
            return;
        }
       long result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_LOOK_TIME_10, this);
        if(result == 0){
            dataProcessing(ConfigUtils.TASK_DAY_LOOK_TIME_10,TYPE_LOOK_10,TOTAL_TIME_10);
        }else if(result == 1){
            result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_LOOK_TIME_20,this);
            if(result == 0){
                dataProcessing(ConfigUtils.TASK_DAY_LOOK_TIME_20,TYPE_LOOK_20,TOTAL_TIME_20);
            }else if(result == 1){
                result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_LOOK_TIME_40,this);
                if(result == 0){
                    dataProcessing(ConfigUtils.TASK_DAY_LOOK_TIME_40,TYPE_LOOK_40,TOTAL_TIME_40);
                }else if(result == 1){
                    result = NoviceTaskUtils.getTaskResult(ConfigUtils.TASK_DAY_LOOK_TIME_90,this);
                    if(result == 0){
                        dataProcessing(ConfigUtils.TASK_DAY_LOOK_TIME_90,TYPE_LOOK_90,TOTAL_TIME_90);
                    }else if(result == 1){
                        //每日观看视频任务完成
                        mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_yellow);
                        mType = TASK_OVER;
                    }else{
                        mTime = 0;
                        flag = true;
                        mCurrentType = TYPE_LOOK_90;
                        mCurrentTotalTime = TOTAL_TIME_90;
                        startThread();
                    }
                }else{
                    mTime = 0;
                    flag = true;
                    mCurrentType = TYPE_LOOK_40;
                    mCurrentTotalTime = TOTAL_TIME_40;
                    startThread();
                }
            }else{
                mTime = 0;
                flag = true;
                mCurrentType = TYPE_LOOK_20;
                mCurrentTotalTime = TOTAL_TIME_20;
                startThread();
            }
        }else{
            mTime = 0;
            flag = true;
            mCurrentType = TYPE_LOOK_10;
            mCurrentTotalTime = TOTAL_TIME_10;
            startThread();
        }
    }

    private void dataProcessing(String TaskName,int type,long time){
        mCurrentTotalTime = time;
       long result = NoviceTaskUtils.getDbData(TaskName, this);
        if(result == time){
            //修改领取金币按钮的照片
            mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_yellow);
            mType = type;
            mCurrentType = type;
            flag = false;
        }else{
            mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_grey);
            mTime = result;
            flag = true;
            mCurrentType = type;
            startThread();
        }
    }
    private void queryProps(){
        GiftModel model = RetrofitUtils.create(GiftModel.class);
        model.queryProps(new DefaultLiveHttpCallBack<PropBeanList>() {
            @Override
            public void success(PropBeanList propBeanList) {
                CacheUtils.savePropList(propBeanList);//保存
                if (UserUtils.isNotLogin()) {
                    ToastUtils.showToast(LivePlayActivity.this, "请先登录");
                    return;
                }
                showPopupWindow(propBeanList);
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void showPopupWindow(PropBeanList propBeanList) {
        giftFullPopWindow = new GiftFullPopWindow(this,propBeanList,this,mUserid,mProId);
        giftFullPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        giftFullPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        giftFullPopWindow.setFocusable(true);
        giftFullPopWindow.setOutsideTouchable(true);
        giftFullPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
        giftFullPopWindow.showAtLocation(mMainLayout, Gravity.NO_GRAVITY, 0, screenHeight - giftFullPopWindow.getHeight() );
    }
    public void sendMessage(String type , String messageText, String behavior,String propimg) {
        MqttMessageBean bean = new MqttMessageBean();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        bean.time = date;
        bean.topic = ConfigUtils.TOPIC_SEND_CION + mProId;
        bean.clientid = VersionUtils.getDeviceId(MainApplication.getInstance());
        MqttMessageBean.ChatRoom data = new MqttMessageBean.ChatRoom();
        data.avatar = mUserinfoEntity.avatar;
        data.news = messageText;
        data.nickname = mUserinfoEntity.nickname;
        data.uid = mUserinfoEntity._id;
        data.date = date;
        bean.type = type;
        if(CacheUtils.getMyContributionJinjiao(mProId) != null) {
            data.jinjiaoImg = CacheUtils.getMyContributionJinjiao(mProId).levelimg;
            data.jinjiaoLevel = CacheUtils.getMyContributionJinjiao(mProId).level;
        }
        if(CacheUtils.getMyContributionCoin(mProId) != null) {
            data.acoinImg = CacheUtils.getMyContributionCoin(mProId).levelimg;
            data.acoinLevel = CacheUtils.getMyContributionCoin(mProId).level;
        }
        if(ConfigUtils.CHAT_ROOM_CONCERN.equals(type)){
            data.behavior = behavior;
        }else if (ConfigUtils.CHAT_ROOM_IN_OUT.equals(type)){
            data.behavior = behavior;
        }else if(ConfigUtils.CHAT_ROOM_SEND_GIFT.equals(type)){
            data.prop = behavior;
            data.propimg = propimg;
        }else if(ConfigUtils.CHAT_ROOM_LIVE.equals(type)){
            data.behavior = behavior;
        }
        bean.data = data;
        Gson gson = GsonUtils.getGson();
        try {
            if(ConfigUtils.USER_CHAT.equals(type)){
                //mMqttController.sendMessage(ConfigUtils.TOPIC_CHAT + mProId, gson.toJson(bean));
                mChatRoom.sendMessage(gson.toJson(bean));
            }else {
               // mMqttController.sendMessage(ConfigUtils.TOPIC_SEND_CION + mProId, gson.toJson(bean));
                mSendCoinRoom.sendMessage(gson.toJson(bean));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(this, e.getMessage());
        }
    }
    private MultiUserChat mChatRoom;
    private MultiUserChat mSendCoinRoom;
    private MultiUserChat mEnterRoom;
    private MultiUserChat mLeftRoom;
    private MultiUserChat mBlackListRoom;
    private void subscribeTopic() {
        String id ;
        if(mUserinfoEntity == null){
            id = VersionUtils.getDeviceId(this);
        }else{
            id = mUserinfoEntity._id;
        }
        mChatRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_CHAT + mProId,"");
        mChatListener = new ChatPacketListener();
        mChatRoom.addMessageListener(mChatListener);

        mSendCoinRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_SEND_CION + mProId,"");
        mSendGifListener = new SendCoinPacketListener();
        mSendCoinRoom.addMessageListener(mSendGifListener);

        mEnterRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_ENTER_ROOM + mProId,"");
        mLeftRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_LEFT_ROOM + mProId,"");

        mBlackListRoom = XmppConnection.getInstance().joinMultiUserChat(id,ConfigUtils.TOPIC_BLACK_LIST + mProId,"");
        mBlackListListener = new BlackListPacketListener();
        mBlackListRoom.addMessageListener(mBlackListListener);
        sendInRoomMessage();
       *//* MqttService mqttService = (MqttService) MainApplication.getInstance().getObjcet();
        if(mqttService != null) {
            mqttService.setParseJsonListener(this);
            mMqttController = mqttService.getMqttController();
            if(mMqttController != null) {
                try {
                    if(mMqttController.getMqttClient() != null) {
                       // mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_CHAT_HISTORY + mProId,0);//订阅聊天历史的topic
//                        mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_CHAT + mProId, 0);//聊天
                        mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_BLACK_LIST + mProId, 0);//黑名单
//                        mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_SEND_CION + mProId , 0);//赠送金币
//                        mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_SEND_RMB + mProId , 0);//赠送人民币
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }*//*
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        if(giftFullPopWindow != null){
            if(giftFullPopWindow.isShowing()) {
                giftFullPopWindow.dismiss();
            }
        }
       *//* try {
            if(mMqttController != null) {
                mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_BLACK_LIST + mProId);
                mMqttController.destory();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }*//*
        //退出房间，取消mqtt订阅
        *//*try {
            //取消订阅的Topic
            *//**//*if(mMqttController != null) {
                mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_BLACK_LIST + mProId);
                //mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_CHAT_HISTORY + mProId);
                mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_CHAT + mProId);
                mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_SEND_CION + mProId);
                mMqttController.getMqttClient().unsubscribe(ConfigUtils.TOPIC_SEND_RMB + mProId);
            }*//**//*
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendOutRoom();
                mChatRoom.removeMessageListener(mChatListener);
                mSendCoinRoom.removeMessageListener(mSendGifListener);
                mBlackListRoom.removeMessageListener(mBlackListListener);
                mChatRoom.leave();
                mSendCoinRoom.leave();
                mBlackListRoom.leave();
            }
        }).start();
        flag = false;
        //向数据库中保存观看时间 //计算观看时间，保存到数据库，完成每日观看任务
       *//* if (!NoviceTaskUtils.getData(ConfigUtils.DAY_LOOK,LivePlayActivity.this)){
            long timeCount = (SystemClock.currentThreadTimeMillis() - mStartTime) / 1000;
            long count =  NoviceTaskUtils.getDbData(ConfigUtils.DAY_LOOK,LivePlayActivity.this);
            long zong = count + timeCount;
            NoviceTaskUtils.saveCount(ConfigUtils.DAY_LOOK,LivePlayActivity.this,zong);
            if(zong > 3600){
                NoviceTaskUtils.doDayTask(ConfigUtils.DAY_LOOK,LivePlayActivity.this);
            }
        }*//*

    }

    private void saveData() {
        switch (mCurrentType){
            case TYPE_LOOK_90:
                NoviceTaskUtils.saveTask(ConfigUtils.TASK_DAY_LOOK_TIME_90, mTime, 0, this);
                break;
            case TYPE_LOOK_40:
                NoviceTaskUtils.saveTask(ConfigUtils.TASK_DAY_LOOK_TIME_40, mTime, 0, this);
                break;
            case TYPE_LOOK_20:
                NoviceTaskUtils.saveTask(ConfigUtils.TASK_DAY_LOOK_TIME_20, mTime, 0, this);
                break;
            case TYPE_LOOK_10:
                NoviceTaskUtils.saveTask(ConfigUtils.TASK_DAY_LOOK_TIME_10, mTime, 0, this);
                break;
        }
    }

    private void sendOutRoom() {
        //向服务器发送观看视频时间
        Map<String ,String > map = new HashMap<>();
        map.put("房间ID",mProId);
        map.put("主播ID",mUserid);
        MobclickAgent.onEventValue(this, ConfigUtils.UMENG_LIVE_TIME, map, (int) ((System.currentTimeMillis() - mStartTime) / 1000));
        mStatisticsMode.sendProgramTime(mProId, System.currentTimeMillis() - mStartTime, ConfigUtils.FULL_SCREEN, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
        if(UserUtils.isNotLogin()){
            return;
        }
        saveData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        MqttMessageBean bean = new MqttMessageBean();
        MqttMessageBean.ChatRoom data = new MqttMessageBean.ChatRoom();
        bean.type = ConfigUtils.CHAT_ROOM_IN_OUT;
        bean.clientid = VersionUtils.getDeviceId(MainApplication.getInstance());
        bean.time = date;
        bean.topic = ConfigUtils.TOPIC_LEFT_ROOM + mProId ;
        data.uid = mUserinfoEntity._id;
        data.nickname = mUserinfoEntity.nickname;
        data.avatar = mUserinfoEntity.avatar;
        data.news = mUserinfoEntity.nickname + "退出直播间";
        data.date = date;
        bean.data = data;
        data.behavior = "n";
        Gson gson = GsonUtils.getGson();
        try {
           // mMqttController.sendMessage(ConfigUtils.TOPIC_LEFT_ROOM + mProId , gson.toJson(bean));
            mLeftRoom.sendMessage(gson.toJson(bean));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 退出聊天通知服务器
        if(!UserUtils.isNotLogin()){
            mVideoModel.chatLogin(mProId, 2, new DefaultHttpCallback<String>() {
                @Override
                public void success(HttpResponse<String> stringHttpResponse, Response response) {
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            mVideoView.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mStartTime = System.currentTimeMillis();
        if(XmppConnection.getInstance().isConnection()){
            subscribeTopic();
        }else{
            new Thread(){
                @Override
                public void run() {
                    subscribeTopic();
                }
            }.start();
        }
        *//*mMqttController = new MqttController(mHost,VersionUtils.getDeviceId(this),"",new MyMqttHandler());
        mMqttController.connect();*//*
        //getDayLookData();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == CHANGE_BUTTON_BG_WHAT){
                mMediaController.mReceiveButton.setBackgroundResource(R.mipmap.acoin_yellow);
            }
        }
    };
    private void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    if(++mTime == mCurrentTotalTime){
                        flag = false;
                       if(mCurrentTotalTime == mTime && mCurrentTotalTime == TOTAL_TIME_10){
                           mType = TYPE_LOOK_10;
                       }else if(mCurrentTotalTime == mTime  && mCurrentTotalTime == TOTAL_TIME_20){
                           mType = TYPE_LOOK_20;
                       }else if(mCurrentTotalTime == mTime  && mCurrentTotalTime == TOTAL_TIME_40){
                           mType = TYPE_LOOK_40;
                       }else if(mCurrentTotalTime == mTime  && mCurrentTotalTime == TOTAL_TIME_90){
                           mType = TYPE_LOOK_90;
                       }
                        handler.sendEmptyMessage(CHANGE_BUTTON_BG_WHAT);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }
    private void sendInRoomMessage() {
        //进入 聊天室通知服务器
        if(!UserUtils.isNotLogin()){
            if(mProId != null){
                mVideoModel.chatLogin(mProId, 1, new DefaultHttpCallback<String>() {
                    @Override
                    public void success(HttpResponse<String> stringHttpResponse, Response response) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = format.format(new Date());
                        MqttMessageBean bean = new MqttMessageBean();
                        MqttMessageBean.ChatRoom data = new MqttMessageBean.ChatRoom();
                        bean.type = ConfigUtils.CHAT_ROOM_IN_OUT;
                        bean.clientid = VersionUtils.getDeviceId(MainApplication.getInstance());
                        bean.time = date;
                        data.date = date;
                        bean.topic = ConfigUtils.TOPIC_ENTER_ROOM + mProId;
                        data.uid = mUserinfoEntity._id;
                        data.nickname = mUserinfoEntity.nickname;
                        data.avatar = mUserinfoEntity.avatar;
                        data.news = mUserinfoEntity.nickname + "进入直播间";
                        bean.data = data;
                        data.behavior = "y";
                        Gson gson = GsonUtils.getGson();
                        try {
                            //mMqttController.sendMessage(ConfigUtils.TOPIC_ENTER_ROOM + mProId, gson.toJson(bean));
                            mEnterRoom.sendMessage(gson.toJson(bean));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        *//*if(mMqttController != null)
            mMqttController.destory();*//*
        mVideoView.stopPlayback();
        flag = false;
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
        return super.onTouchEvent(event);
    }

    *//**
     * 手势结束
     *//*
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    public void messageArrived(String message) {
        MqttMessageBean e = GsonUtils.getGson().fromJson(message, MqttMessageBean.class);
        try {
            String msg = e.data.news;
            fragment.addDanmaku(msg);
        } catch (Exception e1) {
            e1.printStackTrace();
            MqttMessageBeanOld bean = GsonUtils.getGson().fromJson(message, MqttMessageBeanOld.class);
            String msg = bean.news;
            fragment.addDanmaku(msg);
        }
    }

    public void messageArrivedNew(String message) {
        MqttMessageBean bean = GsonUtils.getGson().fromJson(message, MqttMessageBean.class);
        if(ConfigUtils.CHAT_ROOM_SEND_GIFT.equals(bean.type)) {
            if(bean.data.prop.equals(getPropName(0))){
                fragment.addTopDanmaku(bean.data.nickname , bean.data.news , bean.data.prop,getPropName(0)+".png");
            }else if(bean.data.prop.equals(getPropName(1))){
                fragment.addTopDanmaku(bean.data.nickname , bean.data.news, bean.data.prop,getPropName(1)+".png");
            }else if(bean.data.prop.equals(getPropName(2))){
                fragment.addTopDanmaku(bean.data.nickname , bean.data.news , bean.data.prop,getPropName(2)+".png");
            }else if(bean.data.prop.equals(getPropName(3))){
                fragment.addTopDanmaku(bean.data.nickname , bean.data.news , bean.data.prop,getPropName(3)+".png");
            }else if(bean.data.prop.equals(getPropName(4))){
                fragment.addTopDanmaku(bean.data.nickname , bean.data.news , bean.data.prop,getPropName(4)+".png");
            }else if(bean.data.prop.equals(getPropName(5))){
                fragment.addTopDanmaku(bean.data.nickname , bean.data.news , bean.data.prop,getPropName(5)+".png");
            }
        }else {
            fragment.addDanmaku(bean.data.news);
        }

    }
    private String getPropName(int position){
        return CacheUtils.getPropList().props.get(position).name;
    }

    public void messageArrivedChatHistory(String message) {
        try {
            JSONArray jsonArray = new JSONArray(message);
            JSONObject jsonObject = null;
            MessageBean.MessagesEntity msgBean = null;
            for(int i = 0; i < jsonArray.length(); i++ ){
                jsonObject = jsonArray.getJSONObject(i);
                msgBean = new MessageBean.MessagesEntity();
                msgBean.uid = jsonObject.getJSONObject("data").getString("uid");
                msgBean.message = jsonObject.getJSONObject("data").getString("news");
                msgBean.avatar = jsonObject.getJSONObject("data").getString("avatar");
                msgBean.nickname = jsonObject.getJSONObject("data").getString("nickname");
                if(!message.contains("赠送给主播")) {
                    fragment.addDanmaku(msgBean.message);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void messageArrivedBlackList(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            int status = jsonObject.getInt("status");
            if(status == 0) {
                BlackListBean blackListBean = GsonUtils.getGson().fromJson(jsonObject.get("data").toString(), BlackListBean.class);
                CacheUtils.saveBlackList(mProId, blackListBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        *//**
         * 双击
         *//*
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
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
            System.out.println("moldX                   ===  " + mOldX);
            System.out.println("windowWidth * 4.0 / 5   ===  " + windowWidth * 4.0 / 5);
            System.out.println("windowWidth / 5.0       ===  " + windowWidth / 5.0);
            if (mOldX > windowWidth * 4.0 / 5) {
                // 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            } else if (mOldX < windowWidth / 5.0) {
                // 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private void onGo(long misecond) {
        long currentPosition = mVideoView.getCurrentPosition();
        currentPosition += misecond;
        mPosition.setVisibility(View.VISIBLE);
        mPosition.setText(misecond / 1000 + " 秒");
        mVideoView.seekTo(currentPosition);
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
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
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
    private void getTopic() {
        mMessageModel = RetrofitUtils.create(MessageModel.class);
        mMessageModel.getMqttTopic(mProId, new DefaultLiveHttpCallBack<BlackListBean>() {//获取Topic的接口，改为获取黑名单的接口
            @Override
            public void success(BlackListBean blackListBean) {
                CacheUtils.saveBlackList(mProId, blackListBean);
                CacheUtils.saveMyContributionCoin(blackListBean.mycontribution_coin, mProId);
                CacheUtils.saveMyContributionJinjiao(blackListBean.mycontribution_jinjiao, mProId);
            }

            @Override
            public void failure(String message) {
                ToastUtils.showToast(LivePlayActivity.this, message);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(giftFullPopWindow != null && giftFullPopWindow.dialog != null){
            if(giftFullPopWindow.isShowing()){
                giftFullPopWindow.dialog.dismiss();
            }
        }
        if(requestCode == 66 && resultCode == 88){
            if(giftFullPopWindow != null && giftFullPopWindow.dialog != null){
                if(giftFullPopWindow.dialog.isShowing()){
                    giftFullPopWindow.dialog.dismiss();
                }
            }
            final String propName = data.getExtras().getString("propname");
            final int count = data.getExtras().getInt("count");
            final String propid = data.getExtras().getString("propid");
            final String propimg = data.getExtras().getString("propimg");
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_gift_buzu,null);
            TextView cancelText = (TextView) dialogView.findViewById(R.id.cancel_buy);
            TextView confirmText = (TextView) dialogView.findViewById(R.id.confirm_buy);
            TextView titleText = (TextView) dialogView.findViewById(R.id.title);
            titleText.setText("充值完毕，确认赠送？");
            confirmText.setText("确认");
            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            confirmText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiveGiftUtil.giveGiftJinjao(propid, mUserid, LivePlayActivity.this, count / 100, propName, mProId,propimg);
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setContentView(dialogView);
        }
    }
    private ChatPacketListener mChatListener;
    private SendCoinPacketListener mSendGifListener;
    private BlackListPacketListener mBlackListListener;
    class ChatPacketListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) {
            String message = ((org.jivesoftware.smack.packet.Message)packet).getBody();
            messageArrived(message);
        }
    }

    class SendCoinPacketListener implements PacketListener{
        @Override
        public void processPacket(Packet packet) {
            String message = ((org.jivesoftware.smack.packet.Message)packet).getBody();
            System.out.println(message);
            messageArrivedNew(message);
        }
    }
    class BlackListPacketListener implements  PacketListener{
        @Override
        public void processPacket(Packet packet) {
            String message = ((org.jivesoftware.smack.packet.Message)packet).getBody();
            System.out.println(message);
            messageArrivedBlackList(message);
        }
    }
   *//* public class MyMqttHandler extends MqttHandler {
        @Override
        public void connectionLost(Throwable t) {

        }

        @Override
        public void connectSuccess() {
            try {
                if(mMqttController != null) {
                    mMqttController.getMqttClient().subscribe(ConfigUtils.TOPIC_BLACK_LIST + mProId, 0);//黑名单
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }

        @Override
        public void messageArrivedPay(String message) throws Exception {

        }

        @Override
        public void messageArrivedBlackList(String message) throws Exception {
            try {
                JSONObject jsonObject = new JSONObject(message);
                int status = jsonObject.getInt("status");
                if(status == 0) {
                    BlackListBean blackListBean = GsonUtils.getGson().fromJson(jsonObject.get("data").toString(), BlackListBean.class);
                    CacheUtils.saveBlackList(mProId,blackListBean);
                    System.out.println(blackListBean.blacklist);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void messageArrivedNotice(String message) throws Exception {

        }

        @Override
        public void messageArrivedSystem(String message) throws Exception {

        }
    }*/
}