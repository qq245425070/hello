package cn.gietv.mlive.http;

import cn.gietv.mlive.constants.HttpConstants;
import cn.kalading.android.retrofit.utils.HttpResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * author：steven
 * datetime：15/9/17 11:07
 */
public abstract class DefaultLiveHttpCallBack<T> implements Callback<HttpResponse<T>> {
    @Override
    public void success(HttpResponse<T> tHttpResponse, Response response) {
        if (tHttpResponse != null) {
            switch (tHttpResponse.status) {
                case HttpConstants.RESULT_SUCCESS:
                    success(tHttpResponse.data);
                    break;
                default:
                    failure(tHttpResponse.msg);
                    break;
            }
        } else {
            failure("服务器数据错误");
        }
    }

    @Override
    public void failure(RetrofitError error) {
        String message;
        if (error.getKind().equals(RetrofitError.Kind.HTTP)) {
            message = "网络连接异常。";
        } else if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
            message = "网络连接异常。";
        } else {
            message = "url:" + error.getUrl() + "\n" + "message" + error.getMessage();
        }
        failure(message);
        System.out.println("error message = " + message);
    }

    public abstract void success(T t);

   // public void success(String message){}

    public abstract void failure(String message);
}
