package cn.gietv.mlive.modules.video.fragment;

import cn.gietv.mlive.base.AbsBaseFragment;
/**
 * author：steven
 * datetime：15/10/8 19:38
 */
public class VideoPlayFragment extends AbsBaseFragment {
//    public static final String EXTRA_PATH = "extra_path";
//    public static final String EXTRA_IMAGE_URL = "extra_image_url";
//    public static final String EXTRA_MODEL = "extra_model";
//    public static final String EXTRA_PRO_ID = "extra_id";
//    public static final String EXTRA_USER_ID = "extra_user_id";
//    private String path;
//    private String imageUrl;
//
//    private View mCurrentView;
//    public VideoView mVideoView;
//    private View mVolumeBrightnessLayout;
//    private ImageButton mExitBtn;
//    private AudioManager mAudioManager;
//
//    private ImageLoader mImageLoader;
//    private ImageView mFontImage;
////    private ProgressWheel mWheel;
////    private ProgressHelper mProgressHelper;
//    /**
//     * 最大声音
//     */
//    private int mMaxVolume;
//    /**
//     * 当前缩放模式
//     */
//    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
//    private MediaController mMediaController;
//
//    private boolean mHasCreated = false;
//    private long mCurrentPosition;
//    public static VideoPlayFragment getInstence(String path, String imageUrl, int model, String proId,String userid) {
//        VideoPlayFragment fragment = new VideoPlayFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRA_PATH, path);
//        bundle.putInt(EXTRA_MODEL, model);
//        bundle.putString(EXTRA_IMAGE_URL, imageUrl);
//        bundle.putString(EXTRA_PRO_ID, proId);
//        bundle.putString(EXTRA_USER_ID,userid);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mCurrentView = inflater.inflate(R.layout.video_layout, container, false);
//        path = getArguments().getString(EXTRA_PATH);
//        imageUrl = getArguments().getString(EXTRA_IMAGE_URL);
//        if (StringUtils.isEmpty(path)) {
//            ToastUtils.showToast(getActivity(), "视频地址为空");
//        }
//        Vitamio.isInitialized(getActivity());
//        mVideoView = (VideoView) mCurrentView.findViewById(R.id.surface_view);
//        mVolumeBrightnessLayout = mCurrentView.findViewById(R.id.operation_volume_brightness);
//        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getActivity());
//        mFontImage = (ImageView) mCurrentView.findViewById(R.id.video_play_iv_font);
//        mExitBtn = (ImageButton) mCurrentView.findViewById(R.id.head_ib_exit);
//        mExitBtn.setVisibility(View.VISIBLE);
//        mExitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().finish();
//            }
//        });
//        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
////        mWheel = (ProgressWheel) mCurrentView.findViewById(R.id.imageViewLoading);
////        mProgressHelper = new ProgressHelper(getActivity());
////        mProgressHelper.setBarColor(Color.parseColor("#999999"));
////        mProgressHelper.setProgressWheel(mWheel);
////        mProgressHelper.spin();
////
////        mWheel.setVisibility(View.VISIBLE);
//
//
//        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        System.out.println(path + ".....................");
//        mVideoView.setVideoPath(path);
////        mMediaController = new MediaController(getActivity());
//        mMediaController = new MediaController(getActivity(), true, mCurrentView);
//        mMediaController.getRoot().setVisibility(View.GONE);
//        mMediaController.setModel(getArguments().getInt(EXTRA_MODEL));
//        mMediaController.setFullScreenClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getArguments().getInt(EXTRA_MODEL) == MediaController.VIDEO_MODEL_LIVE_SMALL) {
//                    LivePlayActivity.openLivePlayActivity(getActivity(), path, "", getArguments().getString(EXTRA_PRO_ID), getArguments().getString(EXTRA_USER_ID),((LivePlayListActivity)getActivity()).mProgram);
//                    mVideoView.stopPlayback();
//                    getActivity().finish();
//                } else {
//                    mCurrentPosition = mVideoView.getCurrentPosition();
//                    Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
//                    intent.putExtra("extra_path", path);
//                    intent.putExtra("extra_position", mVideoView.getCurrentPosition());
//                    intent.putExtra("extra_room_id", getArguments().getString(EXTRA_PRO_ID));
//                    intent.putExtra("extra_program",((VideoPlayListActivity)getActivity()).mProgram);
//                    startActivityForResult(intent, 0);
////                    mProgressHelper.stopSpinning();
//                    mVideoView.stopPlayback();
//                    getActivity().finish();
//                }
//            }
//        });
//
//        mVideoView.setMediaController(mMediaController);
//        mVideoView.requestFocus();
////        mImageLoader.displayImage(imageUrl, mFontImage);
////        mVideoView.start();
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                // optional need Vitamio 4.0
//                mediaPlayer.setPlaybackSpeed(1.0f);
//                mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_PARENT, 1.78f);
////                mWheel.setVisibility(View.GONE);
//            }
//        });
//
//        mFontImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mFontImage.setVisibility(View.GONE);
//                mVideoView.start();
//            }
//        });
//        return mCurrentView;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mVideoView.pause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            mVideoView.seekTo(data.getLongExtra("position", mCurrentPosition));
//        }
//         mVideoView.resume();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if (mVideoView != null)
//            mVideoView.setVideoLayout(mLayout, 0);
//        super.onConfigurationChanged(newConfig);
//    }
}
