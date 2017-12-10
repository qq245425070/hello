package cn.gietv.mlive.modules.video.activity;

/**
 * author：steven
 * datetime：15/10/8 21:14
 */
public class VideoPlayListActivity {//extends AbsBaseActivity implements OnClickListener {
//    public static final String EXTRA_PROGRAM = "extra_program";
//    private TextView attentionText, downloadText, gameNameText, gameDlCount, compereNameText, compereAttCount, compereAttText, titleText;
//    private RoundedImageView gameImage, compereImage;
//    private ListView mCommentList;
//    private EditText mContentEdit;
//    private ImageButton mSendButton,shareText,mShoucangText,mDownloadButton;
//    private LinearLayout mBottomLayout;
//    private ImageView smal_iv1,smal_iv2;
//    private View line1,line2;
//    public ProgramBean.ProgramEntity mProgram;
//    private LinearLayout mGameParent,mAttentionParent;
//    private MessageModel mMessageModel;
//    private FollowModel mFollowModel;
//    private GameModel mGameModel;
//    private CompereModel mCompereModel;
//    private VideoModel mVideoModel;
//    private StatisticsMode mStatisticsMode;
//    private ImageLoader mImageLoader;
//    private String mGameDownloadUrl;
//    private String mGameFileName;
//    private MessageAdapter adapter;
//    private VideoPlayFragment3 mVideoPlayFragment;
//    private VideoPlayFragment2 mVideoPlayFragment2;
//    private long mStartTime;
//    private CustomFrameLayout mCustomFrameLayout;
//    private RelativeLayout titleParent;
//    private MultiUserChat chatRoom;
//    private TextView mSendMsgButton;
//    private void initViews() {
//        mProgram = (ProgramBean.ProgramEntity) getIntent().getExtras().getSerializable(EXTRA_PROGRAM);
//        mSendMsgButton = (TextView) findViewById(R.id.send_msg_button);
//        mSendMsgButton.setOnClickListener(this);
//        titleParent = (RelativeLayout) findViewById(R.id.title_parent);
//        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(this);
//        gameNameText = (TextView) findViewById(R.id.video_tv_game_name);
//        mDownloadButton = (ImageButton) findViewById(R.id.video_tv_download);
//        mDownloadButton.setVisibility(View.VISIBLE);
//        mDownloadButton.setOnClickListener(this);
//        gameDlCount = (TextView) findViewById(R.id.video_tv_game_download_count);
//        gameImage = (RoundedImageView) findViewById(R.id.video_iv_game_image);
//        downloadText = (TextView) findViewById(R.id.video_tv_game_attention);
//        compereNameText = (TextView) findViewById(R.id.video_tv_compere_name);
//        compereAttCount = (TextView) findViewById(R.id.video_tv_compere_attention_count);
//        compereAttText = (TextView) findViewById(R.id.video_tv_compere_attention);
//        compereImage = (RoundedImageView) findViewById(R.id.video_iv_compere_image);
//        mShoucangText = (ImageButton) findViewById(R.id.video_tv_shoucang);
//        mShoucangText.setBackgroundResource(R.drawable.live_share);
////        mShoucangText.setVisibility(View.GONE);
//        titleText = (TextView) findViewById(R.id.video_tv_title);
//        shareText = (ImageButton) findViewById(R.id.video_tv_share);
//        shareText.setBackgroundResource(R.drawable.jiantou);
////        shareText.setVisibility(View.GONE);
//        attentionText = (TextView) findViewById(R.id.video_tv_attention);
//        smal_iv1 = (ImageView) findViewById(R.id.smal_iv1);
//        smal_iv2 = (ImageView) findViewById(R.id.smal_iv2);
//        smal_iv1.setBackgroundResource(R.drawable.lubo_smal_down);
//        smal_iv2.setBackgroundResource(R.drawable.lubo_smal_plus);
//        mGameParent = (LinearLayout) findViewById(R.id.game_parent);
//        mAttentionParent = (LinearLayout) findViewById(R.id.ll_live_room);
//        line1 = findViewById(R.id.line);
//        line2 = findViewById(R.id.line2);
//        gameImage.setOnClickListener(this);
//        compereImage.setOnClickListener(this);
//        mCustomFrameLayout = (CustomFrameLayout) findViewById(R.id.video_message_parent);
//        mCommentList = (ListView) findViewById(R.id.video_lv_comment);
//        mContentEdit = (EditText) findViewById(R.id.video_et_content);
//        mSendButton = (ImageButton) findViewById(R.id.video_btn_send);
//        mBottomLayout = (LinearLayout) findViewById(R.id.video_ll_bottom);
//        mContentEdit.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    sendMessage();
//                    return true;
//                }
//                return false;
//            }
//        });
//        mMessageModel = RetrofitUtils.create(MessageModel.class);
//        mGameModel = RetrofitUtils.create(GameModel.class);
//        mCompereModel = RetrofitUtils.create(CompereModel.class);
//        mVideoModel = RetrofitUtils.create(VideoModel.class);
//        mStatisticsMode = RetrofitUtils.create(StatisticsMode.class);
//
//        mShoucangText.setOnClickListener(this);
//        mStatisticsMode.playSta(mProgram._id, 1, new DefaultLiveHttpCallBack<String>() {
//            @Override
//            public void success(String s) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mStatisticsMode.playSta(mProgram._id, 2, new DefaultLiveHttpCallBack<String>() {
//            @Override
//            public void success(String s) {
//
//            }
//            @Override
//            public void failure(String message) {
//
//            }
//        });
//    }
//
//    private void initData() {
////        if(mProgram.game != null) {
////            gameNameText.setText("游戏：" + mProgram.game.name);
////        }
//        attentionText.setText(NumUtils.w(mProgram.onlines));
//        compereNameText.setText(mProgram.userinfo.nickname);
////        gameDlCount.setText(NumUtils.w(mProgram.game.programnums) + " 视频");
////        mGameDownloadUrl = mProgram.game.url_android;
////        mGameFileName = mProgram.game.name + ".apk";
//        compereAttCount.setText(NumUtils.w(mProgram.userinfo.follows) + " 人关注");
//        mImageLoader.displayImage(mProgram.userinfo.avatar, compereImage);
////        mImageLoader.displayImage(mProgram.game.spic, gameImage);
//        titleText.setText((mProgram.name.length() > 14) ? mProgram.name.substring(0,14) + "..." : mProgram.name);
//
//        mSendButton.setOnClickListener(this);
//        downloadText.setOnClickListener(this);
//        compereAttText.setOnClickListener(this);
//        shareText.setOnClickListener(this);
////        if("qita".equals(mProgram.game._id)){
////            mGameParent.setVisibility(View.GONE);
////            line2.setVisibility(View.GONE);
////        }
//        if (mProgram.userinfo.isfollow == CommConstants.TRUE) {
//            compereAttText.setText("已关注");
//            compereAttText.setBackgroundResource(R.drawable.commen_button_light_green_selected);
//            compereAttText.setTextColor(getResources().getColorStateList(R.color.color_green_button_selected));
//        } else {
//            compereAttText.setText("关注");
//            compereAttText.setBackgroundResource(R.drawable.commen_button_light_green);
//            compereAttText.setTextColor(getResources().getColorStateList(R.color.color_green_button));
//        }
//
////        if (PackageUtils.hasInstalled(this, mProgram.game.package_name)) {
////            downloadText.setText("打开");
////        } else {
////            downloadText.setText("安装");
////        }
////        downloadText.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (PackageUtils.hasInstalled(VideoPlayListActivity.this, mProgram.game.package_name)) {
////                    PackageUtils.openApplication(VideoPlayListActivity.this, mProgram.game.package_name);
////                    mStatisticsMode.gameAction(mProgram.game._id, 2, new DefaultLiveHttpCallBack<String>() {
////                        @Override
////                        public void success(String s) {
////
////                        }
////
////                        @Override
////                        public void failure(String message) {
////
////                        }
////                    });
////                } else {
////                    DownloadController controller = new DownloadController(VideoPlayListActivity.this);
////                    controller.startDownload(mProgram.game.name + ".apk", mProgram.game.url_android);
////                    mProgram.game.download++;
////                    mStatisticsMode.gameAction(mProgram.game._id, 1, new DefaultLiveHttpCallBack<String>() {
////                        @Override
////                        public void success(String s) {
////
////                        }
////
////                        @Override
////                        public void failure(String message) {
////
////                        }
////                    });
////                }
////            }
////        });
//        Drawable d = getResources().getDrawable(R.drawable.commen_shoucang_icon_select);
//        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
//        Drawable dS = getResources().getDrawable(R.drawable.commen_shoucang_icon);
//        dS.setBounds(0, 0, dS.getMinimumWidth(), dS.getMinimumHeight());
//    }
//
//    private void showError(String message) {
//        ToastUtils.showToast(VideoPlayListActivity.this,message);
//    }
//
//    private void getProgramEntity() {
////        ProgramModel programModel = RetrofitUtils.create(ProgramModel.class);
////        programModel.getProgramById(mProgram._id, new DefaultLiveHttpCallBack<ProgramBean.ProgramEntity>() {
////            @Override
////            public void success(ProgramBean.ProgramEntity programEntity) {
////                if (isNotFinish()) {
////                    mProgram = programEntity;
////                    initData();
////                    getMessage();
////                }
////            }
////
////            @Override
////            public void failure(String message) {
////                if (isNotFinish()) {
////                    showError(message);
////                }
////            }
////        });
//        getVideoPath();
//        VideoModel model = RetrofitUtils.create(VideoModel.class);
//        model.queryProByProid(mProgram._id, new DefaultLiveHttpCallBack<VideoPlayBean>() {
//            @Override
//            public void success(VideoPlayBean videoPlayBean) {
//                mProgram = videoPlayBean.program;
//                initData();
//                getMessage();
//
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//        });
//    }
//
//    private void getMessage() {
//        mMessageModel.getMessageByProId(mProgram._id, CommConstants.COMMON_PAGE_COUNT, 1, CommConstants.DEFAULT_SORT_ONLINE_PERSON, new DefaultLiveHttpCallBack<MessageBean>() {
//            @Override
//            public void success(MessageBean messageBean) {
//                if (isNotFinish() && messageBean.messages != null) {
//                    adapter = new MessageAdapter(VideoPlayListActivity.this, messageBean.messages, false, mProgram.fanstitle);
//                    mCommentList.setAdapter(adapter);
////                    mCommentList.setSelection(adapter.getCount() - 1);
////                    getVideoPath();
//                }
//            }
//
//            @Override
//            public void failure(String message) {
//                showError(message);
//            }
//        });
//    }
//
//    private String videoPath;
//    private void getVideoPath() {
//        mVideoModel.getPlayUrl(mProgram._id, new DefaultLiveHttpCallBack<String>() {
//            @Override
//            public void success(String s) {
//                if (!isFinishing()) {
//                    videoPath = s;
////                    mVideoPlayFragment = VideoPlayFragment2.getInstence(s, mProgram.spic, MediaController.VIDEO_MODEL_VIDEO_SMALL, mProgram._id, mProgram.userinfo._id,mProgram.collect);
////                    if(mProgram.type == 2) {
////                        mVideoPlayFragment = VideoPlayFragment3.getInstence(s);
////                        FragmentManager fm = getSupportFragmentManager();
////                        FragmentTransaction ft = fm.beginTransaction();
////                        ft.add(R.id.video_fragment, mVideoPlayFragment);
////                        ft.commitAllowingStateLoss();
//                    }
//                    else {
////                        mVideoPlayFragment2 = VideoPlayFragment2.getInstence(s, mProgram.spic, MediaController.VIDEO_MODEL_VIDEO_SMALL, mProgram._id, mProgram.userinfo._id, mProgram.type);
////                        FragmentManager fm = getSupportFragmentManager();
////                        FragmentTransaction ft = fm.beginTransaction();
////                        ft.add(R.id.video_fragment, mVideoPlayFragment2);
////                        ft.commitAllowingStateLoss();
//                    }
//
//            }
//
//            @Override
//            public void failure(String message) {
//                if (!isFinishing()) {
//                    showError(message);
//                }
//            }
//        });
//    }
//
//    private void attentionGame() {
//        if (UserUtils.isNotLogin()) {
//            IntentUtils.openActivity(this, LoginActivity.class);
//            return;
//        }
//        mFollowModel = RetrofitUtils.create(FollowModel.class);
////        if (mProgram.game.isfollow == CommConstants.TRUE) {
////            mFollowModel.followByContent(mProgram.game._id, CommConstants.FOLLOW_TYPE_GAME, CommConstants.FOLLOW_ACTION_OFF, new DefaultLiveHttpCallBack<String>() {
////                @Override
////                public void success(String s) {
////                    if (isNotFinish()) {
////                        downloadText.setText("+ 关注");
////                        mProgram.game.isfollow = CommConstants.FALSE;
////                        ToastUtils.showToast(VideoPlayListActivity.this, "取消关注成功");
////                        initData();
////                    }
////                }
////
////                @Override
////                public void failure(String message) {
////                    if (isNotFinish()) {
////                        ToastUtils.showToast(VideoPlayListActivity.this, message);
////                    }
////                }
////            });
////        } else {
////            mFollowModel.followByContent(mProgram.game._id, CommConstants.FOLLOW_TYPE_GAME, CommConstants.FOLLOW_ACTION_ON, new DefaultLiveHttpCallBack<String>() {
////                @Override
////                public void success(String s) {
////                    if (isNotFinish()) {
////                        downloadText.setText("已关注");
////                        mProgram.game.isfollow = CommConstants.TRUE;
////                        ToastUtils.showToast(VideoPlayListActivity.this, "关注成功");
////                        initData();
////                    }
////                }
////
////                @Override
////                public void failure(String message) {
////                    if (isNotFinish()) {
////                        ToastUtils.showToast(VideoPlayListActivity.this, message);
////                    }
////                }
////            });
////        }
//
//    }
//
//    private void handleShoucang() {
//        if (UserUtils.isNotLogin()) {
//            IntentUtils.openActivity(this, LoginActivity.class);
//            return;
//        }
//        mFollowModel = RetrofitUtils.create(FollowModel.class);
//        if (mProgram.isfollow == CommConstants.TRUE) {
//            mFollowModel.followByContent(mProgram._id, 2, 2, new DefaultLiveHttpCallBack<String>() {
//                @Override
//                public void success(String s) {
//                    if (isNotFinish()) {
//                        mProgram.isfollow = CommConstants.FALSE;
//                        ToastUtils.showToast(VideoPlayListActivity.this, "取消收藏成功");
//                        initData();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(VideoPlayListActivity.this, message);
//                    }
//                }
//            });
//        } else {
//            mFollowModel.followByContent(mProgram._id, 2, 1, new DefaultLiveHttpCallBack<String>() {
//                @Override
//                public void success(String s) {
//                    if (isNotFinish()) {
//                        mProgram.isfollow = CommConstants.TRUE;
//                        ToastUtils.showToast(VideoPlayListActivity.this, "收藏成功");
//                        initData();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(VideoPlayListActivity.this, message);
//                    }
//                }
//            });
//        }
//    }
//
//    private void attentionCompere() {
//        if (UserUtils.isNotLogin()) {
//            IntentUtils.openActivity(this, LoginActivity.class);
//            return;
//        }
//        mFollowModel = RetrofitUtils.create(FollowModel.class);
//        if (mProgram.userinfo.isfollow == CommConstants.TRUE) {
//            mFollowModel.follow(mProgram.userinfo._id, CommConstants.FOLLOW_ACTION_OFF, mProgram.userinfo.type, new DefaultLiveHttpCallBack<String>() {
//                @Override
//                public void success(String s) {
//                    if (isNotFinish()) {
//                        compereAttText.setText("+ 关注");
//                        mProgram.userinfo.isfollow = CommConstants.FALSE;
//                        mProgram.userinfo.follows--;
//                        compereAttCount.setText(NumUtils.w(mProgram.userinfo.follows) + " 人关注");
//                        ToastUtils.showToast(VideoPlayListActivity.this, "取消关注成功");
//                        initData();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(VideoPlayListActivity.this, message);
//                    }
//                }
//            });
//        } else {
//            mFollowModel.follow(mProgram.userinfo._id, CommConstants.FOLLOW_ACTION_ON,mProgram.userinfo.type, new DefaultLiveHttpCallBack<String>() {
//                @Override
//                public void success(String s) {
//                    if (isNotFinish()) {
//                        compereAttText.setText("已关注");
//                        mProgram.userinfo.isfollow = CommConstants.TRUE;
//                        mProgram.userinfo.follows++;
//                        compereAttCount.setText(NumUtils.w(mProgram.userinfo.follows) + " 人关注");
//                        ToastUtils.showToast(VideoPlayListActivity.this, "关注成功");
//                        initData();
//                    }
//                }
//
//                @Override
//                public void failure(String message) {
//                    if (isNotFinish()) {
//                        ToastUtils.showToast(VideoPlayListActivity.this, message);
//                    }
//                }
//            });
//        }
//    }
//
//    private void sendMessage() {
//        if (UserUtils.isNotLogin()) {
//            IntentUtils.openActivity(this, LoginActivity.class);
//            return;
//        }
//        final String message = mContentEdit.getText().toString();
//        if (StringUtils.isEmpty(message)) {
//            ToastUtils.showToast(this, "请输入内容");
//            return;
//        }
//        if(message.length() > 110){
//            ToastUtils.showToast(this, "输入字数太多，请分多条发送");
//            return;
//        }
//        final ProgressDialog dialog = ProgressDialogUtils.createShowDialog(this, "正在发送留言");
//        long currentPosition = 1000;//mVideoPlayFragment.mVideoView.getCurrentPosition()/1000;
//        mMessageModel.sendMessage(mProgram._id, mContentEdit.getText().toString(), String.valueOf(currentPosition), new DefaultLiveHttpCallBack<MessageBean.MessagesEntity>() {
//            @Override
//            public void success(MessageBean.MessagesEntity messagesEntity) {
//                try {
////                    chatRoom.sendMessage(GsonUtils.getGson().toJson(messagesEntity));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (isNotFinish()) {
//                    adapter.add(messagesEntity);
//                    adapter.notifyDataSetChanged();
//                    mContentEdit.setText("");
//                    mCommentList.setSelection(adapter.getCount() - 1);
//                    dialog.dismiss();
//                    InputUtils.closeInputKeyBoard(mContentEdit);
//                }
//            }
//
//            @Override
//            public void failure(String message) {
//                if (isNotFinish()) {
//                    dialog.dismiss();
//                    ToastUtils.showToast(VideoPlayListActivity.this, message);
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        final String id ;
//        if(UserUtils.isNotLogin()){
//            id = VersionUtils.getDeviceId(this);
//        }else{
//            id = CacheUtils.getCacheUserInfo()._id;
//        }
//        mStartTime = SystemClock.currentThreadTimeMillis();
//
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mCustomFrameLayout.setOnSizeChangedListener(new CustomFrameLayout.OnSizeChangedListener() {
//                    @Override
//                    public void onSizeChanged(int w, int h, int oldw, int oldh) {
//                        if (oldh > h) {
//                            mGameParent.setVisibility(View.INVISIBLE);
//                            mAttentionParent.setVisibility(View.INVISIBLE);
//                            line1.setVisibility(View.INVISIBLE);
//                            line2.setVisibility(View.INVISIBLE);
//                        } else {
//                            mGameParent.setVisibility(View.VISIBLE);
//                            mAttentionParent.setVisibility(View.VISIBLE);
//                            line1.setVisibility(View.VISIBLE);
//                            line2.setVisibility(View.VISIBLE);
//                        }
//                            mGameParent.setVisibility(View.GONE);
//                            line2.setVisibility(View.GONE);
//                    }
//                });
//            }
//        }.start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//    private RelativeLayout mVideoParent;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.video_play_list_layout);
//        initViews();
//        getProgramEntity();
//        mVideoParent = (RelativeLayout) findViewById(R.id.video_list_parent);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.video_tv_share) {
//            handleShoucang();
//        }else if(view.getId() == R.id.video_tv_game_attention) {
//            attentionGame();
//        }else if(view.getId() == R.id.video_tv_compere_attention) {
//            attentionCompere();
//        }else if(view.getId() == R.id.video_btn_send) {
//            sendMessage();
//        }else if(view.getId() == R.id.send_msg_button) {
//            sendMessage();
//        }else if(R.id.video_iv_game_image  == view.getId()) {
////            GameInfoActivity.openGameInfoActivity(this, mProgram.game._id, mProgram.game.name, 3);
//        }else if(view.getId() == R.id.video_iv_compere_image) {
////            CompereActivity.openCompereActivity(this, mProgram.userinfo._id);
//            AnchorActivity.openAnchorActivity(this, mProgram.userinfo._id, mProgram.userinfo.nickname);
//        }else if(view.getId() == R.id.video_tv_download) {
//            showPopuwindow();
//        }else if(view.getId() == R.id.video_tv_shoucang){
//                showSharePopu();
//        }
//    }
//    private SharePopWindow mSharePopWindow;
//    private void showSharePopu() {
//        InputUtils.closeInputKeyBoard(mContentEdit);
//        if(mSharePopWindow == null ) {
//            mSharePopWindow = new SharePopWindow(this, mProgram.spic, mProgram.shareurl, "我正在看直播   " + mProgram.name);
//            mSharePopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//            mSharePopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            mSharePopWindow.setFocusable(true);
//            mSharePopWindow.setOutsideTouchable(true);
//            mSharePopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        int height = mVideoParent.getHeight();
//        mSharePopWindow.showAtLocation(mCustomFrameLayout, Gravity.NO_GRAVITY, 0, height - mSharePopWindow.getHeight()- DensityUtil.dip2px(this, 52));
//    }
//    private DownloadPopuWindow popuWindow;
//    private void showPopuwindow() {
//        InputUtils.closeInputKeyBoard(mContentEdit);
//        List<DownLoadBean> beans = new ArrayList<>();
//        DownLoadBean bean = new DownLoadBean();
//        bean.setCheck(false);
//        bean.setTitle(mProgram.name);
//        bean.setUrl(videoPath);
//        bean.setImagePath(mProgram.spic);
//        bean.setAnchor(mProgram.userinfo.nickname);
//        bean.setVideoId(mProgram._id);
//        beans.add(bean);
//        popuWindow = new DownloadPopuWindow(this,beans);
//        popuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        popuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popuWindow.setFocusable(true);
//        popuWindow.setOutsideTouchable(true);
//        popuWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popuWindow.showAsDropDown(titleParent);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /**使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler =  UMServiceFactory.getUMSocialService("com.umeng.share").getConfig().getSsoHandler(requestCode) ;
//        if(ssoHandler != null){
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//    }


}
