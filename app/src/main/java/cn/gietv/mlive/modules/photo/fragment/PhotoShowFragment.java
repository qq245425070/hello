package cn.gietv.mlive.modules.photo.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseFragment;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * author：steven
 * datetime：15/10/20 20:51
 *
 */
public class PhotoShowFragment extends AbsBaseFragment implements ImageLoadingListener {
    public static final String EXTRA_URL = "extra_url";
    private View mCurrentView;
    private RelativeLayout mRelativeLayout;
    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;
    private ImageLoader mImageLoader;
    private String mUrl;

    public static PhotoShowFragment getInstence(String url) {
        PhotoShowFragment fragment = new PhotoShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.photo_show_item_layout, container, false);
        mUrl = getArguments().getString(EXTRA_URL);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getActivity());
        mImageView = (ImageView) mCurrentView.findViewById(R.id.photo_show_iv_image);
        mRelativeLayout = (RelativeLayout) mCurrentView.findViewById(R.id.photo_show_re_layout);
        mAttacher = new PhotoViewAttacher(mImageView);
        mImageLoader.displayImage(mUrl, mImageView);

        return mCurrentView;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        mAttacher.update();
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
