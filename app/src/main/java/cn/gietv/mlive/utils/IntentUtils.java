package cn.gietv.mlive.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by steven on 15/7/15.
 */
public class IntentUtils {
    public static void openActivity(Context context, Class clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity, Class clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openActivityForResult(Activity activity, Class clazz, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivityForResult(intent, requestCode);
    }
}
