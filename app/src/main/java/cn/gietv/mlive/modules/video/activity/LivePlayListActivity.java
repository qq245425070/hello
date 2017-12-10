package cn.gietv.mlive.modules.video.activity;

/**
 * author：steven
 * datetime：15/10/11 20:17
 */
public class LivePlayListActivity{//} extends AbsBaseActivity implements View.OnClickListener {
   /* public static final String EXTRA_PROGRAM = "extra_program";
    public static final String LOG = "LivePlayListActivity";
    public static final String ROOM_ID = "extra_room_id";
    private TextView  attentionText,  compereNameText, compereAttCount, compereAttText, titleText;//downloadText, gameNameText, gameDlCount,
    private ImageView  compereImage,shareText,mShoucangText,smal_iv2,mVideoCall,mRankingList;//gameImage,
    public ImageView mPrompt;
    private ListView mCommentList;
    private EditText mContentEdit;
    private ImageButton mSendButton;
    private LinearLayout mBottomLayout ,mLlliveRoom,mHeaderVideoItem,mTextParent,mGiftParent;
    private ImageView gift_button , attention_count;
    private GiftPopWindow giftPopWindow;
    public ProgramBean.ProgramEntity mProgram;
    private MessageModel mMessageModel;
    private FollowModel mFollowModel;
    private VideoModel mVideoModel;
    private StatisticsMode mStatisticsMode;
    private CustomFrameLayout mMessageLayout;
    private ImageLoader mImageLoader;
    private MessageAdapter adapter;
    public AnimationDrawable framAnimation;
    private VideoPlayFragment mVideoPlayFragment;
    private LiveMessageFragment mMessageFragment;
    //private View headerView;

    private long mStartTime ;
    private int mFragmentHeight;
    private int mTop;
    private int mWindowHeight;
    private RelativeLayout live_play_list_parent;
    private boolean flag = false;
    private InputMethodManager imm;
    private String mRoomId;
    public TextView prompt_text;
    private int temp = 0;
    private void initViews() {
        framAnimation = (AnimationDrawable) getResources().getDrawable(R.anim.prompt);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mProgram = (ProgramBean.ProgramEntity) getIntent().getExtras().getSerializable(EXTRA_PROGRAM);
        mRoomId = (getIntent().getExtras().getString(ROOM_ID) == null) ? mProgram._id:getIntent().getExtras().getString(ROOM_ID);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
        mVideoCall = (ImageView)  findViewById(R.id.video_tv_video_call);
        mRankingList = (ImageView)  findViewById(R.id.ranking_list);
        mPrompt = (ImageView)  findViewById(R.id.prompt_image_view);
        mRankingList.setVisibility(View.VISIBLE);
        mPrompt.setVisibility(View.VISIBLE);
        prompt_text = (TextView)  findViewById(R.id.prompt_text);
        prompt_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentHeight += prompt_text.getHeight();
                prompt_text.setVisibility(View.GONE);
            }
        });
        mPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prompt_text.setVisibility(View.VISIBLE);
                NoticeBean noticeBean = new NoticeBean();
                noticeBean.notice = (String) prompt_text.getText();
                noticeBean.date = TimeUtil.getTime(new Date().getTime());
                noticeBean.roomid = mProgram._id;
                CacheUtils.saveNotice(noticeBean,mProgram._id);
                mFragmentHeight -= DensityUtil.dip2px(LivePlayListActivity.this, 40);
                mPrompt.setBackgroundDrawable(getResources().getDrawable(R.mipmap.prompt));
                framAnimation.stop();
            }
        });
        mHeaderVideoItem = (LinearLayout)  findViewById(cn.gietv.mvr.R.id.head_live_message);
        mTextParent = (LinearLayout)  findViewById(cn.gietv.mvr.R.id.play_list_item_parent);
        mLlliveRoom = (LinearLayout)  findViewById(cn.gietv.mvr.R.id.ll_live_room);
        mLlliveRoom.setBackgroundColor(this.getResources().getColor(cn.gietv.mvr.R.color.livelist_background));
        attention_count = (ImageView)  findViewById(R.id.play_list_iv_attention_count);
        attention_count.setBackgroundResource(R.drawable.live_count);
         findViewById(cn.gietv.mvr.R.id.game_parent).setVisibility(View.GONE);
         findViewById(R.id.line).setVisibility(View.INVISIBLE);
         findViewById(R.id.line2).setVisibility(View.INVISIBLE);
        smal_iv2 = (ImageView)  findViewById(R.id.smal_iv2);
        smal_iv2.setBackgroundResource(R.drawable.live_smal_plus);
        compereNameText = (TextView)  findViewById(cn.gietv.mvr.R.id.video_tv_compere_name);
        compereAttCount = (TextView)  findViewById(cn.gietv.mvr.R.id.video_tv_compere_attention_count);
        compereAttText = (TextView)  findViewById(cn.gietv.mvr.R.id.video_tv_compere_attention);
        compereImage = (ImageView)  findViewById(cn.gietv.mvr.R.id.video_iv_compere_image);
        shareText = (ImageButton)  findViewById(cn.gietv.mvr.R.id.video_tv_share);


        attentionText = (TextView)  findViewById(cn.gietv.mvr.R.id.video_tv_attention);
        mShoucangText = (ImageButton)  findViewById(cn.gietv.mvr.R.id.video_tv_shoucang);
        titleText = (TextView)  findViewById(cn.gietv.mvr.R.id.video_tv_title);

       // gameImage.setOnClickListener(this);
        compereImage.setOnClickListener(this);

        live_play_list_parent = (RelativeLayout) findViewById(R.id.live_play_list_parent);
        mCommentList = (ListView) findViewById(cn.gietv.mvr.R.id.video_lv_comment);
        mContentEdit = (EditText) findViewById(cn.gietv.mvr.R.id.video_et_content);
        mSendButton = (ImageButton) findViewById(cn.gietv.mvr.R.id.video_btn_send);
        mBottomLayout = (LinearLayout) findViewById(cn.gietv.mvr.R.id.video_ll_bottom);
        mMessageLayout = (CustomFrameLayout) findViewById(R.id.video_list_fragment);

        gift_button = (ImageView) findViewById(cn.gietv.mvr.R.id.video_ivbt_gift);
        mContentEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
        mMessageLayout.setOnSizeChangedListener(new CustomFrameLayout.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                if(mFragmentHeight == oldh && oldh - h > DensityUtil.dip2px(LivePlayListActivity.this,80)){
                    softKeyShow();
                    flag = true;
                }else {
                    if(flag){
                        softKeyShow();
                    }else{
                        softKeyHidden();
                    }
                    flag = false;
                }
               *//* if(mFragmentHeight == oldh){
                    if(Math.abs(oldh - h) > oldh/2) {
                        flag = true;
                        softKeyShow();
                    }
                }else{
                    if(flag){
                        softKeyShow();
                    }else{
                        softKeyHidden();
                    }
                    flag = false;
                }*//*

            }
        });
        mMessageModel = RetrofitUtils.create(MessageModel.class);
//        mGameModel = RetrofitUtils.create(GameModel.class);
//        mCompereModel = RetrofitUtils.create(CompereModel.class);
        mVideoModel = RetrofitUtils.create(VideoModel.class);
//        mDownloadController = new DownloadController(this);
        mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
        mShoucangText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GameInfoActivity.openGameInfoActivity(LivePlayListActivity.this, mProgram.game._id, mProgram.game.name,mProgram.game.type);
                //handleShoucang();
            }
        });
        mStatisticsMode.playSta(mProgram._id, 1, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
        mRankingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(RankingListActivity.LIVE_ROOM_ID,mProgram._id);
                bundle.putString(RankingListActivity.LIVE_NICKNAME,mProgram.userinfo.nickname);
                IntentUtils.openActivity(LivePlayListActivity.this,RankingListActivity.class,bundle);
            }
        });
    }

    private void softKeyHidden() {// 软键盘隐藏式执行的方法
        if(mMessageFragment != null) {
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.head_live_message);
            params.addRule(RelativeLayout.ABOVE, R.id.video_ll_bottom);
            mHeaderVideoItem.setVisibility(View.VISIBLE);
            mMessageLayout.setBackgroundColor(Color.TRANSPARENT);
            mMessageLayout.post(new Runnable() {
                @Override
                public void run() {
                    mMessageLayout.setLayoutParams(params);
                    //mMessageFragment.showHeadView();
                }
            });
        }
    }

    private void softKeyShow() {//软键盘弹出是执行的方法
        if(mMessageFragment != null) {
            //mMessageFragment.hiddenHeadView();
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ABOVE, R.id.video_ll_bottom);
            mHeaderVideoItem.setVisibility(View.GONE);
            mMessageLayout.setBackgroundColor(Color.parseColor("#99343F44"));
            mMessageLayout.post(new Runnable() {
                @Override
                public void run() {
                    mMessageLayout.setLayoutParams(params);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStatisticsMode.playSta(mProgram._id, 2, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void initData() {
        attentionText.setText(NumUtils.w(mProgram.onlines));
//        mGameDownloadUrl = mProgram.game.url_android;
//        mGameFileName = mProgram.game.name + ".apk";
        titleText.setText((mProgram.name.length() > 10) ? mProgram.name.substring(0,10) + "..." : mProgram.name);
        mImageLoader.displayImage(mProgram.userinfo.avatar, compereImage);

        mSendButton.setOnClickListener(this);
       // downloadText.setOnClickListener(this);
        compereAttText.setOnClickListener(this);
        shareText.setOnClickListener(this);
        gift_button.setOnClickListener(this);
        mVideoCall.setOnClickListener(this);
        if (mProgram.userinfo.isfollow == CommConstants.TRUE) {
            //项数据库中保存关注主播数据
            if(!NoviceTaskUtils.getData(ConfigUtils.TASK_DAY_FOLLOW_LIVE,this)) {
                NoviceTaskUtils.editTaskResult(ConfigUtils.TASK_DAY_FOLLOW_LIVE, this, 0);
                NoviceTaskUtils.updateTask(ConfigUtils.TASK_DAY_FOLLOW_LIVE,10);
            }
            if(temp == 0){
                mLlliveRoom.setVisibility(View.GONE);
            }else {
                setAnimation();
            }
        } else {
            mTextParent.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            TextView text = new TextView(this);
            text.setTextColor(getResources().getColorStateList(cn.gietv.mvr.R.color.color_green_button_selected));
            text.setText("主播 - " + mProgram.userinfo.nickname);
            mTextParent.addView(text, params);
            compereAttText.setText("关注");
            compereAttText.setBackgroundResource(cn.gietv.mvr.R.drawable.common_button_white);
            compereAttText.setTextColor(getResources().getColorStateList(cn.gietv.mvr.R.color.color_green_button_selected));
            DBUtils.getInstance(this).saveAttentionAnchor(mProgram.userinfo._id,1);
        }
    }

    private void showError(String message) {
        if (isNotFinish()) {
            ToastUtils.showToast(LivePlayListActivity.this,message);
        }
    }

    private void getProgramEntity() {
        ProgramModel programModel = RetrofitUtils.create(ProgramModel.class);
        programModel.getProgramById(mRoomId, new DefaultLiveHttpCallBack<ProgramBean.ProgramEntity>() {
            @Override
            public void success(ProgramBean.ProgramEntity programEntity) {
                if (isNotFinish()) {
                    mProgram = programEntity;
                    initData();
                    getMqttTopic();
                    softKeyHidden();
                    if(CacheUtils.getNotice(programEntity._id) != null && programEntity.desc.equals(CacheUtils.getNotice(programEntity._id).notice)){
                        prompt_text.setText(programEntity.desc);
                        mPrompt.setBackgroundDrawable(getResources().getDrawable(R.mipmap.prompt));
                    }else{
                        mPrompt.setBackgroundDrawable(framAnimation);
                        prompt_text.setText(programEntity.desc);
                        framAnimation.start();
                        NoticeBean bean = new NoticeBean();
                        bean.roomid = programEntity._id;
                        bean.date = TimeUtil.getTime(new Date().getTime());
                        bean.notice = programEntity.desc;
                        CacheUtils.saveNotice(bean,bean.roomid);
                    }
                }
            }

            @Override
            public void failure(String message) {
                if (isNotFinish()) {
                    showError(message);
                }
            }
        });
    }

    private void handleShoucang() {
        if (UserUtils.isNotLogin()) {
            IntentUtils.openActivity(this, LoginActivity.class);
            return;
        }
        mFollowModel = RetrofitUtils.create(FollowModel.class);
        if (mProgram.isfollow == CommConstants.TRUE) {
           // final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "取消关注中，请稍后");
            mFollowModel.followByContent(mProgram._id, 1, 2, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    if (isNotFinish()) {
                        mProgram.isfollow = CommConstants.FALSE;
                        ToastUtils.showToast(LivePlayListActivity.this, "取消关注成功");
                       // dialog.dismiss();
                        initData();
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        ToastUtils.showToast(LivePlayListActivity.this, message);
                       // dialog.dismiss();
                    }
                }
            });
        } else {
           // final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "关注中，请稍后");
            mFollowModel.followByContent(mProgram._id, 1, 1, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    if (isNotFinish()) {
                        mProgram.isfollow = CommConstants.TRUE;
                        ToastUtils.showToast(LivePlayListActivity.this, "关注成功");
                       // dialog.dismiss();
                        initData();
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        ToastUtils.showToast(LivePlayListActivity.this, message);
                       // dialog.dismiss();
                    }
                }
            });
        }
    }

    private void setAnimation(){
        ScaleAnimation animator = new ScaleAnimation(1,1,1,0,Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animator.setDuration(200);
        mLlliveRoom.startAnimation(animator);
        animator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFragmentHeight += mLlliveRoom.getHeight();
                mLlliveRoom.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void getMqttTopic() {
        mMessageFragment = LiveMessageFragment.getInstence(UrlConstants.Mqtt.URL_MQTT_HOST,mProgram._id,mProgram.fanstitle);
        mMessageFragment.setMessageListener(new LiveMessageFragment.MessageListener() {
            @Override
            public void sendMessageSuccess() {
                //InputUtils.closeInputKeyBoard(mContentEdit);
                //softKeyHidden();
            }
        });
       // mMessageFragment.setHeaderView(headerView);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cn.gietv.mvr.R.id.video_list_fragment, mMessageFragment, "message");
        ft.commitAllowingStateLoss();
        getVideoPath();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(giftPopWindow != null){
            if(giftPopWindow.isShowing()) {
                giftPopWindow.dismiss();
            }
        }
        InputUtils.closeInputKeyBoard(mContentEdit);
        findViewById(cn.gietv.mvr.R.id.shade).setVisibility(View.VISIBLE);
        //向服务器发送观看视频时间
       sendOutRoom();
        if (mMessageFragment != null) {
            mMessageFragment.destory();
        }
       if(giftPopWindow != null && giftPopWindow.isShowing()){
            giftPopWindow.dismiss();
        }
        //向数据库中保存观看时间  //计算观看时间，保存到数据库，完成每日观看任务
       *//* if (!NoviceTaskUtils.getData(ConfigUtils.DAY_LOOK,this)){
            long timeCount = (SystemClock.currentThreadTimeMillis() - mStartTime) / 1000;
            long count =  NoviceTaskUtils.getDbData(ConfigUtils.DAY_LOOK,this);
            long total = count + timeCount;
            NoviceTaskUtils.saveCount(ConfigUtils.DAY_LOOK, this, total);
            if(total > 3600){
                NoviceTaskUtils.doDayTask(ConfigUtils.DAY_LOOK,this);
            }
        }*//*
    }

    private void sendOutRoom() {
        MobclickAgent.onPageEnd("直播页");
        Map<String ,String > map = new HashMap<>();
        map.put("直播昵称",mProgram.userinfo.nickname);
        map.put("主播ID",mProgram.userinfo._id);
        map.put("房间ID",mProgram._id);
        MobclickAgent.onEventValue(this, ConfigUtils.UMENG_LIVE_TIME, map, (int)((System.currentTimeMillis() - mStartTime) / 1000));
        mStatisticsMode.sendProgramTime(mProgram._id, System.currentTimeMillis() - mStartTime, ConfigUtils.SMALL_SCREEN, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void getVideoPath() {
        mVideoModel.getPlayUrl(mProgram._id, new DefaultLiveHttpCallBack<String>() {
            @Override
            public void success(String s) {
                if (!isFinishing()) {
                    mVideoPlayFragment = VideoPlayFragment.getInstence(s, mProgram.spic, 1, mProgram._id,mProgram.userinfo._id);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(cn.gietv.mvr.R.id.video_fragment, mVideoPlayFragment,"videoplay");
                    ft.commitAllowingStateLoss();
                    mFragmentHeight = mMessageLayout.getHeight();
                }
            }

            @Override
            public void failure(String message) {
                if (!isFinishing()) {
                    ToastUtils.showToast(LivePlayListActivity.this, message);
                }
            }
        });
    }
//在0.2.0中该页面注释掉的内容
//    private void attentionGame() {
//        if (UserUtils.isNotLogin()) {
//            IntentUtils.openActivity(this, LoginActivity.class);
//            return;
//        }
//        mFollowModel = RetrofitUtils.create(FollowModel.class);
//        if (mProgram.game.isfollow == CommConstants.TRUE) {
//            final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "取消关注中，请稍后");
//            mFollowModel.followByContent(mProgram.game._id, CommConstants.FOLLOW_TYPE_GAME, CommConstants.FOLLOW_ACTION_OFF, new DefaultLiveHttpCallBack<String>() {
//                @Override
//                public void success(String s) {
//                    if (isNotFinish()) {
//                        downloadText.setText("+ 关注");
//                        mProgram.game.isfollow = CommConstants.FALSE;
//                        mProgram.game.follows--;
//                        ToastUtils.showToast(LivePlayListActivity.this, "取消关注成功");
//                        dialog.dismiss();
//                        initData();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(LivePlayListActivity.this, message);
//                        dialog.dismiss();
//                    }
//                }
//            });
//        } else {
//            final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "关注中，请稍后");
//            mFollowModel.followByContent(mProgram.game._id, CommConstants.FOLLOW_TYPE_GAME, CommConstants.FOLLOW_ACTION_ON, new DefaultLiveHttpCallBack<String>() {
//                @Override
//                public void success(String s) {
//                    if (isNotFinish()) {
//                        //第一次关注游戏，赠送游戏币
//                        NoviceTaskUtils.doNoviceTask(ConfigUtils.FIRST_FOLLOW_GAME, LivePlayListActivity.this);
//                        //每日关注任务
//                        NoviceTaskUtils.doDayTask(ConfigUtils.DAY_CONCER_ANCHOR, LivePlayListActivity.this);
//                        downloadText.setText("已关注");
//                        mProgram.game.isfollow = CommConstants.TRUE;
//                        mProgram.game.follows++;
//                        ToastUtils.showToast(LivePlayListActivity.this, "关注成功");
//                        dialog.dismiss();
//                        initData();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(LivePlayListActivity.this, message);
//                        dialog.dismiss();
//                    }
//                }
//            });
//        }
//
//    }

    private void attentionCompere() {
        if (UserUtils.isNotLogin()) {
            IntentUtils.openActivity(this, LoginActivity.class);
            return;
        }
        mFollowModel = RetrofitUtils.create(FollowModel.class);
        if (mProgram.userinfo.isfollow == CommConstants.TRUE) {
            //mLlliveRoom.setVisibility(View.GONE);
          ////  final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "取消关注中，请稍后");
            mFollowModel.followByUser(mProgram.userinfo._id, CommConstants.FOLLOW_ACTION_OFF, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    if (isNotFinish()) {
                        compereAttText.setText("+ 关注");
                        mProgram.userinfo.isfollow = CommConstants.FALSE;
                        mProgram.userinfo.follows--;
                        compereAttCount.setText(NumUtils.w(mProgram.userinfo.follows) + " 人关注");
                        ToastUtils.showToast(LivePlayListActivity.this, "取消关注成功");
                        //  dialog.dismiss();
                        initData();
                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        ToastUtils.showToast(LivePlayListActivity.this, message);
                        // dialog.dismiss();
                    }
                }
            });
        } else {
            //final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "关注中，请稍后");
            mFollowModel.followByUser(mProgram.userinfo._id, CommConstants.FOLLOW_ACTION_ON, new DefaultLiveHttpCallBack<String>() {
                @Override
                public void success(String s) {
                    if (isNotFinish()) {
                        compereAttText.setText("已关注");
                        mProgram.userinfo.isfollow = CommConstants.TRUE;
                        mProgram.userinfo.follows++;
                        ToastUtils.showToast(LivePlayListActivity.this, "关注成功");
                        compereAttCount.setText(NumUtils.w(mProgram.userinfo.follows) + " 人关注");
                        // dialog.dismiss();
                        //关注主播，向主播发送消息
                        try {
                            Gson gson = GsonUtils.getGson();
                            NewsBean newsBean = new NewsBean();
                            newsBean.nickname = CacheUtils.getCacheUserInfo().nickname;
                            newsBean.avatar = CacheUtils.getCacheUserInfo().avatar;
                            newsBean.userId = CacheUtils.getCacheUserInfo()._id;
                            newsBean.time = System.currentTimeMillis();
                            newsBean.message = CacheUtils.getCacheUserInfo().nickname + "已经关注你，你们现在可以聊天了";
                            newsBean.toOrFrom = CacheUtils.getCacheUserInfo()._id;
                            newsBean.read = ConfigUtils.MESSAGE_NOT_READ;
                            Chat chat = XmppConnection.getInstance().getConnection().getChatManager().createChat(newsBean.userId, null);
                            chat.sendMessage(gson.toJson(newsBean));
                            // mqttService.getMqttController().sendMessage(ConfigUtils.PRIVATE_CHAT + mProgram.userinfo._id, gson.toJson(newsBean));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        temp = 1;
                        initData();

                    }
                }

                @Override
                public void failure(String message) {
                    if (isNotFinish()) {
                        ToastUtils.showToast(LivePlayListActivity.this, message);
                        //dialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendMessage() {
        String message = mContentEdit.getText().toString();
        if (StringUtils.isEmpty(message)) {
            ToastUtils.showToast(this, "请输入内容");
            return;
        }
        if (UserUtils.isNotLogin()) {
            IntentUtils.openActivity(this, LoginActivity.class);
            return;
        }
            ArrayList<BlackListBean.BlackName> blackList = CacheUtils.getCacheBlackList(mProgram._id);
            System.out.println(blackList);
            if(blackList != null) {
                if(blackList != null && blackList.size() > 0){
                    for(BlackListBean.BlackName blackName : blackList){
                        if(blackName.uid.equals(CacheUtils.getCacheUserInfo()._id) && System.currentTimeMillis() < blackName.gagtime){
                            ToastUtils.showToast(this,"您已经被加入黑名单，" + TimeUtil.getTimeString(blackName.gagtime) + "之后再发言");
                            return ;
                        }
                    }
                }
            }

            mMessageFragment.sendMessage(ConfigUtils.USER_CHAT,message,null,null);
            mContentEdit.setText("");
            //每日发言任务
           if(NoviceTaskUtils.getData(ConfigUtils.TASK_DAY_SEND_MSG, LivePlayListActivity.this)){
                long count = NoviceTaskUtils.getDbData(ConfigUtils.TASK_DAY_SEND_MSG,LivePlayListActivity.this);
                NoviceTaskUtils.saveCount(ConfigUtils.TASK_DAY_SEND_MSG,LivePlayListActivity.this,++count);
               if(count == 5){
                   NoviceTaskUtils.updateTask(ConfigUtils.TASK_DAY_SEND_MSG,1);
               }
            }else{
               NoviceTaskUtils.saveCount(ConfigUtils.TASK_DAY_SEND_MSG,LivePlayListActivity.this,1);
           }
    }

    private int softKeyGoneHeight;
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("直播页");
        findViewById(cn.gietv.mvr.R.id.shade).setVisibility(View.GONE);
        mStartTime = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mFragmentHeight = mMessageLayout.getHeight();
                softKeyGoneHeight = mBottomLayout.getTop();
            }
        }.start();
        mMessageModel.getMqttTopic(mProgram._id, new DefaultLiveHttpCallBack<BlackListBean>() {//获取Topic的接口，改为获取黑名单的接口
            @Override
            public void success(BlackListBean blackListBean) {
                CacheUtils.saveBlackList(mProgram._id, blackListBean);
                //System.out.println(blackListBean.mycontribution_coin.levelimg);
                CacheUtils.saveMyContributionCoin(blackListBean.mycontribution_coin, mProgram._id);
                CacheUtils.saveMyContributionJinjiao(blackListBean.mycontribution_jinjiao, mProgram._id);
            }

            @Override
            public void failure(String message) {
                showError(message);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.gietv.mvr.R.layout.live_play_list_layout);
        initViews();
        mWindowHeight = getWindowManager().getDefaultDisplay().getHeight();
        getProgramEntity();
        //添加社交平台
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.video_tv_share) {
            showSharePopu();
        }else if(view.getId() == R.id.video_tv_compere_attention) {
            attentionCompere();
        }else if(view.getId() == R.id.video_btn_send) {
            sendMessage();
        }else if(view.getId() == R.id.video_iv_compere_image) {
            CompereActivity.openCompereActivity(this, mProgram.userinfo._id);
        }else if (view.getId() == R.id.video_ivbt_gift) {
            if (UserUtils.isNotLogin()) {
                IntentUtils.openActivity(this, LoginActivity.class);
                return;
            }
            if (mProgram.userinfo._id != null && mProgram.userinfo._id.equals(CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID))) {
                ToastUtils.showToast(LivePlayListActivity.this, "自己不能给自己送金币");
            } else {
                if (CacheUtils.getPropList() == null) {
                    queryProps();
                } else {
                    showPopupWindow(CacheUtils.getPropList());
                }

            }
        }else if(view.getId() == R.id.video_tv_video_call){
            *//*    if(UserUtils.isNotLogin()){
                    IntentUtils.openActivity(this,LoginActivity.class);
                }else{
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment  fragment = fm.findFragmentByTag("videoplay");
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(fragment);
                    ft.add(R.id.video_fragment,VideoCallFragment.getInstance(mProgram.userinfo._id), "videocall");
                    ft.commit();
                    if(mVideoCallListener != null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mVideoCallListener.videoCalling();
                    }
                }*//*
        }
    }
    private SharePopWindow mSharePopWindow;
    private void showSharePopu() {
        InputUtils.closeInputKeyBoard(mContentEdit);
        if(mSharePopWindow == null ) {
            mSharePopWindow = new SharePopWindow(this, mProgram.spic, mProgram.shareurl, "我正在看直播   " + mProgram.name);
            mSharePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mSharePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mSharePopWindow.setFocusable(true);
            mSharePopWindow.setOutsideTouchable(true);
            mSharePopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        int height = live_play_list_parent.getHeight();
        mSharePopWindow.showAtLocation(mMessageLayout, Gravity.NO_GRAVITY, 0, height - mSharePopWindow.getHeight()- DensityUtil.dip2px(this, 52));
    }

    private void showPopupWindow(PropBeanList propBeanList) {
        InputUtils.closeInputKeyBoard(mContentEdit);
        if(giftPopWindow == null) {
            giftPopWindow = new GiftPopWindow(this, propBeanList, mMessageFragment, mProgram.userinfo._id, mProgram._id);
            giftPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            giftPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            giftPopWindow.setFocusable(true);
            giftPopWindow.setOutsideTouchable(true);
            giftPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        WindowManager wm = this.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        giftPopWindow.showAtLocation(mMessageLayout, Gravity.NO_GRAVITY, 0, height - DensityUtil.dip2px(this, 310) - DensityUtil.dip2px(this, 52));
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment  fragment = fm.findFragmentByTag("videocall");
        FragmentTransaction ft = fm.beginTransaction();
        if(fragment == null){
            super.onBackPressed();
        }else {
            ft.remove(fragment);
            ft.commit();
            getVideoPath();
            if(mVideoCallListener != null){
                mVideoCallListener.videoCallOver();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
    private VideoCallListener mVideoCallListener;
    public void setVideoCallListener(VideoCallListener videoCall){
        this.mVideoCallListener = videoCall;
    }
   public interface VideoCallListener{
        void videoCalling();
        void videoCallOver();
    }
    private void queryProps(){
        GiftModel model = RetrofitUtils.create(GiftModel.class);
        model.queryProps(new DefaultLiveHttpCallBack<PropBeanList>() {
            @Override
            public void success(PropBeanList propBeanList) {
                CacheUtils.savePropList(propBeanList);//保存礼物列表
                showPopupWindow(propBeanList);
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        *//**使用SSO授权必须添加如下代码 *//*
        UMSsoHandler ssoHandler =  UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if(giftPopWindow != null && giftPopWindow.dialog != null){
            if(giftPopWindow.dialog.isShowing()){
                giftPopWindow.dialog.dismiss();
            }
        }
            if(requestCode == 66 && resultCode == 88){
                if(giftPopWindow != null && giftPopWindow.dialog != null){
                    if(giftPopWindow.dialog.isShowing()){
                        giftPopWindow.dialog.dismiss();
                    }
                }
            final String propName = data.getExtras().getString("propname");
            final int count = data.getExtras().getInt("count");
            final String propid = data.getExtras().getString("propid");
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
                final String propimg = data.getExtras().getString("propimg");
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
                    GiveGiftUtil.giveGiftJinjao(propid,mProgram.userinfo._id, mMessageFragment, count / 100, propName, mProgram._id,propimg);
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setContentView(dialogView);
        }
    }*/
}
/*默认分享样式，代码
*  UMSocialService controller = UMServiceFactory.getUMSocialService("com.umeng.share");
                controller.setShareContent("我正在看直播   " + mProgram.name);
                UMVideo umVideo = new UMVideo(mProgram.shareurl);
                // 设置视频缩略图
                umVideo.setThumb(mProgram.spic);
                umVideo.setTitle("独角视频直播");
                controller.setShareMedia(umVideo);
                controller.setShareBoardListener(new SocializeListeners.UMShareBoardListener() {
                    @Override
                    public void onShow() {
                        findViewById(R.id.shade).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDismiss() {
                        findViewById(R.id.shade).setVisibility(View.GONE);
                    }
                });
                controller.openShare(this, false);
* */