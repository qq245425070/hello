package cn.gietv.mlive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.gietv.mlive.MainApplication;
import cn.gietv.mlive.R;

/**
 * author：steven
 * datetime：15/9/21 10:57
 *
 */
public class ImageLoaderUtils {

    private static ImageLoader imageLoader;
    private static ImageLoaderUtils instance;

    public static ImageLoaderUtils getInstance() {
        if (null == instance) {
            instance = new ImageLoaderUtils();
        }
        return instance;
    }
    public static void init(Context context){
        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
        options.bitmapConfig(Bitmap.Config.RGB_565);
        options.cacheInMemory(false);
        options.cacheOnDisk(true);
        options.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
//        options.showImageOnFail(R.drawable.default_image);
        options.showImageOnLoading(R.drawable.default_loading);
        //options.showImageForEmptyUri(R.drawable.default_image);
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options.build())
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory().threadPoolSize(Thread.NORM_PRIORITY)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
        imageLoader.init(configuration);
    }
    public static ImageLoader getDefaultImageLoader(Context context) {
        if(imageLoader == null){
            init(MainApplication.getInstance());
        }
        return imageLoader;
    }
    public void downLoadImage(final Context context,final ImageView mImageView, final String imageUrl,
                              final boolean bAnimation) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        ImageLoader.getInstance().displayImage(imageUrl, mImageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mImageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });


    }
}
