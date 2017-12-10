package cn.gietv.mlive.modules.photo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.gietv.mlive.base.AbsArrayAdapter;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.modules.photo.activity.PhotoShowActivity;
import cn.gietv.mlive.modules.photo.bean.PhotoBean;
import cn.gietv.mlive.utils.DensityUtil;
import cn.gietv.mlive.utils.ImageLoaderUtils;
import cn.gietv.mlive.utils.IntentUtils;
import cn.gietv.mlive.utils.ViewHolder;
import cn.gietv.mlive.views.SquareImageView;

/**
 * author：steven
 * datetime：15/10/20 19:44
 */
public class PhotoUpdateAdapter extends AbsArrayAdapter<PhotoBean.PhotosEntity> {
    private ImageLoader mImageLoader;
    private Set<String> mCheckedPhoto = new HashSet<>();
    int mWidth;
    int mHeight;
    private List<PhotoBean.PhotosEntity> mList;

    public PhotoUpdateAdapter(Context context, List<PhotoBean.PhotosEntity> objects) {
        super(context, objects);
        this.mList = objects;
        mImageLoader = ImageLoaderUtils.getDefaultImageLoader(getContext());
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        mWidth = (width - DensityUtil.dip2px(getContext(), 8) * 3) / 2;
        mHeight = (int) (mWidth / 1.33);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(cn.gietv.mlive.R.layout.photo_update_item, null);
        }
        SquareImageView photoImage = ViewHolder.get(convertView, cn.gietv.mlive.R.id.photo_update_iv_image);
        final ImageView checkedImage = ViewHolder.get(convertView, cn.gietv.mlive.R.id.photo_update_iv_check);
        photoImage.setRatio(1.33f);
        photoImage.setBackgroundColor(Color.WHITE);
        if(position == 0){
            photoImage.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), cn.gietv.mlive.R.mipmap.upload_black));
            checkedImage.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((AbsBaseActivity) getContext()).startActivityForResult(i, 0);
                }
            });
            photoImage.setBackgroundColor(Color.parseColor("#ededed"));
        }else{
            PhotoBean.PhotosEntity bean = getItem(position - 1);
            final String url = bean.url;
            mImageLoader.displayImage(bean.url, photoImage);
            if (mCheckedPhoto.contains(url)) {
                checkedImage.setVisibility(View.VISIBLE);
            } else {
                checkedImage.setVisibility(View.GONE);
            }
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mCheckedPhoto.contains(url)) {
                        checkedImage.setVisibility(View.GONE);
                        mCheckedPhoto.remove(url);
                    } else {
                        checkedImage.setVisibility(View.VISIBLE);
                        mCheckedPhoto.add(url);
                    }
                    return true;
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PhotoShowActivity.EXTRA_POSITION, position-1);
                    bundle.putSerializable(PhotoShowActivity.EXTRA_PHOTO_LIST, (Serializable) mList);
                    IntentUtils.openActivity(getContext(), PhotoShowActivity.class, bundle);
                }
            });
        }
        return convertView;
    }

    public void clearChecked() {
        mCheckedPhoto.clear();
    }

    public Set<String> getChecked() {
        return mCheckedPhoto;
    }
}
