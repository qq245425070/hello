package cn.gietv.mlive.modules.usercenter.bean;

import android.graphics.Bitmap;

/**
 * Created by houde on 2016/4/22.
 */
public class LoadedImage {
    Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
