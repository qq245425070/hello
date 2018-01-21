package com.houde.competitive.lagua.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author : houde
 *         Date : 18-1-20
 *         Desc :
 */

public class GlideUtils {
    public static void display(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
    public static void display(Fragment context,String url,ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
}
