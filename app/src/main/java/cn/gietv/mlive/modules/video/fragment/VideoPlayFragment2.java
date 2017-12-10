package cn.gietv.mlive.modules.video.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.modules.video.activity.VRVideoPlayActivity;
import cn.gietv.mlive.modules.video.activity.VideoPlayListActivity3;
import cn.gietv.mlive.modules.video.activity.VideoPlayerActivity2;
import cn.gietv.mlive.utils.ImageLoaderUtils;

/**
 * author：steven
 * datetime：15/10/8 19:38
 */
public class VideoPlayFragment2 extends AbsBaseFragment {
    public static final String EXTRA_PATH = "extra_path";
    public static final String EXTRA_IMAGE_URL = "extra_image_url";
    public static final String EXTRA_MODEL = "extra_model";
    public static final String EXTRA_PRO_ID = "extra_id";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_COLLECT = "extra_collect";
    private String path;
    private String imageUrl;
    private ImageLoader mImageLoader;
    private ImageView mVideoImage,mExitImage,mVideoPlayImage;
    private String mID;
    private View mCurrentView;
    public static VideoPlayFragment2 getInstence(String path, String imageUrl, String proId,int type,String name) {
        VideoPlayFragment2 fragment = new VideoPlayFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PATH, path);
        bundle.putString(EXTRA_IMAGE_URL, imageUrl);
        bundle.putString(EXTRA_PRO_ID, proId);
        bundle.putInt(EXTRA_COLLECT,type);
        bundle.putString("name",name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.video_layout2, container, false);
        path = getArguments().getString(EXTRA_PATH);
        imageUrl = getArguments().getString(EXTRA_IMAGE_URL);
        mID = getArguments().getString(EXTRA_PRO_ID);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getActivity());
        mVideoImage = (ImageView) mCurrentView.findViewById(R.id.video_image);
        mExitImage = (ImageView) mCurrentView.findViewById(R.id.exit_image);
        mVideoPlayImage = (ImageView) mCurrentView.findViewById(R.id.video_play);
        mImageLoader.displayImage(imageUrl,mVideoImage);
        mExitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mVideoPlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().getInt(EXTRA_COLLECT)==6) {
//                    MD360PlayerActivity.startVideo(VideoPlayFragment2.this.getActivity(), Uri.parse(path));
                    VRVideoPlayActivity.openVRVideoPlayActivity(VideoPlayFragment2.this.getActivity(),0,0,((VideoPlayListActivity3) getActivity()).mProgram,path,getArguments().getString("name"));
                    getActivity().finish();
                }else if(getArguments().getInt(EXTRA_COLLECT)==9){
                    VideoPlayerActivity2.openVideoPlayerActivity(getActivity(),path,mID,0,getArguments().getString("name"),9);
//                    getActivity().finish();
                }
            }
        });
        return mCurrentView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
