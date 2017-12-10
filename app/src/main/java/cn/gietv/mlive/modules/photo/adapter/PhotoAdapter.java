package cn.gietv.mlive.modules.photo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.modules.photo.activity.PhotoShowActivity;
import cn.gietv.mlive.modules.photo.bean.PhotoBean;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.views.SquareImageView;


/**
 * author：steven
 * datetime：15/10/16 23:56
 */
public class PhotoAdapter extends AbsArrayAdapter<PhotoBean.PhotosEntity> {
    private ImageLoader mImageLoader;
    private List<PhotoBean.PhotosEntity> mList;
    int mWidth;
    int mHeight;

    public PhotoAdapter(Context context, List<PhotoBean.PhotosEntity> objects) {
        super(context, objects);
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
        this.mList = objects;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        mWidth = (width - DensityUtil.dip2px(getContext(), 8) * 2) / 2;
        mHeight = (int) (mWidth / 1.33);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new SquareImageView(getContext());
            SquareImageView image = (SquareImageView) convertView;
            image.setBackgroundColor(Color.WHITE);
            image.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mHeight));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        mImageLoader.displayImage(getItem(position).url, (SquareImageView) convertView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(PhotoShowActivity.EXTRA_POSITION, position);
                bundle.putSerializable(PhotoShowActivity.EXTRA_PHOTO_LIST, (Serializable) mList);
                IntentUtils.openActivity(getContext(), PhotoShowActivity.class, bundle);
            }
        });
        return convertView;
    }
}
