package cn.gietv.mlive.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by steven on 15/7/15.
 */
public class ToastUtils {

    public static void showToast(Context context, String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void showToastShort(Context context, String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
