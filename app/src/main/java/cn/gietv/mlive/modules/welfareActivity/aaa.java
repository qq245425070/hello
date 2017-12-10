package cn.gietv.mlive.modules.welfareActivity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 作者：houde on 2016/11/25 11:28
 * 邮箱：yangzhonghao@gietv.com
 */
public class aaa {
    OkHttpClient okHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
            .url("https://openapi.youku.com/v2/comments/by_video.json?client_id=d0014dda41970fe5&video_id=XMTgxNDY3MTg0OA==&page=1&count=20")
            .build();
    String id = "XMTg1ODk3MzYwNA";
    public void getAllComment(){
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                response.body().string();
            }
        });
    }
}
