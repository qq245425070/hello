package cn.kalading.android.retrofit.utils;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Retrofit工具类
 * author：steven
 * datetime：15/9/2 12:01
 */
public class RetrofitUtils {
    /**
     * RestAdapter
     */
    private volatile static RestAdapter adapter;
    /**
     * header map
     */
    public static HashMap<String, String> mHeaderMap = new HashMap<>();
    /**
     * 服务器url
     */
    private static String mBaseServiceUrl;

    /**
     * 初始化Retrofit
     *
     * @param baseServiceUrl 服务器Url
     */
    public static void initRetrofit(String baseServiceUrl) {
        mBaseServiceUrl = baseServiceUrl;
    }

    public static void addHeader(Map<String, String> headerMap) {
        mHeaderMap.putAll(headerMap);
        adapter = null;
    }

    public static void removeHeader(String name) {
        mHeaderMap.remove(name);
        adapter = null;
    }

    public static void addHeader(String name, String value) {
        mHeaderMap.put(name, value);
        adapter = null;
    }

    public static RestAdapter getRestAdapter() {
        if (adapter == null) {
            synchronized (RetrofitUtils.class) {
                if (adapter == null) {
                    RestAdapter.Builder builder = new RestAdapter.Builder();
                    builder.setEndpoint(mBaseServiceUrl);
                    builder.setClient(new OkClient());
                    builder.setRequestInterceptor(new MyHttpRequestInterceptor());
                    builder.setLogLevel(RestAdapter.LogLevel.FULL);
                    adapter = builder.build();
                }
            }
        }
        return adapter;
    }

    public static <T> T create(Class<T> clazz) {
        return getRestAdapter().create(clazz);
    }

    private static class MyHttpRequestInterceptor implements RequestInterceptor {

        @Override
        public void intercept(RequestFacade request) {
            for (Map.Entry<String, String> entry : mHeaderMap.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }
    }
}
