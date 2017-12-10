package cn.gietv.mlive.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


/**
 *
 */
public final class SharedPreferenceUtils {
    private static final String TAG = SharedPreferenceUtils.class
            .getSimpleName();

    /**
     * 分隔符
     */
    private static final String PROP_VALUE_SEPARATOR = ",";
    public static final String DEFAULT_SHARE_NAME = "APP_DEFAULT";

    private static SharedPreferences sharedPreferences = null;

    /**
     * 只调用一次，不需要考虑线程安全，
     * 两个init方法最多各加载一次
     *
     * @param context
     */
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(DEFAULT_SHARE_NAME,
                Activity.MODE_PRIVATE);
    }

    public static void init(String name, Context context) {
        sharedPreferences = context.getSharedPreferences(name,
                Activity.MODE_PRIVATE);
    }

    public static void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public static void remove(String[] keys) {
        Editor edit = sharedPreferences.edit();
        for (String key : keys) {
            edit.remove(key);
        }
        edit.apply();
    }

    public static boolean saveProp(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }

    public static boolean saveProp(String key, boolean value) {
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean saveProp(String key, int value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public static int getInt(String key) {
        return getInt(key, -1);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public static boolean isContainsValue(String key, String value) {
        String oriValue = getString(key);
        if (oriValue != null && value != null) {
            String[] arrValues = oriValue.split(PROP_VALUE_SEPARATOR);
            for (String sValue : arrValues) {
                if (value.trim().equals(sValue.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean addValue(String key, String value) {
        if (!isContainsValue(key, value)) {
            String oriValue = getString(key);
            if (oriValue != null) {
                StringBuilder sbValue = new StringBuilder(oriValue);
                sbValue.append(PROP_VALUE_SEPARATOR).append(value);
                return saveProp(key, sbValue.toString());
            }
        }
        return saveProp(key, value);
    }

    /**
     * 清除数据
     */
    public static void clearData() {
        Log.v(TAG, "清除sharedPreferences");
        sharedPreferences.edit().clear().apply();
    }

}
