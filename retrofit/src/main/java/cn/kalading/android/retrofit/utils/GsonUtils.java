package cn.kalading.android.retrofit.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * author：steven
 * datetime：15/9/2 13:57
 */
public class GsonUtils {
    private volatile static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            synchronized (GsonUtils.class) {
                if (gson == null) {
                    GsonBuilder builder = new GsonBuilder();
                    gson = builder.create();
                }
            }
        }
        return gson;
    }
}
