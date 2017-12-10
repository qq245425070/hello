package cn.gietv.mlive.modules.video.fragment;

/**
 * author：steven
 * datetime：15/10/11 11:07
 */
public class LivePlayFragment {/*extends AbsBaseFragment {
    public static final String EXTRA_PATH = "extra_path";
    public static final String EXTRA_IMAGE_URL = "extra_image_url";
    public static final String EXTRA_PRO_ID = "extra_pro_id";
    private String path;
    private String imageUrl;

    private View mCurrentView;
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private AudioManager mAudioManager;

    private ImageLoader mImageLoader;
    private ImageView mFontImage;
    *//**
     * 最大声音
     *//*
    private int mMaxVolume;
    *//**
     * 当前缩放模式
     *//*
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private MediaController mMediaController;

    public static LivePlayFragment getInstence(String path, String imageUrl, String proId) {
        LivePlayFragment fragment = new LivePlayFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PATH, path);
        bundle.putString(EXTRA_IMAGE_URL, imageUrl);
        bundle.putString(EXTRA_PRO_ID, proId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.video_layout, container, false);
        path = getArguments().getString(EXTRA_PATH);
        imageUrl = getArguments().getString(EXTRA_IMAGE_URL);
        if (StringUtils.isEmpty(path)) {
            ToastUtils.showToast(getActivity(), "视频地址为空");
        }
        Vitamio.isInitialized(getActivity());
        mVideoView = (VideoView) mCurrentView.findViewById(R.id.surface_view);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getActivity());
        mFontImage = (ImageView) mCurrentView.findViewById(R.id.video_play_iv_font);
        mVideoView.setVideoPath(path);
        mMediaController = new MediaController(getActivity(), true, mCurrentView);
        mMediaController.getRoot().setVisibility(View.GONE);
        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();
        mImageLoader.displayImage(imageUrl, mFontImage);
//        mMediaController.setIsShowSeekBar(false);
//        mMediaController.setIsShowStartTime(false);
//        mMediaController.setIsShowTotalTime(false);
//        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mFontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFontImage.setVisibility(View.GONE);
                mVideoView.start();
            }
        });
        return mCurrentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }*/
}
