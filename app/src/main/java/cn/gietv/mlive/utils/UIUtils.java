package cn.gietv.mlive.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import cn.gietv.mlive.MainApplication;

/**
 * Created by houde on 2016/6/2.
 */
public class UIUtils {

    public static Context getContext() {
        return MainApplication.getInstance();
    }
    /** 获取资源 */
    public static Resources getResources() {
        return getContext().getResources();
    }
    /** 获取颜色选择器 */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }
    /** 获取drawable */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }
}
